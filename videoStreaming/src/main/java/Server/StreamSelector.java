package Server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TimerTask;
public class StreamSelector extends TimerTask{

	private ArrayList<String> videoChannels;
	/**
	 * Network 
	 */
	//Adress of the streaming
	private String address;
	//Socket where the packets (audio and image) will be sent
	private DatagramSocket socket;
	private int address_count;
	private int port_count;
	public final static String CHAN_SEP = "#";
	
	public StreamSelector(DatagramSocket socket) {
		address_count = 4;
		port_count = 8889;
		createVideoChannels ();
		this.socket = socket;
		
	}
	
	public ArrayList<String> getVideoChannels () {
		return videoChannels;
	}
	
	public void createVideoChannels () {
		videoChannels = new ArrayList<String>();
		File folder = new File("data/");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String videoChannel = "data/"+ listOfFiles[i].getName() + ServidorVideos.VIDEO_SEP +  ServidorVideos.ADDR + address_count + ServidorVideos.VIDEO_SEP + port_count;
				videoChannels.add(videoChannel);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
			address_count++;
			port_count ++;
		}
	}
	
	public String addChannel (String fileName) {
		String add = ServidorVideos.ADDR + address_count;
		int port = port_count;
		String videoChannel =  fileName + ServidorVideos.VIDEO_SEP +  add+ ServidorVideos.VIDEO_SEP + port_count;
		videoChannels.add(videoChannel);
		address_count++;
		port_count++;
		return add + ServidorVideos.VIDEO_SEP + port ;
	}

	@Override
	public void run() {
		String message = "";
		for (String chann : videoChannels) {
			message += chann + CHAN_SEP;
		}
		//send the packet as a DatagramPacket over the UDP socket 
		InetAddress addr;
		try {
			addr = InetAddress.getByName("224.0.0.3");
			int port = 4446;
			byte [] sendData = message.getBytes();
			DatagramPacket senddp = new DatagramPacket(sendData, sendData.length, addr, port);
			socket.send(senddp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
