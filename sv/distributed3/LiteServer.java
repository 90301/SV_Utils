package sv.distributed3;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.DeflaterInputStream;

import sv.debugSuite.DebugSuite;
import sv.distributed2.SyncThread;
import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;
import sv.utils.Utils;

public class LiteServer implements Runnable {
	/**
	 * 3rd generation of distributed computing this is a server that handles
	 * information less robustly all information sent to this server goes into a
	 * buffer based on where it came from (client,data)
	 * 
	 * This server has two methods for sending information back the first is a
	 * "broadcast" the other is a sendToConnection which utilizes either a
	 * LiteConnection or ipAddress
	 */

	/*
	 * Lite communication protocols:
	 * 
	 * simple, single values: data Type (byte), value
	 * 
	 * overhead: 1 byte per transfer.
	 * --------------------------------------------
	 * --------------------------------
	 * 
	 * large transfer: large trasnfer constant (byte), dataType(byte), # vals to
	 * transfer (byte) value,value... ect
	 * 
	 * overhead: 3 bytes per transfer
	 * --------------------------------------------
	 * -------------------------------- large object transfer: large object
	 * transfer constant (byte), number of values per object (byte) number of
	 * objects for format (byte) data type1, data type2..., value type1, value
	 * type2...
	 * 
	 * so to transfer 10 objects with 2 ints and 2 bool values each:
	 * 
	 * (constant) , 4 , 10 , (int const) , (int const) , (bool const) , (bool
	 * const) , int value, int value, bool value, bool value, int value, int
	 * value, bool value ...
	 * 
	 * overhead: 3 bytes + (number of data types) bytes
	 * 
	 * this will have it's own object to hold these values
	 * 
	 * -------------------------------------------------
	 * Very_Large_Object_Transfer_2 this is for objects that have lots of
	 * repetitive data or, objects that are large on their own, and are then
	 * potentially being sent in an array a good example of this would be
	 * sending a map or a world.
	 * 
	 * VLOT2 (byte) (number of objects (int)) (types per object(int)) type
	 * (byte) number of values (int) ... data
	 */
	public static final byte BOOL_CONST = 1;
	public static final byte BYTE_CONST = 2;
	public static final byte INT_CONST = 3;
	public static final byte STRING_CONST = 4;
	public static final byte FLOAT_CONST = 5;
	public static final byte DOUBLE_CONST = 6;

	public static final byte LARGE_TRANSFER = 16;
	public static final byte LARGE_OBJECT_TRANSFER = 17;
	public static final byte VERY_LARGE_TRANSFER = 18;
	public static final byte VERY_LARGE_OBJECT_TRANSFER = 19;

	int port;
	private boolean endServer = false;
	private boolean ready = false;
	private static boolean debug = DistributedGlobalVariables.debugServer;

	public ArrayList<LiteConnection> connections = new ArrayList<LiteConnection>();
	public ArrayList<LiteBufferObject> objectBuffer = new ArrayList<LiteBufferObject>();
	public LiteServerBuffer bufferValues = new LiteServerBuffer();
	public boolean singleBuffer = true;// one buffer(true), or one buffer per
										// connection(false)

	// ArrayList<LiteBufferValue> bufferValues = new
	// ArrayList<LiteBufferValue>();

	public LiteServer(int port) {
		this.port = port;
		init();
	}

