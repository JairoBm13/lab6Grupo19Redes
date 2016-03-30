package Server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;

public class ComunicacionTCP extends Thread{

	// Constantes de protocolo
	private final static String C_LOGIN = "LOGIN";
	private final static String S_USUARIO = "USUARIO";
	private final static String	S_PASSWORD = "PASSWORD";
	private final static String S_OK = "OK";
	private final static String S_USUARIO_NOK = "USUARIO INCORRECTO";
	private final static String S_PASSWORD_NOK = "PASSWORD INCORRECTO";
	private final static String C_SUBIR = "SUBIR";
	private final static String C_LISTA = "LISTAR";
	//------------------------------------------------------------------------------

	private final Socket sockCliente;

	private InputStream in;
	private OutputStream out;

	public ComunicacionTCP(Socket cl){
		sockCliente = cl;
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
			PrintWriter pw = new PrintWriter(out);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String msjIni = readBR(br);

			if(msjIni.startsWith(C_LOGIN))
				iniLogin(br, pw);

			else{

				String us = msjIni.split(":::")[1];
				String token = msjIni.split(":::")[2];

				verificarToken(us, token);

				if(msjIni.startsWith(C_SUBIR)){

				}

				else if(msjIni.startsWith(C_LISTA)){

				}

			}
		}catch(Exception e){
			e.printStackTrace();

		}finally{
			try{
				out.close();
				in.close();
				sockCliente.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	// Metodos que manejan el protocolo para cada tipo de acción del usuario
	public void iniLogin(BufferedReader br, PrintWriter pw) throws Exception{
		
		SecureRandom random = new SecureRandom();
	    byte bytes[] = new byte[20];
	    random.nextBytes(bytes);
	    String token = bytes.toString();
	}

	public void iniListar(BufferedReader br, PrintWriter pw) throws Exception{

	}

	public void iniSubir(BufferedReader br, PrintWriter pw)throws Exception{

	}

	// Metodos auxiliares
	public void verificarToken(String us, String token) throws Exception{

	}

}
