package sv.distributed3;

import java.util.ArrayList;

import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;

public class LiteBufferValue {
	/**
	 * A class for holding various network data
	 * supports the simple transfer protocol and large transfer
	 * protocol.
	 * For Object transfer protocol, use: LiteBufferObject
	 */
	public ArrayList<Object> o = new ArrayList<Object>();
	byte type;
	//simple transfer
	public LiteBufferValue(Object obj,byte type) {
		this.o = new ArrayList<Object>();
		this.o.add(obj);
		this.type = type;
		init();
	}
	//Large transfer
	public LiteBufferValue(ArrayList<Object> o, byte type) {
		this.o = o;
		this.type = type;
		init();
	}
	
	/**
	 * returns an error/null if the value is not the
	 * correct type
	 */
	public Byte getByte() {
		Byte b = null;
		if (type==LiteServer.BYTE_CONST) {
			b = (Byte) o.remove(0);
		}
		return b;
	}
	public Boolean getBoolean() {
		Boolean b = null;
		if (type==LiteServer.BOOL_CONST) {
			b = (Boolean) o.remove(0);
		}
		return b;
	}
	public Integer getInteger() {
		Integer b = null;
		if (type==LiteServer.INT_CONST) {
			b = (Integer) o.remove(0);
		}
		return b;
	}
	public String getString() {
		String b = null;
		if (type==LiteServer.STRING_CONST) {
			b = (String) o.remove(0);
		}
		return b;
	}
	public Float getFloat() {
		Float b = null;
		if (type==LiteServer.FLOAT_CONST) {
			b = (Float) o.remove(0);
		}
		return b;
	}
	public Double getDouble() {
		Double b = null;
		if (type==LiteServer.DOUBLE_CONST) {
			b = (Double) o.remove(0);
		}
		return b;
	}
	public Object getNext() {
		return o.remove(0);
	}
	
	public ArrayList<Object> getO() {
		return o;
	}
	public void setO(ArrayList<Object> o) {
		this.o = o;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public boolean hasMultiple() {
		Boolean multiple = (o.size()>1);
		if (debug) {
			c.addLine("multiple? " + multiple + " size: " + o.size());
		}
		return multiple;
	}	
	static boolean debug = DistributedGlobalVariables.debugBufferValue;
	static Integer guiID = null;
	static Console c = null;
	static Gui2 gui;

	private void init() {
		if (debug && c == null && guiID == null) {
			guiID = GuiManager.createGui(this.getClass() + " Console");
			c = new Console(20, 80, 20, 20);
			gui = GuiManager.getGui(guiID);
			gui.add(c);
		}
	}
}
