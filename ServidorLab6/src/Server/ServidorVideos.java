package Server;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.print.attribute.standard.Severity;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServidorVideos extends JFrame{

	/**
	 * Constante que especifica el tiempo máximo en milisegundos que se esperara 
	 * por la respuesta de un cliente en cada una de las partes de la comunicación
	 */
	private static final int TIME_OUT = 10000;

	/**
	 * Constante que especifica el numero de threads que se usan en el pool de conexiones.
	 */
	public static final int N_THREADS = 4;

	/**
	 * Puerto en el cual escucha el servidor.
	 */
	public static final int PUERTO = 8080;

	/**
	 * Archivo con los usuarios
	 */
	public final static String RUTA_US = "/data/usuarios.json";


	private PrintWriter pwTCP;
	private PrintWriter pwUDP;

	/**
	 * Id de los clientes
	 */
	public int idUDP;

	public int idTCP;
	/**
	 * Metodo main del servidor con seguridad que inicializa un 
	 * pool de threads determinado por la constante nThreads.
	 * @param args Los argumentos del metodo main (vacios para este ejemplo).
	 * @throws IOException Si el socket no pudo ser creado.
	 */
	public static void main(String[] args) throws IOException {
		ServidorVideos sv = new ServidorVideos();
		sv.iniciarCom();
		sv.setVisible(true);
		sv.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Hash map que contienen la informacion de login
	 */
	public static HashMap<String, String> hashLogin = new HashMap<String, String>();

	/**
	 * Hash map que contiene la informacion de tokens para verificar si un usuario ha iniciado sesion
	 */
	public static HashMap<String, String> hashToken = new HashMap<String, String>();

	/**
	 * Ruta al archivo JSON con la informacion de usuarios
	 */
	public final static String RUTA_JSON = "usuariosLog.json";

	/**
	 * Metodo que atiende a los usuarios.
	 */
	public void iniciarCom() {
		idUDP = 0;
		idTCP = 0;
		File fison = new File(RUTA_JSON);
		if(!fison.exists()){
			try {
				fison.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
		Runnable serverRunTCP = new Runnable(){

			@Override
			public void run() {
				ServerSocket servidorSocket = null;
				try{
					servidorSocket = new ServerSocket(PUERTO);
					System.out.println("Listo para recibir conexiones TCP");
					while(true){
						Socket cliente = servidorSocket.accept();

						cliente.setSoTimeout(TIME_OUT);
						idTCP++;
						pool.execute(new ComunicacionTCP(cliente));
					}
				}catch(Exception e){
					System.err.println("Ocurrio un error");
					e.printStackTrace();
				}finally{
					try{
						servidorSocket.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		};

		//		Runnable serverRunUDP = new Runnable(){
		//
		//			@Override
		//			public void run() {
		//				DatagramSocket servidorSocket = null;
		//				try{
		//					servidorSocket = new DatagramSocket(PUERTO);
		//					System.out.println("Listo para recibir conexiones UDP");
		//					while(true){
		//						byte[] buf = new byte[256];
		//						DatagramPacket cliente = new DatagramPacket(buf, buf.length);
		//						servidorSocket.receive(cliente);
		//						idUDP++;
		//						pwUDP = new PrintWriter(new FileWriter(UDP, true));
		//						pool.execute(new ComunicacionUDP(servidorSocket, cliente, idUDP, pwUDP));
		//					}
		//
		//				}catch(Exception e){
		//					System.err.println("Ocurrio un error");
		//					e.printStackTrace();
		//				}finally{
		//					try{
		//						servidorSocket.close();
		//					}
		//					catch(Exception e){
		//						e.printStackTrace();
		//					}
		//
		//				}
		//			}
		//		};

		Thread serverTCP = new Thread(serverRunTCP);
		serverTCP.start();

		//		Thread serverUDP = new Thread(serverRunUDP);
		//		serverUDP.start();

	}


	public void cargarTablasHash(){
		JSONParser parser = new JSONParser();

		try{
			Object obj = parser.parse(new FileReader(RUTA_JSON));

			JSONObject jsonObject = (JSONObject) obj;

			for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();

				hashLogin.put(key, (String) jsonObject.get(key));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void dispose(){
		JSONObject obj = new JSONObject();
		Iterator it = hashLogin.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			obj.put(pair.getKey(), pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}

		try {
			FileWriter file = new FileWriter(RUTA_US, true);
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
