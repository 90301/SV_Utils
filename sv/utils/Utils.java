package sv.utils;


import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import sv.debugSuite.DebugSuite;
import sv.gui2.Gui2;
import sv.gui2.GuiManager;
import sv.gui2.drawables2.Console;


public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}

	public static String toString(int[] array) {
		String str = "";
		for (Object i : array) {
			str += i + " , ";
		}

		return str;
	}
	public static String toString(float[] array) {
		String str = "";
		for (Object i : array) {
			str += i + " , ";
		}

		return str;
	}

	/*
	 * this checks to make sure the valueToTest is within (+/-) the tolerance of
	 * the center value so if the center value was 1000 and tolerance was 100,
	 * if the value to test is within 900 to 1100 this would return true
	 */
	public static boolean checkWithinTolerance(double valueToTest,
			double centerValue, double tolerance) {
		boolean rtrn = false;
		if (valueToTest >= centerValue - tolerance
				&& valueToTest <= centerValue + tolerance) {
			rtrn = true;
		}
		return rtrn;
	}

	/*
	 * Fairly simple scanner code, just figured it would be faster
	 */

	public static Scanner cinScanner;

	public static Scanner scannerSetup(String fileName) {
		Scanner finScanner = new Scanner(fileName);
		return finScanner;
	}

	/*
	 * creates a console reading scanner uses System.in
	 */
	public static Scanner consoleScannerSetup() {
		if (cinScanner == null) {
			cinScanner = new Scanner(System.in);
		}
		return cinScanner;
	}

	final static JFileChooser fc = new JFileChooser();
	private static boolean debug = false;

	/*
	 * DIALOG BOXES!!!!
	 */
	/**
	 * creates a scanner for reading data in from a dialog box where the user
	 * picks a file.
	 * 
	 * @return the Scanner from the dialogScanner
	 */
	public static Scanner dialogScanner() {
		Component parent = null;
		Scanner finScanner = null;
		int i = fc.showOpenDialog(parent);
		if (JFileChooser.APPROVE_OPTION == i) {
			File file = fc.getSelectedFile();

			try {
				System.out.println("Loading file: " + file.getPath());
				finScanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		return finScanner;
	}

	/**
	 * displays an output dialog box, returns null if
	 * user does not approve, else returns a compressed
	 * data output stream
	 * @return
	 */
	public static DataOutputStream dialogOutput() {
		Component parent = null;
		DataOutputStream dos = null;
		int i = fc.showSaveDialog(parent);
		if (JFileChooser.APPROVE_OPTION == i) {
		try {
			dos = deflatedDataOut(new FileOutputStream(fc.getSelectedFile()), 2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return dos;
	}

	/**
	 * Allows appending
	 * returns null if the user doesn't select approve
	 * @param append
	 *            if the file should be appended
	 * @return the dataOutputStream
	 */
	public static DataOutputStream dialogOutput(boolean append) {
		Component parent = null;
		DataOutputStream dos = null;
		int i = fc.showSaveDialog(parent);
		if (JFileChooser.APPROVE_OPTION == i) {
			try {
				FileOutputStream fos = new FileOutputStream(
						fc.getSelectedFile(), append);
				dos = deflatedDataOut(fos, 2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dos;
	}

	/**
	 * A yes/no dialog box that only returns true if yes is clicked
	 * 
	 * @param text
	 *            the text to show for the question
	 * @return if the user clicked yes
	 */
	public static boolean tfDialog(String text) {
		boolean rtrn = false;

		int dialogButton = JOptionPane.YES_NO_OPTION;
		int output = JOptionPane.showConfirmDialog(null, text, "Warning",
				dialogButton);

		// Object value = optionPane.getValue();
		// c.addLine(value.toString());
		// if (value==JOptionPane.YES_OPTION) {
		if (output == JOptionPane.YES_OPTION) {
			rtrn = true;
		}
		// }
		return rtrn;

	}

	/*
	 * Deflated and inflated output stream
	 */
	public static DataOutputStream deflatedDataOut(OutputStream underlyingOut,
			int deflateLevel) {
		Deflater d = new Deflater();
		d.setLevel(deflateLevel);
		DeflaterOutputStream deflaterOut = new DeflaterOutputStream(
				underlyingOut, d);
		DataOutputStream dataOut = new DataOutputStream(deflaterOut);
		return dataOut;

	}

	public static DataInputStream deflatedDataIn(InputStream underlyingIn) {
		Inflater d = new Inflater();
		InflaterInputStream deflaterOut = new InflaterInputStream(underlyingIn,
				d);
		DataInputStream dataOut = new DataInputStream(deflaterOut);
		return dataOut;

	}

	/*
	 * Debug Gui
	 */

	static int guiID;
	static Console c;
	static Gui2 gui;

	public static void startUtilsDebug() {
		if (debug == false) {
			debug = true;
			guiID = GuiManager.createGui("Utils Debug Console");
			gui = GuiManager.getGui(guiID);
			c = new Console(20, 80);
			gui.addDrawable2(c);
		}
	}

	/**
	 * a partial match function, not case sensitive returns true if searchTerm
	 * is found in value or they are equal.
	 * 
	 * @param searchTerm
	 * @param value
	 * @return
	 */
	public static boolean partialMatch(String searchTerm, String value) {
		String st = searchTerm.toLowerCase();
		String val = value.toLowerCase();
		boolean eq = st.equals(val);
		boolean con1 = val.contains(st);
		if (debug) {
			if (eq) {
				c.addLine(st + " = " + val);
			}
			if (con1) {
				c.addLine(val + " contains " + st);
			}
			if (eq || con1) {

			} else {
				c.addLine(st + " !=/ " + val);
			}
			gui.repaint();
		}

		boolean rtrn = eq || con1;
		return rtrn;
	}

	/**
	 * THIS IS FOR COMPRESSED DATA OUTPUT
	 * 
	 * @return the compressed DataInputStream
	 */
	public static DataInputStream dialogInput() {
		int i = fc.showOpenDialog(null);
		if (i == JFileChooser.APPROVE_OPTION) {
			try {
				FileInputStream fis = new FileInputStream(fc.getSelectedFile());
				DataInputStream dis = Utils.deflatedDataIn(fis);
				return dis;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				if (debug) {
					c.addLine("File not found");
				}
			}

		}
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * safely reads user input for integers RETURNS NULL if invalid input occurs
	 * 
	 * @param s
	 *            the scanner to use
	 * @return the integer or null for invalid input
	 */
	public static Integer safeUserReadInt(Scanner s) {
		Integer i = null;
		if (s.hasNextInt()) {
			i = s.nextInt();
		} else {
			if (debug) {
				c.addLine("invalid input: " + s.next());
			} else {
				s.next();
			}
		}

		return i;
	}
	static Random rand = new Random();
	
	public static Color genRandColor() {
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);
		int rgb = r*256*256+g*256+b;
		//System.out.println("r: " + r + " g: " + g + " b: " + b);
		Color c = new Color(rgb);
		//System.out.println(c.getRed());
		return c;
	}
	
	/**
	 * DO NOT USE THIS FUNCTION, currently broken.
	 * 4-14-16
	 * 
	 * @param x
	 * @param y
	 * @param destX
	 * @param destY
	 * @param moveSpeed
	 * @return
	 */
	public static Point2D move(Float x, Float y, Float destX, Float destY, Float moveSpeed) {
		float diffX = destX - x;
		float diffY = destY - y;
		float divider = 1;
		if (Math.abs(diffX) > Math.abs(diffY)) {
			divider = Math.abs(diffX);
		} else {
			divider = Math.abs(diffY);
		}
		diffX /= divider;
		diffY /= divider;
		
		x+= diffX * moveSpeed;
		y+= diffY * moveSpeed;
		
		Point2D p2 = null;// = new Point2D();
		p2.setLocation(x, y);
		return p2;
	}
	
	
/*
	public static ArrayList<?> deepCopy(ArrayList<?> items) {
		ArrayList<?> rtrn;
		//for (Object i : items) {
			rtrn.addAll((Collection<?>) items);
		//}
		return rtrn;
	}
*/
	public static String eol = System.getProperty("line.separator");
	private static Map<String,ArrayList<String>> trackedVariables = new HashMap<String,ArrayList<String>>();
	public static void trackVariableString(String id,String data) {
	  ArrayList<String> list = trackedVariables.get(id);
	  if (list==null) {
	    ArrayList<String> createdList = new ArrayList<String>();
	    trackedVariables.put(id, createdList);
	    list = trackedVariables.get(id);
	  }
	  list.add(data);
	  
	}
	public static String toStringTrackedVariable(String id) {
	  String rtrn = "";
	  ArrayList<String> list = trackedVariables.get(id);
	  if (list == null) {
	    rtrn = "tracked variable: " + id + " has not Values";
	    return rtrn;
	  }
	  
	  rtrn += "Tracked Variable: " + id +eol;
	  for (String s : list) {
	    rtrn += s + eol;
	  }
	  return rtrn;
	  
	  
	}
	
	public static String toStringAllTrackedVariables() {
	  String rtrn = "All Tracked Variables" + eol;
	  for (String key:trackedVariables.keySet()) {
	    rtrn += toStringTrackedVariable(key);
	  }
	  return rtrn;
	}
	
	public static String toStringContainingTrackedVariables(String filter) {
    String rtrn = "All Tracked Variables Containing: "+ filter + eol;
    for (String key:trackedVariables.keySet()) {
      if (key.contains(filter)) {
      rtrn += toStringTrackedVariable(key);
      }
    }
    return rtrn;
  }
	/**
	 * 
	 * @param id the tracked variable ID
	 * @param data the data to track
	 * @param c The console to output the variable to (or null if you want sysOut)
	 * @return All the tracked Variable data (toString) for this id 
	 */
	public static String trackAndOutputVariable(String id, String data, Console c) {
	  trackVariableString(id, data);
	  c.addLine("- "+id + " : " + data);
	  return toStringTrackedVariable(id);
	}
	
}
