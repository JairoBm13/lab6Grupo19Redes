package Client;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.Timer;


public class ClienteUDP {

	//Client statuses
	final static int INIT = 0;
	final static int READY_CHANNEL = 1;
	final static int READY = 2;
	final static int PLAYING = 3;

	//Packet info
	private static int DATAGRAM_MAX_SIZE = 65507;
	public static int HEADER_SIZE = 3;
	public static int SOUND_HEADER_SIZE = 11;

	public static int AUDIO_PACKET = 3;
	public static int IMG_PACKET = 2;

	//Actual status
	private int state;

	//The audio line we'll output sound to; it'll be the default audio device on your system if available
	private static SourceDataLine mLine;

	//The previous icon recieved
	ImageIcon prev_icon; 

	MulticastSocket streamingSocket; //socket to be used to send and receive UDP packets
	MulticastSocket channelStreamingSocket; //socket to be used to send and receive UDP packets
	static int STREAMING_VIDEO_PORT = 4445;
	static int STREAMING_PORT = 4446; //port where the client will receive the RTP packets
	private String channel_inet_addr ; //Direction for the multicast connection
	private Image image;
	Timer timer; //timer used to receive data from the UDP socket
	private Canal channelSelected;

	public ClienteUDP(Timer time) {

		timer = time;
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		//allocate enough memory for the buffer used to receive data from the server
		channel_inet_addr = "224.0.0.3";
	}

	public void setup() {
		System.out.println("setup");
		try {
			InetAddress address = InetAddress.getByName(channel_inet_addr);
			//construct a new DatagramSocket to receive channel info packet
			channelStreamingSocket = new MulticastSocket(STREAMING_PORT);
			channelStreamingSocket.joinGroup(address);

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void play() {
		if (state == READY_CHANNEL)
		{
			state = PLAYING;
			timer.start();
		}
	}

	public void selectChannel (Canal ch) {
		System.out.println("setted" + ch.getAddress());
		if (channelSelected == null || !ch.getAddress().equals(channelSelected.getAddress())) {
			channelSelected = ch;
			state = READY_CHANNEL;
			InetAddress group;
			try {
				group = InetAddress.getByName(ch.getAddress());
				streamingSocket = new MulticastSocket(STREAMING_VIDEO_PORT);
				streamingSocket.joinGroup(group);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}		
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ImageIcon getImage() {
		if (streamingSocket != null && state == PLAYING) {
			byte [] buf2 = new byte[DATAGRAM_MAX_SIZE];
			DatagramPacket rcvdp = new DatagramPacket(buf2, buf2.length);
			try {

				streamingSocket.receive(rcvdp);
				int size = (int) ((buf2[1] & 0xff) << 8 | (buf2[2] & 0xff)); // mask
				int packetType = (short) (buf2[0] & 0xff);
				if ( packetType == IMG_PACKET) {
					byte [] imageData = new byte [size];
					System.arraycopy(buf2, HEADER_SIZE, imageData, 0, size);
					ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
					BufferedImage image = ImageIO.read(bis);
					ImageIcon icon = new ImageIcon(image);
					prev_icon = icon;
					return icon;
				}
				else if (packetType == AUDIO_PACKET) {
					if (mLine == null){
						int sampleRate = (int) ((buf2[3] & 0xff) << 8 | (buf2[4] & 0xff)); // mask
						int sampleFormat = (int) ((buf2[5] & 0xff) << 8 | (buf2[6] & 0xff)); // mask
						int channels = (int) ((buf2[7] & 0xff) << 8 | (buf2[8] & 0xff)); // mask
						try {
							openJavaSound(sampleRate, sampleFormat, channels);
						} catch (LineUnavailableException e) {
							e.printStackTrace();
						}

					}
					int soundSampleSize = (int) ((buf2[9] & 0xff) << 8 | (buf2[10] & 0xff)); // mask
					byte [] soundData = new byte [size];
					System.arraycopy(buf2, SOUND_HEADER_SIZE, soundData, 0, size);
					try{
						mLine.write(soundData, 0,soundSampleSize);
					}
					catch (Exception e) {
						System.out.println("rare error");
					}

					return prev_icon;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	public Canal[] getChannels() {
		if (channelStreamingSocket!= null)
		{	
			String data = "" ;
			try {
				byte [] buffer = new byte[1500];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				channelStreamingSocket.receive(request);
				data = new String(request.getData());
				data = data.trim();
				if (data != "") {
					String [] channels = data.split("#");
					Canal[] chs = new Canal[channels.length];
					for (int i = 0; i < channels.length; i++) {
						String[] attr = channels[i].split("&");
						String name = attr[0].replace("data/", "");
						String address = attr[1];
						int port = Integer.parseInt(attr[2]);
						Canal ch = new Canal(name, address, port);
						chs[i] = ch;
					}
					return chs;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Canal[0];
	}

	private static void openJavaSound(int sampleRate, int sampleFormat, int channels) throws LineUnavailableException
	{	
		AudioFormat audioFormat = new AudioFormat(sampleRate,sampleFormat,channels, true, /* xuggler defaults to signed 16 bit samples */ false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		mLine = (SourceDataLine) AudioSystem.getLine(info);
		/**
		 * if that succeeded, try opening the line.
		 */
		mLine.open(audioFormat);
		/**
		 * And if that succeed, start the line.
		 */
		mLine.start();
	}

	public void pause() {
		if (state == PLAYING)
		{
			state = READY_CHANNEL;
			timer.stop();
		}
	}
}
