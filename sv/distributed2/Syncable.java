package sv.distributed2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/*
 * This is the Syncable interface. It contains the necessary functions
 * to enable syncing support
 * 
 */
import java.net.Socket;
public interface Syncable {
	/*
	 * A method that should send data
	 */
	//public String className;
	public void syncSend(DataOutputStream dos);
	/*
	 *  A method that should receive data
	 *  Right now they must be programmed separately
	 */
	public void syncReceive(DataInputStream dis);
	
	public int getSyncID();//This returns the ID, it must be hardcoded.
}
