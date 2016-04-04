package Client;

public class Canal {
	private String name;
	private String address;
	private int port;
	
	public Canal(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;
	}
	
	public String getName () {
		return name;
	}
	
	public String getAddress () {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public String toString() {
		return name;
	}
}
