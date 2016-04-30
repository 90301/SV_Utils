/**
 * 
 */
package sv.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author NtD5, tbsw
 *
 */
public class NDArray<E> {
	
	private final int dimensions;
	private int[] size;
	private ArrayList<E> arrayList = new ArrayList<E>(); 
	

	/**
	 * 
	 */
	public NDArray(int... size) {
		dimensions = size.length + 1;
		this.size = size;
	}
	
	public int getDimensions() {
		return dimensions;
	}
	
	public int[] getSize() {
		return size;
	}
	
	public int getLength() {
		return arrayList.size();
	}
	
	public E get(int index) {
		return arrayList.get(index);
	}
	
	public E get(int... coords) {
		return get(getIndex(coords));
	}
	
	public E get(List<Integer> coords) {
		return get(listToArray(coords));
	}
	
	public void add(E element) {
		arrayList.add(element);
	}
	
	public E removeLast() {
		return arrayList.remove(getLength() - 1);
	}
	
	public void set(E element, int index) {
		arrayList.set(index, element);
	}
	
	public void set(E element, int... coords) {
		arrayList.set(getIndex(coords), element);
	}
	
	public void set(E element, List<Integer> coords) {
		set(element, listToArray(coords));
	}
	
	public int getIndex(int... coords) {
		if (coords.length != dimensions) {
			throw new IllegalArgumentException("Invalid number of coords: " + coords.length);
		}
		int k = 1;
		int t = coords[0];
		for (int i = 1; i < coords.length; i++) {
			k *= size[i-1];
			t += k*coords[i];
		}
		return t;
	}
	
	public int getIndex(List<Integer> coords) {
		return getIndex(listToArray(coords));
	}
	
	public int[] getCoords(int index) {
		int[] r = new int[dimensions];
		int d = 1;
		for (int i = 0; i < size.length; i++) {
			d *= size[i];
		}
		int j = index;
		for (int i = r.length - 1; i >= 0; i--) {
			r[i] = j / d; //floor
			j -= r[i] * d;
			if (i==0) break;
			d /= size[i-1];
		}
		return r;
	}
	
	public static int[] listToArray(List<Integer> list) {
		int i = 0;
		int[] r = new int[list.size()];
		for (Integer e : list) {
			r[i++] = e;
		}
		return r;
	}
	
	public static ArrayList<Integer> arrayToList(int[] array) {
		ArrayList<Integer> list = new ArrayList<Integer>(array.length);
		for (int e : array) {
			list.add(e);
		}
		return list;
	}

}
