package sv.arduinoServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import sv.debugSuite.DebugSuite;
import sv.distributed2.Server2;
import sv.distributed2.Syncable;

public class ArdunioReceiveThread implements Runnable {

	private static final boolean showOutput = true;
	private Socket clientSocket;

	public ArdunioReceiveThread(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
		InputStream is = this.clientSocket.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		while (is.available()>0) {
			DebugSuite.conditionalOutput(""+is.read(),showOutput);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
