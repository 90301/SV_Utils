package sv.distributed3;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;

public class LiteConnection {
	/**
	 * 3rd generation connection this contains the information related to the
	 * connection specifically the ip address and port it also has methods to
	 * create a socket (intended for use in clients, not servers).
	 */
	String ip;
	InetAddress inet;

	public LiteConnection(String ip) {
		this.ip = ip;
		try {
			this.inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			if (debug) {
				c.addLine("failed to find host: " + ip);
			}
		}
		init();
	}

	public LiteConnection(InetAddress inet) {
		this.inet = inet;
		this.ip = inet.getHostAddress();
		init();
	}

	/*
	 * GUI code designed for copy paste
	 */
	static boolean debug = DistributedGlobalVariables.debugConnection;
	static int guiID;
	static Console c;
	static Gui2 gui;

	private void init() {
		if (debug) {
			guiID = GuiManager.createGui("liteConnection Console");
			c = new Console(20, 80, 20, 20);
			gui = GuiManager.getGui(guiID);
			gui.add(c);
		}
	}

	/**
	 * gets information from a socket
	 * 
	 * @param s
	 *            the socket to pull information from
	 */
	public void getConnection(Socket s) {
		ip = s.getInetAddress().getHostName();
		if (debug) {
			c.addLine(this.toString());

		}

	}

	public Socket createSocket(int port) {
		try {
			Socket sock = new Socket(this.ip, port);
			return sock;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String toString() {
		String str = "";
		str += this.ip + " inet: " + this.inet;
		return str;
	}

	public boolean equals(InetAddress i) {
		boolean rtrn = (i.getHostAddress().equals(this.ip))
				|| (i.getHostName().equals(this.ip));
		if (debug) {
			c.addLine(i.getHostName() + " / " + i.getHostAddress() + " == "
					+ this.ip + " : " + rtrn);
		}
		return rtrn;

	}

	// getters and setters
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public InetAddress getInet() {
		return inet;
	}

	public void setInet(InetAddress inet) {
		this.inet = inet;
	}
}