	/**
	 * creates a thread to start the server.
	 */
	public void startServer() {
		if (debug) {
			c.addLine("Creating Server");
		}
		Thread svThread = new Thread(this);
		svThread.start();
		if (debug) {
			c.addLine("Server Started Success");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int PortNumber = this.port;
		ServerSocket serverSock;
		try {
			serverSock = new ServerSocket(PortNumber);

			while (endServer != true) {
				// accept connections and pass to client threads

				Socket clientSocket = serverSock.accept();
				LiteReceiveThread receiveThread = new LiteReceiveThread(
						clientSocket);
				Thread runableReceiveThread = new Thread(receiveThread);
				runableReceiveThread.start();

			}
			serverSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LiteConnection findByInet(InetAddress i) {
		LiteConnection c = connections.get(connections.indexOf(i));

		return c;

	}

	public class LiteReceiveThread implements Runnable {
		/*
		 * Internal class to handle receiving data for the server.
		 */
		Socket clientSocket;

		/**
		 * 
		 * @param clientSocket
		 */
		public LiteReceiveThread(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			// clientSocket.getInetAddress();
			setReady(false);
			DataInputStream dis = null;
			try {
				dis = Utils.deflatedDataIn(clientSocket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (singleBuffer) {
				try {
					byte arg1 = dis.readByte();

					if (debug) {
						c.addLine("received input: " + arg1);
					}
					if (arg1 < 16) {
						// read value, simple transfer protocol
						readValue(dis, arg1);
					} else {
						if (arg1 == LARGE_TRANSFER
								|| arg1 == VERY_LARGE_TRANSFER) {
							largeTransfer(dis, arg1);
						}
						if (arg1 == LARGE_OBJECT_TRANSFER
								|| arg1 == VERY_LARGE_OBJECT_TRANSFER) {
							// large object transfer protocol
							largeObjectTransfer(dis, arg1);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			setReady(true);
		}

		private void largeObjectTransfer(DataInputStream dis, byte arg1)
				throws IOException {
			
			int typeSize = -1;
			ArrayList<Byte> types = new ArrayList<Byte>();
			int objects = -1;
			// int expectedValues = -1;

			if (arg1 == LARGE_OBJECT_TRANSFER) {
				// large (uses bytes)
				typeSize = dis.readByte();
				objects = dis.readByte();

			} else {
				// very large (uses integers)
				typeSize = dis.readInt();
				objects = dis.readInt();
			}
			if (debug) {
				c.addLine("starting large object transfer. size: " + objects + " typeSize: " + typeSize );
			}
			for (int i = 0; i < typeSize; i++) {
				types.add(dis.readByte());
			}
			for (int i = 0; i < objects; i++) {
				LiteBufferObject lbo = new LiteBufferObject(types);
				for (Byte type : types) {

					Object value = null;

					switch (type) {
					case BOOL_CONST:
						value = dis.readBoolean();
						lbo.add(value);
						break;
					case BYTE_CONST:
						value = dis.readByte();
						lbo.add(value);
						break;
					case DOUBLE_CONST:
						value = dis.readDouble();
						lbo.add(value);
						break;
					case FLOAT_CONST:
						value = dis.readFloat();
						lbo.add(value);
						break;
					case INT_CONST:
						value = dis.readInt();
						lbo.add(value);
						break;
					case STRING_CONST:
						value = dis.readUTF();
						lbo.add(value);
						break;
					}
				}
				objectBuffer.add(lbo);
				
			}

		}

		private void largeTransfer(DataInputStream dis, byte arg1)
				throws IOException {
			Integer size = null;
			byte type = -1;

			type = dis.readByte();

			if (arg1 == LARGE_TRANSFER) {
				byte sizeByte = dis.readByte();
				if (debug) {
					c.addLine("size: " + sizeByte);
				}
				size = (int) sizeByte;
			} else if (arg1 == VERY_LARGE_TRANSFER) {
				size = dis.readInt();
			}
			if (debug) {
				c.addLine("receiving input, size: " + size + " type: " + type
						+ " arg1: " + arg1);
			}
			ArrayList<Object> list = new ArrayList<Object>();
			for (int i = 0; i < size; i++) {
				Object value = null;
				switch (type) {
				case BOOL_CONST:
					value = dis.readBoolean();
					list.add(value);
					break;
				case BYTE_CONST:
					value = dis.readByte();
					list.add(value);
					break;
				case DOUBLE_CONST:
					value = dis.readDouble();
					list.add(value);
					break;
				case FLOAT_CONST:
					value = dis.readFloat();
					list.add(value);
					break;
				case INT_CONST:
					value = dis.readInt();
					list.add(value);
					break;
				case STRING_CONST:
					value = dis.readUTF();
					list.add(value);
					break;
				}
				if (debug) {
					c.addLine("value read: " + value);
				}
			}
			LiteBufferValue lbv = new LiteBufferValue(list, type);
			bufferValues.add(lbv);

		}

		private void readValue(DataInputStream dis, byte arg1) {
			// the simple transfer
			// the first code has already been read and is arg1
			Object value = null;
			try {
				switch (arg1) {
				case BOOL_CONST:
					value = dis.readBoolean();
					break;
				case BYTE_CONST:
					value = dis.readByte();
					break;
				case DOUBLE_CONST:
					value = dis.readDouble();
					break;
				case FLOAT_CONST:
					value = dis.readFloat();
					break;
				case INT_CONST:
					value = dis.readInt();
					break;
				case STRING_CONST:
					value = dis.readUTF();
					break;
				}
			} catch (IOException e) {
				if (debug) {
					c.addLine(e.getMessage());
				}
			}
			if (singleBuffer) {
				if (debug) {
					c.addLine(value.toString() + " arg1 " + arg1);
				}
				LiteBufferValue l = new LiteBufferValue(value, arg1);
				// bufferValues.add(l);
				bufferValues.add(l);
			}

		}

	}

	// static ArrayList<Class<?>> types = new ArrayList<>();


	static int guiID;
	static Console c;
	static Gui2 gui;

	private void init() {
		if (debug && c == null) {
			guiID = GuiManager.createGui(this.getClass() + " Console");
			c = new Console(20, 80, 20, 20);
			gui = GuiManager.getGui(guiID);
			gui.add(c);
		}
	}

	public boolean hasNextObject() {
		// TODO Auto-generated method stub
		return (!objectBuffer.isEmpty());
	}

	public LiteBufferObject getNextObject() {
		return objectBuffer.remove(0);
		
	}

	public boolean hasNextValue() {
		// TODO Auto-generated method stub
		return bufferValues.hasNext();
	}

	public LiteBufferValue getNextValue() {
		// TODO Auto-generated method stub
		return bufferValues.getNext();
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}
