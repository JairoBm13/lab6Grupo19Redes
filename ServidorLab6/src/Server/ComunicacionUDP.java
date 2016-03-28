package Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.xml.ws.handler.MessageContext.Scope;

public class ComunicacionUDP extends Thread{
	
	//-------------------------------------------------------
	// Atributos de trabajo
	//-------------------------------------------------------
	
	private final DatagramPacket sockCliente;
	
	private PrintWriter pw;
	
	//-------------------------------------------------------
	// Constructor
	//-------------------------------------------------------
	public ComunicacionUDP(DatagramSocket sock, DatagramPacket pack, int nId, PrintWriter nPw){
		sockCliente = pack;
		pw = nPw;
	}
	
	//-------------------------------------------------------
	//
	//-------------------------------------------------------
	public void run(){
		byte[] buffer = sockCliente.getData();
		String mensaje = new String(buffer, 0, buffer.length);
		String[] datos = mensaje.split(":::");
		pw.println(sockCliente.getAddress().toString()+","+datos[0]+ ","+datos[1]+","+datos[2]+","+datos[3]);
		System.out.println("UDP de "+sockCliente.getAddress().getHostAddress()+" - Longitud:"+datos[0]+ ", Latitud: "+datos[1]+", Velocidad: "+datos[2]+", Altitud: "+datos[3]);
		DatagramSocket udpSocket = null;
		
		try{
			udpSocket = new DatagramSocket();
	        String ackData = "OK:::";
	        byte[] loccationBytes = ackData.getBytes();
	        InetAddress address = sockCliente.getAddress();
	        int port = sockCliente.getPort();
	        DatagramPacket packet = new DatagramPacket(loccationBytes, ackData.length(), address, port);
	        udpSocket.send(packet);
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				udpSocket.close();
			}
        
		pw.close();
	}
}
