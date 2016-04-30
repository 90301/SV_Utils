/**
 * 
 */
package sv.distributed2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sv.debugSuite.DebugSuite;
import sv.distributed2.SyncThread;
import sv.distributed2.Syncable;

/**
 * @author inhaler
 * 
 */
public class Server2 implements Runnable {

	/**
	 * This is the gen2 Server. This combines everything in project specific
	 * Server and ConnectionManager
	 */

	private boolean endServer = false;
	private int serverPort = 9999;// Default
	private static final boolean showFailedConnections = false;
	private static int clientID = 0;
	private static final boolean debugGui = true;

	public static int debugGuiID;
	private static Map<Integer, Syncable> syncableList = new HashMap<Integer, Syncable>();
	private static ArrayList<Syncable> syncableBuffer = new ArrayList<Syncable>();
	
	
	/*
	 * 1-100 are reserved for project specific uses
	 */
	public static final int WAIT = 101;
	//indicates the sender is not ready to continue
	public static final int READY = 102;
	//indicates the sender is ready to continue execution
	public static final int mpiCode = 12000;
	
	public static ArrayList<Integer> messages = new ArrayList<Integer>();

	public Server2() {
		// TODO Auto-generated constructor stub
		initialize();

	}

	public Server2(int serverPort) {
		// TODO Auto-generated constructor stub
		this.serverPort = serverPort;
		initialize();
	}

	public void initialize() {
		Server2.addSyncable(new Connection("DummyHostName", -1));
		if (debugGui) {
			debugGuiID = DebugSuite.createDebugGui("Server2");
		}
	}

	/*
	 * Sets the server port. NOTE: this must be called before the server is
	 * started.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	// stops the server after the next connection. Probably not the best thing
	// to call.
	public void stopServer() {
		endServer = true;
	}

	/*
	 * This is the server thread, it handles connections, creates client threads
	 * to accept the connections and send data.
	 */

