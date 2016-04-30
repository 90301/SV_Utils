package sv.debugSuite;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import sv.debugSuite.debugDrawables.DebugRectangle;
import sv.debugSuite.debugDrawables.DebugSquare;
import sv.debugSuite.debugDrawables.SquareArray;
import sv.distributed2.Syncable;

public class DebugSuite {

	/*
	 * A class intended to assist with debugging featuring things like output,
	 * testing, program pausing and csv output
	 */
	public DebugSuite() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * No argument output. Intended to be used for quick debugging. outputs a
	 * hardcoded string.
	 */
	public static void output() {
		output("No Arg Output");
	}

	/*
	 * string argument output.
	 */
	public static void output(String outStr) {
		System.out.println(outStr);
	}

	public static void conditionalOutput(String outStr, boolean show) {
		if (show) {
			System.out.println(outStr);
		}
	}

	/*
	 * Makes things a bit easier to read.
	 */
	public static void exceptionHandledSleep(long arg1) {
		try {
			//System.out.println("sleeping for: " + arg1);
			Thread.sleep(arg1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void pause() {
    System.out.println("Program is paused, press [ENTER] to continue.");
    Scanner waitScanner = new Scanner(System.in);
    waitScanner.nextLine();
    waitScanner.close();
  }
	
	/*
	 * Pause the program on this output
	 */
	public static void pause(String msg) {
	  System.out.println("Program is paused, press [ENTER] to continue.");
	  System.out.println(msg);
	  Scanner waitScanner = new Scanner(System.in);
	  waitScanner.nextLine();
	  waitScanner.close();
	}
	/*
	 * GUI debugger, for those annoying things.
	 */
	/*
	 * This is designed to be a debugging GUI, it's just here to display data
	 * and almost all of that needs to be handled through this class, statically
	 * multiple guis at the same time need to be possible creating them needs to
	 * be easy, like DebugSuite.createGUI("TITLE"); it should return something
	 * that makes that gui accsessable (like an ID as an int) two types of data
	 * need to be displayable, one that get's updated (aka replacing the old
	 * value) and one that stays there. it might be a good idea to divide the
	 * window in 2 to display both the data to display should be stored in an
	 * object, and placed in the debugSuiteGui object an ID might be the best
	 * way to deal with the update-able information.
	 */
	private static ArrayList<DebugSuiteGui> guis = new ArrayList<DebugSuiteGui>();

	public static int createDebugGui(String title) {
		JFrame jframe = new JFrame();
		jframe.setSize(800, 400);
		jframe.setTitle(title);
		DebugSuiteGui gui = new DebugSuiteGui();
		jframe.addMouseListener(gui);

		jframe.add(gui);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		guis.add(gui);

		return guis.size() - 1;

	}

	public static void addGuiConsoleOutput(String output, int guiID) {
		guis.get(guiID).consoleDebug.add(output);
		guis.get(guiID).repaint();
	}

	public static void guiOutput(String output, int guiID, int line) {
		while (guis.get(guiID).updateableInfo.size() < line + 1) {
			guis.get(guiID).updateableInfo.add("");
		}
		guis.get(guiID).updateableInfo.set(line, output);
		guis.get(guiID).repaint();
	}
	/*
	 * appends output to the gui output, similar to the regular console
	 * returns the line so this value can be edited later
	 */
	 public static int addGuiOutput(String output, int guiID) {
	   int line = guis.get(guiID).updateableInfo.size();
	    while (guis.get(guiID).updateableInfo.size() < line + 1) {
	      guis.get(guiID).updateableInfo.add("");
	    }
	    guis.get(guiID).updateableInfo.set(line, output);
	    guis.get(guiID).repaint();
	    return line;
	  }

	/*
	 * outputs an entire arraylist using the ToString function returns the line
	 * to continue writing on after this function call.
	 */
	public static int guiOutput(ArrayList<?> output, int guiID, int startLine) {
		while (guis.get(guiID).updateableInfo.size() < (startLine)
				+ output.size()) {
			guis.get(guiID).updateableInfo.add("");
		}
		for (int i = 0; i < output.size(); i++) {
			guis.get(guiID).updateableInfo.set(startLine + i, output.get(i)
					.toString());
		}

		guis.get(guiID).repaint();
		return startLine + output.size();
	}

	public static void guiClear(int guiID) {
		guis.get(guiID).updateableInfo.clear();
		guis.get(guiID).consoleDebug.clear();
		guis.get(guiID).drawables.clear();
	}

	public static final String[] guiModes = new String[] { "console", "drawing" };

	public static void guiChangeMode(int mode, int guiID) {
		DebugSuite.guis.get(guiID).guiStyle = mode;
		guis.get(guiID).repaint();
	}

	public static int guiAddRect(int guiID, int corner1X, int corner1Y,
			int sizeX, int sizeY) {
		int rtrn = -1;
		DebugRectangle rect = new DebugRectangle(corner1X, corner1Y, sizeX,
				sizeY);
		guis.get(guiID).drawables.add(rect);
		rtrn = guis.get(guiID).drawables.indexOf(rect);
		guis.get(guiID).repaint();
		return rtrn;
	}

	public static void guiModRect(int guiID, int drawableID, Color color,
			int corner1X, int corner1Y, int sizeX, int sizeY) {
		if (guis.get(guiID).drawables.get(drawableID).getDrawID() == 1) {
			DebugRectangle rect = (DebugRectangle) guis.get(guiID).drawables
					.get(drawableID);
			rect.updateRect(corner1X, corner1Y, sizeX, sizeY);
			rect.changeColor(color);
		} else {
			DebugSuite
					.output("Check the gui code, attempted to call gui Mod Rect on non-rect");
		}
		guis.get(guiID).repaint();
	}

	public static int guiAddSquare(int guiID, int corner1X, int corner1Y,
			int size) {
		int rtrn = -1;
		DebugSquare square = new DebugSquare(corner1X, corner1Y, size);
		guis.get(guiID).drawables.add(square);
		rtrn = guis.get(guiID).drawables.indexOf(square);
		guis.get(guiID).repaint();
		return rtrn;
	}

	public static void guiModSquare(int guiID, int drawableID, Color color,
			int corner1X, int corner1Y, int size) {
		if (guis.get(guiID).drawables.get(drawableID).getDrawID() == 2) {
			DebugSquare square = (DebugSquare) guis.get(guiID).drawables
					.get(drawableID);
			square.updateSquare(corner1X, corner1Y, size);
			square.changeColor(color);
		} else {
			DebugSuite
					.output("Check the gui code, attempted to call gui Mod Square on non-square");
		}
		guis.get(guiID).repaint();
	}

	public static void guiModColor(int guiID, int drawableID, Color color) {
		guis.get(guiID).drawables.get(drawableID).changeColor(color);
		guis.get(guiID).repaint();
	}

	public static void guiRepaint(int guiID) {
		guis.get(guiID).repaint();
	}

	public static int addSquareArray(SquareArray landSquares, int guiID) {
		// TODO Auto-generated method stub
		guis.get(guiID).drawables.add(landSquares);
		return guis.get(guiID).drawables.lastIndexOf(landSquares);
	}

	public static SquareArray getSquareArray(int landSquareRef, int guiID) {
		// TODO Auto-generated method stub
		SquareArray s = (SquareArray) guis.get(guiID).drawables.get(landSquareRef);
		return s;
	}
	
	public static DebugSuiteGui getGUI(int i) {
	  return DebugSuite.guis.get(i);
	}



}
