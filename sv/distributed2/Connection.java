package sv.distributed2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import sv.debugSuite.DebugSuite;

public class Connection implements Syncable, Runnable {
	private static final boolean outputFailure = false;
	private static final boolean outputAllSentConnections = true;
	private int port;
	private String hostName;
	private boolean working = false;
	private int clientID = Server2.getClientID();
	private boolean newConnection=true;

	public Connection(String inputData) {
		readData(inputData);
	}

	public Connection(String hostName, int port) {
		this.setHostName(hostName);
		this.setPort(port);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the working
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * @param working
	 *            the working to set
	 */
	public void setWorking(boolean working) {
		this.working = working;
	}

	@Override
	public void syncSend(DataOutputStream dos) {
		// TODO Auto-generated method stub
		DebugSuite.conditionalOutput("Sending Connection: " + hostName + " Port:" + port
				+ " Client ID: " + clientID,outputAllSentConnections);
		try {
			dos.writeUTF(hostName);
			dos.writeInt(port);
			dos.writeInt(clientID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void syncReceive(DataInputStream dis) {
		
		try {
			this.hostName = dis.readUTF();
			this.port = dis.readInt();
			this.clientID = dis.readInt();
			DebugSuite.output("Received Connection: " + hostName + " Port:"
					+ port + " Client ID: " + clientID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int getSyncID() {
		// TODO Auto-generated method stub
		return 9999;
	}

	/*
	 * Checks and updates the status of if something is working. returns true if
	 * it is working, false if it isn't automatically updates working.
	 */
	public boolean checkConnection(Connection whoIAm) {
		//DebugSuite.output("Checking Connection: " + this.toString());
		boolean connectionSuccess = false;
		String hostName = this.getHostName();
		int port = this.getPort();
		try {
			Socket sock = new Socket();
			
			sock.connect(new InetSocketAddress(hostName, port) , 200);
			//Server2.sendSyncableStatic(whoIAm, sock);
			connectionSuccess = true;
			sock.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		this.setWorking(connectionSuccess);
		DebugSuite.conditionalOutput("Connection working: " + connectionSuccess
				+ " " + this.toString(), connectionSuccess || outputFailure);
		return connectionSuccess;
	}

	public String toString() {
		String rtrn = "";
		rtrn += hostName + "," + port;

		return rtrn;

	}

	/*
	 * @param str This better be the correct data, heres the format
	 * hostName,port all extra data will be ignored after, data before is going
	 * to cause issues
	 */
	public void readData(String str) {
		Scanner parseScanner = new Scanner(str);
		parseScanner.useDelimiter(",");
		this.hostName = parseScanner.next();
		this.port = parseScanner.nextInt();
		parseScanner.close();
	}

	public Socket createSocket() {
		Socket sock = null;
		try {
			sock = new Socket(hostName, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sock;

	}

	public static int getIDStatic() {
		// TODO Auto-generated method stub
		return 9999;
	}

	public boolean equals(Connection c) {
		boolean rtrn = false;

		if (this.hostName.equals(c.getHostName())) {
			if (this.port == c.getPort()) {
				rtrn = true;
			}
		}

		return rtrn;
	}

	/*
	 * Checking connections as a thread
	 */
	boolean sentAllConnections = false;

	@Override
	public void run() {
		this.checkConnection(new Connection("localhost", 0));
		// TODO Auto-generated method stub
		if (this.isWorking() && sentAllConnections == false) {
			Server2.sendAllConnections(this);
			sentAllConnections = true;
		}
	}

  /**
   * @return the newConnection
   */
  public boolean isNewConnection() {
    return newConnection;
  }

  /**
   * @param newConnection the newConnection to set
   */
  public void setNewConnection(boolean newConnection) {
    this.newConnection = newConnection;
  }

}
