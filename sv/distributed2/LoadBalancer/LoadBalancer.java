package sv.distributed2.LoadBalancer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import sv.distributed2.Server2;
import sv.distributed2.Syncable;

public class LoadBalancer implements Syncable {
	
	/*
	 * Load Balancer is essentially a general implementation of distributed2
	 * in that sense it contains functions to distribute arrays of Syncable items
	 * and keep track of performance.
	 */
	
	
	public LoadBalancer() {
		// TODO Auto-generated constructor stub
	}

	
	private static final int ID = 9998;
	@Override
	public int getSyncID() {
		// TODO Auto-generated method stub
		return ID;
	}
	
	public static int getSyncIDStatic() {
		// TODO Auto-generated method stub
		return ID;
	}
	
	/*******************************************************
	 * Helpful functions for sending data
	 */
	
	//ArrayList modifications
	/*
	 * Retruns the unused portion of the arrayList
	 */
	public static ArrayList<Syncable> sendPartialArrayList(ArrayList<Syncable> items, int start,
			int stop,Socket sendSock,boolean removeSentItems) {
		for (int i=stop;i>=start;i--) {
			Server2.sendSyncable(items.get(i), sendSock);
		}
		
		if (removeSentItems) {
			for (int i=stop;i>=start;i--) {
				items.remove(i);
			}
		}
		
		return items;
		
	}

	@Override
	public void syncSend(DataOutputStream dos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syncReceive(DataInputStream dis) {
		// TODO Auto-generated method stub
		
	}
	

}
