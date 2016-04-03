package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP extends Thread{

	private final static String C_HOLA = "HOLA";
	private final static String S_INICIO = "INICIO";
	private final static String C_UBICACION = "UBICACION";
	private final static String S_ACK = "OK";
	private final static String C_TERMINAR = "TERMINAR";
	private final static String S_FIN = "FIN";
	private final static String S_ERROR = "ERROR";

	private Socket socket;
	private InputStream in;
	private OutputStream out;

	
	public ClienteTCP(){
	}

	/**
	 * Metodo auxiliar para leer mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public String readBR(BufferedReader br) throws Exception{
		String msj = br.readLine();
		System.out.println("CLI: " + msj);

		return msj;
	}

	/**
	 * Metodo auxiliar para enviar mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public void writePW(PrintWriter pw, String msj) throws Exception{
		pw.println(msj);
		System.out.println("SVR: " + msj);

	}

	public void run(){
		try{
			socket = new Socket("192.168.56.1", 8080);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);

			writePW(pw, C_HOLA);
			
			String sMsj = readBR(br);
			
			if(sMsj.equals(S_INICIO)){
				
				boolean alive = true;
				for (int i = 0; i<20 && alive; i++){
					
					sleep(1000);
					writePW(pw,C_UBICACION+":::12312:::32131:::321313:::432435");
					sMsj = readBR(br);
					
					if (!sMsj.equals(S_ACK)) alive = false;
					
				}
				
				if (alive){
					writePW(pw, C_TERMINAR);
					sMsj = readBR(br);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		} finally{
			try{
				out.close();
				in.close();
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ClienteTCP().start();
	}
}
