package ClientTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class ClienteTCP extends AbstractJavaSamplerClient{

	private final static String C_HOLA = "HOLA";
	private final static String S_INICIO = "INICIO";
	private final static String C_UBICACION = "UBICACION";
	private final static String S_ACK = "OK";

	private final static String IP_SERVER = "192.168.0.13";
	private final static int PUERTO_SERVER = 8080;

	private Socket socket;
	private InputStream in;
	private OutputStream out;


	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult result = new SampleResult();
		boolean exito = true;
		result.sampleStart();

		try{
			socket = new Socket(IP_SERVER, PUERTO_SERVER);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);

			writePW(pw, C_HOLA);

			String sMsj = readBR(br);

			if(sMsj.equals(S_INICIO)){

				writePW(pw,C_UBICACION+":::12312:::32131:::321313:::5535435");

				if(sMsj.equals(S_ACK));

			}



		}catch(Exception e){
			exito = false;
			e.printStackTrace();
		} finally{
			try{
				out.close();
				in.close();
				socket.close();
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
