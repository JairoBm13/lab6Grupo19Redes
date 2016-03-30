package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ComunicacionTCP extends Thread{

	// Constantes de protocolo
	private final static String C_LOGIN = "LOGIN";
	private final static String C_REGISTRAR = "REGISTRAR";
	private final static String S_USUARIO = "USUARIO";
	private final static String	S_PASSWORD = "PASSWORD";
	private final static String S_LOGOK = "LOGIN OK";
	private final static String S_REGOK = "REGISTRO OK";
	private final static String S_REGNOK = "REGISTRO NO OK";
	private final static String S_OK = "OK";
	private final static String S_USUARIO_NOK = "USUARIO INCORRECTO";
	private final static String S_PASSWORD_NOK = "PASSWORD INCORRECTO";
	private final static String C_SUBIR = "SUBIR";
	private final static String C_LISTA = "LISTAR";
	private final static String C_LOGOUT = "LOGOUT";
	//------------------------------------------------------------------------------

	/**
	 * Ruta base de la carpeta raiz de los videos de usuarios
	 */
	private final static String RUTA_BASE = "";


	private final Socket sockCliente;

	private InputStream in;
	private OutputStream out;

	public ComunicacionTCP(Socket cl){
		sockCliente = cl;
	}


	// Constantes para encriptacion
	private static final String ALGO = "AES";
	private static final byte[] KEY = "BestKeyEver".getBytes();

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
				iniLogin(msjIni, pw);

			else if(msjIni.startsWith(C_REGISTRAR))
				iniRegistrar(msjIni, pw);

			else{
				String us = msjIni.split(":::")[1];
				String token = msjIni.split(":::")[2];

				verificarToken(us, token);

				if(msjIni.startsWith(C_SUBIR)){

				}
				else if(msjIni.startsWith(C_LISTA)){

				}
				else if(msjIni.startsWith(C_LOGOUT)){
					iniLogout(us, pw);
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
	public void iniLogin(String msj, PrintWriter pw) throws Exception{

		String us = msj.split(":::")[1];
		String pass = msj.split(":::")[2];

		String passStored = ServidorVideos.hashLogin.get(us);
		if (passStored!=null){
			String decryptPassStored = desencriptar(passStored);
			if(decryptPassStored.equals(pass)){
				SecureRandom random = new SecureRandom();
				byte bytes[] = new byte[20];
				random.nextBytes(bytes);
				String token = bytes.toString();

				ServidorVideos.hashToken.put(us, token);

				writePW(pw, S_LOGOK + ":::" + token);
			}
			else{
				writePW(pw, S_PASSWORD_NOK);
			}
		}
		else{
			writePW(pw, S_USUARIO_NOK);
		}

	}

	public void iniRegistrar(String msj, PrintWriter pw) throws Exception{

		String us = msj.split(":::")[1];
		String pass = msj.split(":::")[2];

		String passStored = ServidorVideos.hashLogin.get(us);
		if(passStored==null){

			ServidorVideos.hashLogin.put(us, encriptar(pass));

			Path ruta = Paths.get(RUTA_BASE +us);
			if (Files.notExists(ruta)){
				new File(RUTA_BASE+us).mkdirs();
			}

			writePW(pw, S_REGOK);
		}
		else{
			writePW(pw, S_REGNOK);
		}
	}

	public void iniListar(BufferedReader br, PrintWriter pw) throws Exception{

	}

	public void iniSubir(BufferedReader br, PrintWriter pw)throws Exception{

	}

	public void iniLogout(String us, PrintWriter pw)throws Exception{
		String removido = ServidorVideos.hashToken.remove(us);
		if(removido==null){
			throw new Exception("No existe el usuario");
		}
	}
	// Metodos auxiliares
	public void verificarToken(String us, String token) throws Exception{
		
	}

	// Metodos de encriptacion / desencriptacion
	public String encriptar(String value){
		try{
			Key key = new SecretKeySpec(KEY, ALGO);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrVal = c.doFinal(value.getBytes());
			String msjEncriptar = new String(encrVal) ;
			return msjEncriptar;
		}catch(Exception e){
			e.printStackTrace();
			return "Nope";
		}
	}

	public String desencriptar(String value){
		try{
			Key key = new SecretKeySpec(KEY, ALGO);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decrpValue = c.doFinal(value.getBytes());
			String msjDesencriptar = new String(decrpValue);
			return msjDesencriptar;
		}catch(Exception e){
			e.printStackTrace();
			return "Nope";
		}
	}
}	
