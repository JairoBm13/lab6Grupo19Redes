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

public class ClienteTCPLogin extends AbstractJavaSamplerClient{

	private final static String C_LOGIN = "LOGIN";
	private final static String C_REGISTRAR = "REGISTRAR";
	private final static String S_LOGOK = "LOGIN OK";
	private final static String S_REGOK = "REGISTRO OK";
	private final static String S_REGNOK = "REGISTRO NO OK";
	private final static String S_OK = "OK";
	private final static String S_USUARIO_NOK = "USUARIO INCORRECTO";
	private final static String S_PASSWORD_NOK = "PASSWORD INCORRECTO";
	private final static String C_SUBIR = "SUBIR";
	private final static String C_LISTA = "LISTAR";
	private final static String C_LOGOUT = "LOGOUT";
	private final static String C_REPRODUCIR = "REPRODUCIR";
	private final static String S_ERROR = "ERROR";
	
	private final static String IP_SERVER = "52.165.29.8";
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

			writePW(pw, C_LOGIN + ":::usuario1:::usuario1");

			String sMsj = readBR(br);



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
