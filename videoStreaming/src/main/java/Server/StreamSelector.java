package Server;

import java.io.File;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.TimerTask;

import com.uniandes.streaming.server.StreamingServer;

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
	
	private void createVideoChannels() {
		// TODO Auto-generated method stub
		
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
				String videoChannel = "data/"+ listOfFiles[i].getName() + StreamingServer.VIDEO_SEP +  StreamingServer.ADDR + address_count + StreamingServer.VIDEO_SEP + port_count;
				videoChannels.add(videoChannel);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
			address_count++;
			port_count ++;
		}
	}
	
	public String addChannel (String fileName) {
		String add = StreamingServer.ADDR + address_count;
		int port = port_count;
		String videoChannel =  fileName + StreamingServer.VIDEO_SEP +  add+ StreamingServer.VIDEO_SEP + port_count;
		videoChannels.add(videoChannel);
		address_count++;
		port_count++;
		return add + StreamingServer.VIDEO_SEP + port ;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
