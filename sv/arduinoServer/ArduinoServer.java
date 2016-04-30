package sv.arduinoServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sv.debugSuite.DebugSuite;
import sv.distributed2.SyncThread;

public class ArduinoServer {
	
	private int serverPort = 23;// Default
	private static final boolean debugGui = true;
	private static final boolean DEBUG_TEXT = false;
	private static final boolean showDataInfo = true;
	public static int debugGuiID;
	public boolean endServer = false;
	public static int delay = 5000;
	
	public static ArrayList<Object> receivedDataBuffer = new ArrayList<Object>();
	
	
	
	public ArduinoServer() {
		// TODO Auto-generated constructor stub
	}
	public ArduinoServer(int port) {
		this.serverPort = port;
	}
	static ArrayList<ArduinoClient> clients = new ArrayList<ArduinoClient>();
	public void startServer(String hostName) {
		DebugSuite.output("Creating Server");
		ArduinoClient ac = new ArduinoClient(hostName,serverPort,clients.size());
		clients.add(ac);
		Thread svThread = new Thread(ac);
		svThread.start();
		DebugSuite.output("Listening on: " + hostName +":" + serverPort);
	}

	
	static List<ArduinoData<?>> dataArray = Collections.synchronizedList(new ArrayList<ArduinoData<?>>());
	
	public static void addData(ArduinoData<?> data) {
		dataArray.add(data);
		DebugSuite.conditionalOutput(data.toString(), DEBUG_TEXT);
	}
	public static List<ArduinoData<?>> getDataArray() {
		// TODO Auto-generated method stub
		return  dataArray;
	}
	public static boolean hasData() {
		if (dataArray.isEmpty()) {
			
			//DebugSuite.conditionalOutput("dataArray is empty", showDataInfo);
			return false;
			
		} else {
			return true;
		}
	}
	
	public static ArduinoData<?> takeNextData() {
		ArduinoData<?> nextData = dataArray.get(0);
		dataArray.remove(0);
		return nextData;
		
	}
	public static int hostLookup(String hostName) {
		int i=0;
		int rtrn = -1;
		for (ArduinoClient c:clients) {
			if (c.hostName.equals(hostName) || c.hostName.endsWith(hostName) || hostName.endsWith(c.hostName)) {
				rtrn = i;
			}
			i++;
		}
		if (rtrn==-1) {
			DebugSuite.pause("Hostname not found in array");
		}
		return rtrn;
	}

}
