package Client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.text.StyleContext.SmallAttributeSet;

public class ClienteTCP extends Thread{

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
	
	private final static String IP_SERVER = "40.86.87.167";
	private final static int PUERTO_SERVER = 8080;
	
	private static String token;
	private static String us;
	
	public ClienteTCP(){
	}

	/**
	 * Metodo auxiliar para leer mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public static String readBR(BufferedReader br) throws Exception{
		String msj = br.readLine();
		System.out.println("CLI: " + msj);

		return msj;
	}

	/**
	 * Metodo auxiliar para enviar mensaje por el socket e imprimir en consola los mensajes de comunicacion
	 */
	public static void writePW(PrintWriter pw, String msj) throws Exception{
		pw.println(msj);
		System.out.println("SVR: " + msj);

	}

	public static String sendVideo(String file, String name) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		String rta = "";
		
		try{
			socket = new Socket(IP_SERVER, PUERTO_SERVER);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);
			
			writePW(pw, C_SUBIR + ":::"+us+":::"+token+":::"+name);
			
			String sMsj = readBR(br);
			if (sMsj.equals(S_OK)){
				FileInputStream fis = new FileInputStream(file);
		        BufferedInputStream bis = new BufferedInputStream(fis);
				byte [] mybytearray  = new byte [(int)file.length()];
		         fis = new FileInputStream(file);
		         bis = new BufferedInputStream(fis);
		         bis.read(mybytearray,0,mybytearray.length);
		         out.write(mybytearray,0,mybytearray.length);
		         out.flush();
		         bis.close();
		         fis.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		} finally{
			try{
				out.close();
				in.close();
				socket.close();
				return rta;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}

	@SuppressWarnings("finally")
	public static String login(String text, String password) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		String rta = "";
		try{
			socket = new Socket(IP_SERVER, PUERTO_SERVER);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);

			writePW(pw, C_LOGIN + ":::"+text+":::"+password);

			String sMsj = readBR(br);
			if (sMsj.equals(S_USUARIO_NOK) || sMsj.equals(S_PASSWORD_NOK))
				rta = sMsj;
			
			else{
				token = sMsj.split(":::")[1];
				us = text;
				rta = sMsj;
			}

		}catch(Exception e){
			e.printStackTrace();
		} finally{
			try{
				out.close();
				in.close();
				socket.close();
				return rta;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
	}

	public static String register(String text, String password) {
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		String rta = "";
		try{
			socket = new Socket(IP_SERVER, PUERTO_SERVER);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(out, true);

			writePW(pw, C_REGISTRAR + ":::"+text+":::"+password);

			String sMsj = readBR(br);
			rta = sMsj;
			
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
		return rta;
	}
}
