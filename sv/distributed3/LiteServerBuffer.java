package sv.distributed3;

import java.util.ArrayList;

import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;

public class LiteServerBuffer {
	/**
	 * this is a buffer for the light server it contains the buffer and the
	 * connection from which the information came.
	 */
	LiteConnection connection;

	public LiteServerBuffer(LiteConnection c) {
		this.connection = c;
		init();
		// TODO Auto-generated constructor stub
	}

	public LiteServerBuffer() {
		init();
		// TODO Auto-generated constructor stub
	}

	ArrayList<LiteBufferValue> bufferValues = new ArrayList<LiteBufferValue>();

	/**
	 * adds an object to the buffer
	 * 
	 * @param o
	 */
	public void add(LiteBufferValue o) {
		bufferValues.add(o);
	}

	/**
	 * FIFO method, returns buffer(0) object is removed on return.
	 * 
	 * @return buffer 0
	 */
	public LiteBufferValue getNext() {
		return bufferValues.remove(0);
	}
	
	
	/**
	 * LIFO method, returns the last item in the buffer object is removed on
	 * return. may be more efficient then getNext in terms of memory operations.
	 * 
	 * @return the last object in the buffer
	 */
	/*
	public Object pop() {
		return bufferValues.remove(bufferValues.size() - 1);
	}
	*/
	//need to redo this
	/**
	 * determines if there is a next value
	 * @return
	 */
	public Boolean hasNext() {
		Boolean val = (!bufferValues.isEmpty());
		if (debug) {
			c2.addLine("has next? " + val);
		}
		return val;
	}

	static boolean debug = DistributedGlobalVariables.debugServerBuffer;
	static Integer guiID = null;
	static Console c;
	static Console c2;
	static Gui2 gui;

	private void init() {
		debug = DistributedGlobalVariables.debugServerBuffer;
		if (debug) {
			if (guiID == null) {
			guiID = GuiManager.createGui(this.getClass() + "Console");
			c = new Console(20, 80, 20, 20);
			c2 = new Console(20,30,400,20);
			gui = GuiManager.getGui(guiID);
			gui.add(c);
			gui.add(c2);
			}
		}
	}
}
