package Server;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.DefaultDirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.directaudio.DefaultDirectAudioPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.linux.LinuxVideoSurfaceAdapter;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.net.DatagramSocket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

public class CanalStreaming {
	
	//-----------------------------------------
	// Networking
	//-----------------------------------------
	private String address;
	private DatagramSocket socket;
	
	//-----------------------------------------
	// Video management
	//-----------------------------------------
	public static final int HEADER_SIZE = 3;
	public static final int IMG_FLAG = 2;
	public static final int SOUND_HEADER_SIZE = 11;
	public static final int AUDIO_FLAG = 3;
	
	//-----------------------------------------
	// Sound
	//-----------------------------------------
	private int sampleRateSound;
	private int sampleFormatSound;
	private int channelsSound;
	
	//-----------------------------------------
	// Video
	//-----------------------------------------
	private String videoFile;
	
	private RenderCallbackAdapter render;
	private DefaultDirectMediaPlayer media;
	private DefaultDirectAudioPlayer audio;
	
	public CanalStreaming(String fileName, String address, int port){
		this.videoFile  = fileName;
		this.address = address;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public synchronized void setUp(){
		MediaPlayer mediaPlayer = new DefaultDirectMediaPlayer(libvlc, instance, bufferFormatCallback, renderCallback);
        
	}
}
