package sv.distributed3;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;
import sv.utils.Utils;

public class LiteClient {

	String hostName;
	int port;

	/**
	 * creates a client to connect to a specific hostName and port
	 * 
	 * @param hostName
	 *            the host/ip to connect to
	 * @param port
	 *            the port to connect to.
	 */
	public LiteClient(String hostName, int port) {
		super();
		this.hostName = hostName;
		this.port = port;
		init();
	}

	Socket s;
	DataOutputStream dos;
	private boolean autoFlush = false;

	/**
	 * connects to the hostname and port
	 * 
	 * @return null if failure, the socket if successful.
	 */
	public Socket connect() {
		s = null;
		try {
			s = new Socket(hostName, port);
			dos = Utils.deflatedDataOut(s.getOutputStream(), 8);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
		// TODO Auto-generated method stub

	}

	/**
	 * sending a single int
	 * 
	 * @param i
	 *            that int
	 */
	public void send(int i) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.INT_CONST);
				dos.writeInt(i);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * sends a single byte
	 * 
	 * @param b
	 *            the byte to send
	 */
	public void send(byte b) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.BYTE_CONST);
				dos.writeByte(b);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * sends a boolean value
	 * 
	 * @param bool
	 *            that boolean value
	 */
	public void send(boolean bool) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.BOOL_CONST);
				dos.writeBoolean(bool);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * Sends a string value
	 * 
	 * @param str
	 *            that string
	 */
	public void send(String str) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.STRING_CONST);
				dos.writeUTF(str);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * sends a single floating point number
	 * 
	 * @param f
	 *            the float to send
	 */
	public void send(float f) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.FLOAT_CONST);
				dos.writeFloat(f);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * sends a single double value
	 * 
	 * @param d
	 *            the double value
	 */
	public void send(double d) {
		if (s != null) {
			try {
				dos.writeByte(LiteServer.DOUBLE_CONST);
				dos.writeDouble(d);
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}

	}

	/**
	 * large transfer protocol and very large trasnfer protocol
	 * 
	 * @param list
	 */
	public void sendInts(ArrayList<Integer> list) {
		ltpSend(list, LiteServer.INT_CONST);
	}

	public void sendBytes(ArrayList<Byte> list) {
		ltpSend(list, LiteServer.BYTE_CONST);
	}

	public void sendStrings(ArrayList<String> list) {
		ltpSend(list, LiteServer.STRING_CONST);
	}

	public void sendBooleans(ArrayList<Boolean> list) {
		ltpSend(list, LiteServer.BOOL_CONST);
	}

	public void sendFloats(ArrayList<Float> list) {
		ltpSend(list, LiteServer.FLOAT_CONST);
	}

	public void sendDoubles(ArrayList<Double> list) {
		ltpSend(list, LiteServer.DOUBLE_CONST);
	}

	/**
	 * Large/Very Large transfer protocols
	 * 
	 * @param list
	 *            the arraylist to send
	 * @param type
	 *            the data type (look in LiteServer for data types)
	 */
	public void ltpSend(ArrayList<?> list, byte type) {
		int bytesSent = 0;
		if (s != null) {
			
			try {
				if (list.size() <= 127) {
					// large transfer
					dos.writeByte(LiteServer.LARGE_TRANSFER);
					dos.writeByte(type);
					dos.writeByte((byte) list.size());
					bytesSent += 3;
				} else {
					// very large transfer
					dos.writeByte(LiteServer.VERY_LARGE_TRANSFER);
					dos.writeByte(type);
					dos.writeInt(list.size());
					bytesSent += 6;
				}
				if (debug) {
					c.addLine("sending Arraylist of type: " + type
							+ " and size: " + list.size() + " to: "
							+ s.getInetAddress());
				}
				for (Object o : list) {

					if (debug) {
						// c.addLine(o + " type: " + type + " o.getClass(): " +
						// o.getClass());
					}
					// sends data based on the type specified
					switch (type) {
					case LiteServer.BOOL_CONST:
						dos.writeBoolean((boolean) o);
						bytesSent++;
						break;
					case LiteServer.BYTE_CONST:
						dos.writeByte((byte) o);
						bytesSent++;
						break;
					case LiteServer.DOUBLE_CONST:
						dos.writeDouble((double) o);
						bytesSent+=8;
						break;
					case LiteServer.FLOAT_CONST:
						dos.writeFloat((float) o);
						bytesSent+=4;
						break;
					case LiteServer.INT_CONST:
						dos.writeInt((int) o);
						bytesSent+=4;
						break;

					case LiteServer.STRING_CONST:
						dos.writeUTF((String) o);
						String s = (String) o;
						bytesSent+=2 * s.length();
						break;

					}
				}
				if (debug) {
					c.addLine("bytes sent: " + bytesSent);
				}
				if (autoFlush) {
					dos.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// if the socket is null
		}
	}

	/**
	 * function to call when finished sending, will not close the socket or
	 * dataOutputStream just calls flush on underlying data output stream
	 */
	public void flush() {
		try {
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * closes the dataOutputStream and socket calls flush first just in case.
	 */
	public void close() {
		flush();
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean autoFlush() {
		return autoFlush;
	}

	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

	public static boolean debug = DistributedGlobalVariables.debugClient;
	public LiteServerBuffer lsb = new LiteServerBuffer();
	static int guiID;
	static Console c = null;
	static Gui2 gui;

	private void init() {
		if (debug && c == null) {
			guiID = GuiManager.createGui(this.getClass() + " Console");
			c = new Console(20, 80, 20, 20);
			gui = GuiManager.getGui(guiID);
			gui.add(c);
		}
	}

	// ArrayList<Packager> packagers = new ArrayList<Packager>();

	public void sendObject(Sendable s) {
		// code to send a single object
		Packager pack = new Packager();
		pack.firstObject = true;
		// packagers.add(pack);
		s.send(this, pack);
		pack.firstObject = false;
		try {
			pack.send();
			if (autoFlush) {
				dos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendObjectList(ArrayList<Sendable> sArray) {
		Packager pack = new Packager();
		pack.firstObject = true;
		// packagers.add(pack);
		for (Sendable s : sArray) {
			s.send(this, pack);
			pack.firstObject = false;
		}
		try {
			pack.send();
			if (autoFlush) {
				dos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class Packager {
		ArrayList<Byte> types = new ArrayList<Byte>();
		// variable type format
		ArrayList<Object> items = new ArrayList<Object>();
		// the items to send, order is expected to follow types format
		// the other numbers will be derrived from that.
		boolean firstObject = true;

		public void send() throws IOException {
			int bytesSent = 0;
			int itemsToSend = items.size() / types.size();
			if (itemsToSend < 127 && types.size() < 127) {
				dos.writeByte(LiteServer.LARGE_OBJECT_TRANSFER);
				dos.writeByte((byte) types.size());
				dos.writeByte(itemsToSend);
				bytesSent += 3;
			} else {
				dos.writeByte(LiteServer.VERY_LARGE_OBJECT_TRANSFER);
				dos.writeInt(types.size());
				dos.writeInt(itemsToSend);
				bytesSent += 9;
			}
			// writing expected format
			for (Byte b : types) {
				dos.writeByte(b);
			}
			for (int i = 0; i < items.size(); i++) {
				Object o = items.get(i);
				int typeMod = i % types.size();
				byte currentType = types.get(typeMod);
				switch (currentType) {
				case LiteServer.BOOL_CONST:
					dos.writeBoolean((boolean) o);
					bytesSent ++;
					break;
				case LiteServer.BYTE_CONST:
					dos.writeByte((byte) o);
					bytesSent ++;
					break;
				case LiteServer.DOUBLE_CONST:
					dos.writeDouble((double) o);
					bytesSent +=8;
					break;
				case LiteServer.FLOAT_CONST:
					dos.writeFloat((float) o);
					bytesSent +=4;
					break;
				case LiteServer.INT_CONST:
					dos.writeInt((int) o);
					bytesSent +=4;
					break;
				case LiteServer.STRING_CONST:
					dos.writeUTF((String) o);
					String s = (String) o;
					bytesSent+=2 * s.length();
					break;

				}
			}
			if (debug) {
				c.addLine("sent : " + bytesSent + " bytes over LOTP" + " KB: " + bytesSent/1024);
			}
		}

		public void add(Boolean i) {
			if (firstObject) {
				types.add(LiteServer.BOOL_CONST);
			}
			items.add(i);
		}

		public void add(Integer i) {
			if (firstObject) {
				types.add(LiteServer.INT_CONST);
			}
			items.add(i);
		}

		public void add(Byte i) {
			if (firstObject) {
				types.add(LiteServer.BYTE_CONST);
			}
			items.add(i);
		}

		public void add(String i) {
			if (firstObject) {
				types.add(LiteServer.STRING_CONST);
			}
			items.add(i);
		}

		public void add(Float i) {
			if (firstObject) {
				types.add(LiteServer.FLOAT_CONST);
			}
			items.add(i);
		}

		public void add(Double i) {
			if (firstObject) {
				types.add(LiteServer.DOUBLE_CONST);
			}
			items.add(i);
		}
	}

}