	public void run() {
		// TODO Auto-generated method stub
		int PortNumber = this.serverPort;
		ServerSocket serverSock;
		try {
			serverSock = new ServerSocket(PortNumber);

			while (endServer != true) {
				// accept connections and pass to client threads
				
				Socket clientSocket = serverSock.accept();
				SyncThread receiveThread = new SyncThread(clientSocket, false);
				receiveThread.setPureData(this.pureData);
				Thread runableReceiveThread = new Thread(receiveThread);
				runableReceiveThread.start();
				
			}
			serverSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Project Specific
	 */


	public static void addSyncable(Syncable insertionSyncable) {
		int classID = insertionSyncable.getSyncID();
		syncableList.put(classID, insertionSyncable);
	}

	/*
	 * Syncable buffer functions. add, remove, clear and get
	 */
	public static ArrayList<Syncable> getSyncableBuffer() {
		return syncableBuffer;
	}

	public static Syncable createClassFromID(int ID) {
		System.out.println("Creating class with ID: " + ID);
		Syncable item = syncableList.get(ID);
		System.out.println("created class: " + item);
		return item;
	}

	public static void addToSyncableBuffer(Syncable syncableItem) {
		syncableBuffer.add(syncableItem);
	}

	public static void clearSyncableBuffer() {
		System.out
				.println("Don't freakin call clear SyncableBuffer, it isn't thread friendly.");
		syncableBuffer.clear();
	}

	public static void removeFromSyncableBuffer(Syncable syncableItem) {
		syncableBuffer.remove(syncableItem);
	}

	// end of syncable buffer functions
	public static void sendSyncable(Syncable sendItem, Socket sendSock) {
		// TODO Auto-generated method stub
		try {
			// Socket sendSock = new Socket(HostName,Port);
			OutputStream os = sendSock.getOutputStream();
			int ID = sendItem.getSyncID();// Sending of ID
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(ID);
			sendItem.syncSend(dos);
			// sendSock.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Sends data to all working outgoing connections.
	 */
	public static void sendToAll(Syncable sendItem) {
	  for (int i=0;i<outgoingConnections.size();i++) {
	    Connection c = outgoingConnections.get(i);
	    if (c.isWorking()) {
	      Socket sendSock = c.createSocket();
	      sendSyncable(sendItem,sendSock);
	    }
	  }
	}

	/*
	 * Connection manager code. Deals with connections to other computers.
	 */
	private static ArrayList<Connection> outgoingConnections = new ArrayList<Connection>();
	// connections that are the hostnames and port of the machine this is
	// running on
	private static ArrayList<Connection> myConnections = new ArrayList<Connection>();

	/*
	 * check to make sure something is in outgoingConnections
	 */
	public static boolean isAddedStatic(Connection receivedConnection) {
		// TODO Auto-generated method stub
		boolean rtrn = outgoingConnections.contains(receivedConnection);
		if (myConnections.contains(receivedConnection)) {
			rtrn = true;
		}
		for (int i=0;i<myConnections.size();i++) {
			if(myConnections.get(i).equals(receivedConnection)) {
				rtrn = true;
				String strOut = ""+ receivedConnection.getHostName()
						+ " = " + myConnections.get(i).getHostName();
				DebugSuite.addGuiConsoleOutput(strOut, debugGuiID);
			}
		}
		return rtrn;
	}

	public static void addConnectionStatic(Connection receivedConnection) {
		// TODO Auto-generated method stub
		if (isAddedStatic(receivedConnection)) {
			String strOut = "Did not add: "+ receivedConnection.getHostName()
					+ " to outGoingConnections";
			DebugSuite.addGuiConsoleOutput(strOut, debugGuiID);
		} else {
			Server2.outgoingConnections.add(receivedConnection);
			String strOut = "Added: "+ receivedConnection.getHostName() + " to outGoingConnections";
			DebugSuite.addGuiConsoleOutput(strOut, debugGuiID);
		}

	}

	public static boolean isWorking(Connection receivedConnection) {
		// TODO Auto-generated method stub
		boolean rtrn;
		rtrn = receivedConnection.isWorking();
		return rtrn;
	}

	public static void checkAllConnections() {

		multiThreadCheckAllConnections();

	}

	public static void multiThreadCheckAllConnections() {
		int threads = outgoingConnections.size();
		if (threads > 255) {
			threads = 255;
		}
		ExecutorService executor = Executors.newCachedThreadPool();
		ArrayList<Future<?>> futures = new ArrayList<Future<?>>();
		for (int i = 0; i < outgoingConnections.size(); i++) {
			futures.add(executor.submit(new Thread(outgoingConnections.get(i))));
		}
		for (int i = 0; i < futures.size(); i++) {
			try {
				futures.get(i).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * if it doesn't have the working flag set to true, remove it.
	 */
	public static void purgeNonWorkingConnections() {

		// Iterator<Connection> it = outgoingConnections.iterator();
		// while (it.hasNext()) {
		// Connection c = it.next();
		int connectionsRemoved = 0;
		for (int i = outgoingConnections.size() - 1; i >= 0; i--) {
			Connection c = outgoingConnections.get(i);

			if (c.isWorking() != true) {
				connectionsRemoved++;
				DebugSuite.conditionalOutput(
						"Removing Connection: " + c.toString(),
						showFailedConnections);
				outgoingConnections.remove(i);
			}
		}
		DebugSuite.output("Removed: " + connectionsRemoved + " Connections.");
	}

	/*
	 * Sends only working connections.
	 */
	public static void sendAllConnections(Connection destination) {
		// TODO Auto-generated method stub
		Socket sendSock = destination.createSocket();
		for (int i = 0; i < outgoingConnections.size(); i++) {
			Connection sendItem = outgoingConnections.get(i);
			if (sendItem.isWorking()) {
				sendSyncable(sendItem, sendSock);
			}
		}
		for (int i = 0; i < myConnections.size(); i++) {
			Connection c = myConnections.get(i);
			sendSyncable(c, sendSock);
		}
	}

	public void updateMyConnections() {
		// Change this later to be better.
		// TODO stop hardcoding this

		// This gets all the ipaddresses for the computer and adds them to
		// myConnections
		Enumeration e;
		try {
			e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					DebugSuite.addGuiConsoleOutput(i.getHostAddress(),
							Server2.debugGuiID);
					myConnections.add(new Connection(i.getHostAddress(),
							this.serverPort));
				}
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (myConnections.size() == 0) {
			myConnections.add(new Connection("localhost", this.serverPort));
		}
	}

	public static int getClientID() {
		// TODO Make this make a unique ID
		return clientID;
	}

	public static String createConnectionFile(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			System.out.println("Writing Connection file: " + fileName);

			DataOutputStream dos = new DataOutputStream(fos);
			String rtrn = "";
			for (int i = 0; i < outgoingConnections.size(); i++) {
				Connection c = outgoingConnections.get(i);
				rtrn += c.toString();
				rtrn += "\n";
			}
			dos.writeUTF(rtrn);
			dos.close();
			return rtrn;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * adds connections to the outgoing connections based on input from a file.
	 */
	public static void readConnectionFile(String fileName) {
		String data = "";
		try {
			FileInputStream fis = new FileInputStream(fileName);
			System.out.println("Reading file: " + fileName);
			DataInputStream dis = new DataInputStream(fis);
			data = dis.readUTF();
			dis.close();
			Scanner lineParser = new Scanner(data);
			while (lineParser.hasNext()) {
				Connection c = new Connection(lineParser.nextLine());
				if (isAddedStatic(c) != true) {
					// if it isn't already added, add it.
					Server2.addConnectionStatic(c);
				}
			}
			lineParser.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * adds a range of IPs with a specific port format of base "192.168.1."
	 * <---- see that dot. it's important
	 * 
	 * @param base first 3 numbers of an IP followed by a dot EX: "192.168.1."
	 */
	public static void addConnectionRange(String base, int start, int stop,
			int port) {
		for (int i = start; i < stop; i++) {

			Connection c = new Connection(base + i, port);
			if (Server2.isAddedStatic(c) != true) {
				DebugSuite.output("Adding connection: " + base + i);
				Server2.addConnectionStatic(c);
			}
		}
	}

	/*
	 * A very rudementary way of getting sockets. It needs to check the
	 * connection first just to be safe, then passes the socket to be used
	 * elsewhere.
	 */
	private static int indexInOutgoingConnections = 0;

	public static Socket getNextWorkingSocket() {

		boolean connectionWorks = false;
		Connection c = null;
		int localAttempts = 0;
		while (connectionWorks != true) {
			indexInOutgoingConnections++;
			localAttempts++;
			if (indexInOutgoingConnections >= outgoingConnections.size()) {
				indexInOutgoingConnections = 0;
			}

			c = outgoingConnections.get(indexInOutgoingConnections);
			connectionWorks = c.checkConnection(outgoingConnections.get(0));

			if (localAttempts > outgoingConnections.size()) {
				System.out.println("No working connections found!");
			}

		}
		System.out.println("Found working connection at: " + c.toString());
		Socket sock = c.createSocket();

		return sock;

	}

	/*
	 * Returns true if there are items left in the syncable buffer
	 */
	public static boolean hasNextSyncableBuffer() {
		boolean rtrn = (syncableBuffer.size() >= 1);

		return rtrn;

	}

	/*
	 * This gets the next item in the syncable buffer, Deletes the item from the
	 * container <----IMPORTANT! and returns the item.
	 */
	public static Syncable getNextSyncableBuffer() {

		if (hasNextSyncableBuffer()) {
			return syncableBuffer.remove(syncableBuffer.size() - 1);
		} else {
			DebugSuite
					.output("ERROR: getNextSyncableBuffer was called when syncableBuffer had a size of: "
							+ syncableBuffer.size());
			return null;
		}
	}

	public void startServer() {
		DebugSuite.output("Creating Server");
		Thread svThread = new Thread(this);
		svThread.start();
		DebugSuite.output("Server Started Success");
	}

	public static int getTotalConnections() {
		// TODO Auto-generated method stub
		return outgoingConnections.size();
	}
	
	public static boolean hasNewConnection() {
	  boolean rtrn = false;
	  Iterator<Connection> it = outgoingConnections.iterator();
	  while (it.hasNext()) {
	    Connection c = it.next();
	    if (c.isNewConnection()) {
	      rtrn = true;
	    }
	  }
	  DebugSuite.addGuiConsoleOutput("new connections? : " + rtrn, debugGuiID);
	  return rtrn;
	}
	/*
	 * this is intended to be used for the initial sync.
	 */
	public static Connection getNewConnection() {
	  
	  Iterator<Connection> it = outgoingConnections.iterator();
    while (it.hasNext()) {
      Connection c = it.next();
      if (c.isNewConnection()) {
        return c;
      }
    }
	  
	  
    return null;
	  
	}
	
	public static void setNewConnection(Connection c,boolean value) {
	  Iterator<Connection> it = outgoingConnections.iterator();
    while (it.hasNext()) {
      Connection c2 = it.next();
      if (c.equals(c2)) {
        c2.setNewConnection(value);
      }
    }
	}
	
	
	public static void debugOutput() {
		int line = 0;
		DebugSuite.guiOutput("My Connections", debugGuiID, line);
		line++;
		line = DebugSuite.guiOutput(myConnections, debugGuiID, line);
		DebugSuite.guiOutput("OutGoing Connections", debugGuiID, line);
		line++;
		line = DebugSuite.guiOutput(outgoingConnections, debugGuiID, line);
	}
	
	/*
	 * File IO
	 */
	public static void saveSyncable(Syncable item, String path) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(item.getSyncID());
			item.syncSend(dos);
			dos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 * loading from file, currently only supports 1 object per file.
	 */
	public static void loadSyncable(Syncable item, String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			DataInputStream dis = new DataInputStream(fis);
			int id;
			try {
				id = dis.readInt();
				Syncable inputItem = Server2.createClassFromID(id);
				inputItem.syncReceive(dis);
				dis.close();
				Server2.addToSyncableBuffer(inputItem);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	/*
	 * this is a setting that only needs to be set once befor starting a server, currently no way to undo it
	 * this makes the program setup to handle data from an arduino a little better
	 * might refactor it to setArduinoData
	 */
	private boolean pureData = false;
	public void setPureData() {
		pureData=true;
		// TODO Auto-generated method stub
		
	}

	public static void addMessage(int i) {
		Server2.messages.add(i);
		// TODO Auto-generated method stub
	}
	public static boolean hasMessage() {
		return (!messages.isEmpty());
	}
	public static int getMessage() {
		int rtrn = -1;
		if (hasMessage()) {
			rtrn = messages.get(0);
			messages.remove(0);
		}
		return rtrn;
	}
	/*
	 * sends a msg using the specified socket
	 */
	public static void sendMessage(int msg,Socket sock) {
		try {
			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
			dos.writeInt(mpiCode);
			dos.writeInt(msg);
			dos.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (msg==READY) {
			ready = true;
		}
	}
	/*
	 * checks to make sure that all clients (and this client)
	 * have sent the ready code
	 */
	static int clients = 0;
	static int clientsReady = 0;
	static boolean ready = false;
	public static boolean readyToContinue() {
		boolean rtrn = false;
		if (ready&&clients<=clientsReady) {
			rtrn = true;
		}
		clientsReady = 0;
		return rtrn;
	}

	public static void addReadyNode() {
		// TODO Auto-generated method stub
		clientsReady++;
	}

}
