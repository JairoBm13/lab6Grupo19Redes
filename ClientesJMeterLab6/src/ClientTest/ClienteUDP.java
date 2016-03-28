package ClientTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class ClienteUDP extends AbstractJavaSamplerClient{

	private final static String IP_SERVER = "192.168.0.13";
	private final static int PUERTO_SERVER = 8080;

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult result = new SampleResult();
		boolean exito = true;
		result.sampleStart();

		try{
			DatagramSocket udpSocket = new DatagramSocket();                
			InetAddress address = InetAddress.getByName(IP_SERVER);
			String locationData =  "3212:::32133:::1231231:::31231";
			byte[] loccationBytes = locationData.getBytes();
			DatagramPacket packet = new DatagramPacket(loccationBytes, locationData.length(),address, PUERTO_SERVER);
			udpSocket.send(packet);

		}catch(Exception e){
			exito = false;
			e.printStackTrace();
		} finally{
			try{

			}catch(Exception e){
				exito = false;
				e.printStackTrace();
			}
		}

		result.sampleEnd();
		result.setSuccessful(exito);
		return result;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("Ip servidor", "192.168.0.13");
		defaultParameters.addArgument("Puerto servidor", "8080");
		return defaultParameters;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {

	}

	@Override
	public void teardownTest(JavaSamplerContext context) {

	}


	public String readBR(BufferedReader br) throws Exception{
		String msj = br.readLine();
		return msj;
	}


	public void writePW(PrintWriter pw, String msj) throws Exception{
		pw.println(msj);
	}

}
