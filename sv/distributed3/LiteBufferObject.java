package sv.distributed3;

import java.util.ArrayList;

public class LiteBufferObject {
	/*
	 * Class for supporting the Large Object Transfer protocol
	 */
	ArrayList<Byte> types = null;
	ArrayList<Object> items = new ArrayList<Object>();
	public LiteBufferObject(ArrayList<Byte> types) {
		this.types = types;
		items = new ArrayList<Object>();
	}
	public void add(Object o) {
		items.add(o);
		//System.out.println("loading in object: " + o);
	}
	public ArrayList<Object> getObjects() {
		// TODO Auto-generated method stub
		return items;
	}
	public Boolean hasNext() {
		return !items.isEmpty();
	}
	public Object getNext() {
		return items.remove(0);
	}

}
