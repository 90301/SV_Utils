package sv.arduinoServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import sv.debugSuite.DebugSuite;

public class ArduinoClient implements Runnable {

	String hostName;
	int port;

	byte inputType = 0;
	private int id;
	public static final byte EXPECT_INT = 0;
	private static final boolean DEBUG_TEXT = true;

	public ArduinoClient(String hostName, int port, int id) {
		this.hostName = hostName;
		this.port = port;
		this.id = id;
	}

	@Override
	public void run() {
		// attempt to connect, if successful feed data to
		// the server, if not, die?
		try {
			Socket clientSocket = new Socket(hostName, port);
			DebugSuite.conditionalOutput("Connection established on: "
					+ hostName + ":" + port, DEBUG_TEXT);
			DebugSuite.conditionalOutput("----: "
          + clientSocket.getInetAddress() + ":" + clientSocket.getPort(), DEBUG_TEXT);
			InputStream is = clientSocket.getInputStream();
			Scanner clientScanner = new Scanner(is);

			boolean running = true;
			
			while (clientSocket.isClosed()!=true) {
				// keep placing the data into the arduinoServer Array
				
				if (inputType == EXPECT_INT) {
					if (clientScanner.hasNextInt()) {
					  
						int val = clientScanner.nextInt();
						DebugSuite.conditionalOutput("got data: " + val,
								DEBUG_TEXT);
						ArduinoData<Integer> data = new ArduinoData<Integer>(
								val, clientSocket.getInetAddress().toString(),
								id);
						ArduinoServer.addData(data);

					} else {
						clientScanner.nextLine();
						// DebugSuite.conditionalOutput("scanner has no integers",
						// DEBUG_TEXT);
					}
					
				}
				//DebugSuite.exceptionHandledSleep(ArduinoServer.delay);
			}
			DebugSuite.conditionalOutput("Connection Failed", DEBUG_TEXT);
			clientScanner.close();
			clientSocket.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
