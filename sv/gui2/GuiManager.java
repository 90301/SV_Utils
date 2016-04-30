package sv.gui2;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;

import sv.debugSuite.DebugSuiteGui;

/**
 * This is a static class to create and modify guis
 * @author Josh Benton
 *
 */
public class GuiManager {
	
	static ArrayList<JFrame> frames = new ArrayList<JFrame>();
	static ArrayList<Gui2> guis = new ArrayList<Gui2>();
	
	public static int createGui(String title) {
		JFrame jframe = new JFrame(title);
		jframe.setSize(800, 400);
		//jframe.setTitle(title);
		jframe.setBackground(Color.black);
		jframe.setForeground(Color.white);
		Gui2 gui = new Gui2();
		gui.setBackground(Color.black);

		//jframe.add(gui);
		jframe.setContentPane(gui);
		jframe.addMouseListener(gui);
		jframe.addKeyListener(gui);

		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		guis.add(gui);

		return guis.lastIndexOf(gui);
	}
	
	public static void updateGuis() {
		for (Gui2 gui:guis) {
			gui.repaint(10);
		}
	}

	/**
	 * No error checking, get the gui object, shallow copy.
	 * May update this later to support removing GUIS and doing a lookup
	 * based on a guiID.
	 * @param guiID the position in the arrayList of guis.
	 * @return the gui
	 */
	public static Gui2 getGui(int guiID) {
		return guis.get(guiID);
		
	}
	/**
	 * Gets the JFrame for the guiID
	 * useful for adding components
	 * @param guiID
	 * @return
	 */
	public static JFrame getFrame(int guiID) {
		return frames.get(guiID);
	}

	public static void updateGui(int guiID) {
		guis.get(guiID).repaint(10);
		
	}
}
