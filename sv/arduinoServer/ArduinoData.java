package sv.arduinoServer;

public class ArduinoData<T> {
	
	T data;
	private String hostName;
	private int id;
	public ArduinoData(T data, String hostName, int id) {
		this.data = data;
		this.hostName = hostName;
		this.setId(id);
	}
	public String getHostName() {
		return hostName;
	}
	
	public String toString() {
		return this.data.toString() + " from: " + this.hostName;
	}
	public T getData() {
		return this.data;
		// TODO Auto-generated method stub
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
