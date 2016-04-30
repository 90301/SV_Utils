package sv.distributed2;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import sv.debugSuite.DebugSuite;
import sv.distributed2.LoadBalancer.LoadBalancer;

public class SyncThread implements Runnable {
	/*
	 * Sync Thread Should be the thread to be called. This should sort of handle
	 * the transferring of data.
	 */
	/*
	 * This contains all of the specific to distributed2 syncable classes
	 * syncing.
	 */
	String hostName;
	int portNumber;
	Syncable rtrn;
	Socket sock;
	Boolean send;

	public SyncThread(Socket sock, boolean send) {
		// default values
		hostName = "localhost";
		portNumber = 9999;
		this.sock = sock;
		this.send = send;
	}

	public void run() {

		receiveSyncable();

	}

	private static final int connectionNumber = Connection.getIDStatic();
	private static final boolean debugInfo = true;

	private void receiveSyncable() {
		System.out.println("ReceiveSycable");
		try {

			InputStream is = this.sock.getInputStream();
			DataInputStream dis = new DataInputStream(is);

			if (pureData) {
				//ardunio or pure data reading
				this.pureData(dis);
			} else {
				//regular distributed reading.
				int ID = dis.readInt();
				DebugSuite.conditionalOutput(
						"Got ID: " + ID + " From: " + sock.getInetAddress(),
						debugInfo);
				// TODO finish this by calling a method in project specific
				Syncable item = Server2.createClassFromID(ID);

				item.syncReceive(dis);
				System.out.println("Got item: " + item);
				if (specialCases(ID, item)) {
					// do nothing if it's a special case
					// this is currently handed by the special cases method
				} else {
					// if it's not a special case, throw it in the buffer.
					Server2.addToSyncableBuffer(item);
				}
				System.out.println("added to syncableBuffer: "
						+ Server2.getSyncableBuffer().size());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Checks special cases for received data, things like sending Connections
	 * or anything that shouldn't be in syncable buffer.
	 */
	private static final int loadBalancerNumber = LoadBalancer
			.getSyncIDStatic();

	private boolean specialCases(int ID, Syncable item) {
		boolean rtrn = false;

		// Dealing with connections
		if (ID == connectionNumber) {
			rtrn = true;
			System.out.println("Got here!");
			Connection receivedConnection = (Connection) item;
			if (Server2.isAddedStatic(receivedConnection) != true) {
				// add the connection if it isn't already in the array
				Server2.addConnectionStatic(receivedConnection);
			} else {
				// if the connection is added, but does not have "working" set
				// to true, attempt to make the connection
				if (Server2.isWorking(receivedConnection)) {
					// Do nothing
				} else {
					// Re-evaluate all connections.
					Server2.checkAllConnections();
				}
			}
		}
		// loadbalencer
		if (ID == loadBalancerNumber) {

		}
		if(ID==Server2.mpiCode) {
			rtrn = true;
			
			try {
				DataInputStream dis = new DataInputStream(this.sock.getInputStream());
				int i = dis.readInt();
				Server2.addMessage(i);
				dis.close();
				if (i==Server2.READY) {
					Server2.addReadyNode();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return rtrn;
	}

	private void pureData(DataInputStream dis) {
		try {
			while (dis.available()>0) {
				DebugSuite.output(dis.readUTF());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean pureData = false;

	/*
	 * set this to handle arguino data
	 */
	public void setPureData(boolean pureData) {
		this.pureData = pureData;
		// TODO Auto-generated method stub

	}

}
