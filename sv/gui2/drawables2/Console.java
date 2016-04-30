package sv.gui2.drawables2;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import sv.gui2.GuiManager;

public class Console implements Drawable2 {
	
	private static final Font font = new Font("Courier", 0, 10);
	private static final int FONT_SIZE = 12;
	private static final int CHAR_WIDTH = 8;

	/*
	 * force monospace font!
	 */
	/**
	 * Default values!
	 */
	public Console() {
		// TODO Auto-generated constructor stub
	}
	int numberOfLines = 8;//how many lines to display at a time
	int maxWidth = 20;//20 characters is the default max
	int x =0;
	int y=0;
	GuiHitbox ghb;
	Runnable mouseAction;
	/**
	 * 
	 * @param lines how many lines to display at a time
	 * @param width the width of the console in characters
	 */
	public Console(int lines, int width) {
		this.numberOfLines = lines;
		this.maxWidth = width;
		this.y = FONT_SIZE;
		this.x = 0;
	}
	
	public Console(int lines, int width,int x, int y) {
		this.numberOfLines = lines;
		this.maxWidth = width;
		this.x = x;
		this.y = y;
	}
	
	ArrayList<String> lines = new ArrayList<String>();
	
	/**
	 * 
	 * @param str to add to the console, should be added to the bottom.
	 * @return the place in the lines array to be recalled with other methods
	 */
	public void addLine(String str) {
		
		if (str.length()>this.maxWidth) {
			for (int i=0; i*this.maxWidth<str.length();i++) {
				int bound2 =i*maxWidth+maxWidth;
				if (bound2>str.length()) {
					bound2 = str.length();
				}
				String subString = str.substring(i*maxWidth, bound2);
				this.lines.add(subString);
			}
		} else {
			this.lines.add(str);
		}
		GuiManager.updateGuis();
	}
	
	/**
	 * Removes all data to print to the screen
	 */
	public void clear() {
		this.lines.clear();
	}
	
	
	
	/*
	 * no processing is done here, split the lines if needed before hand
	 */
	@Override
	public void paint(Graphics2D g) {
		g.setFont(font);
		for (int i=1; i<numberOfLines && lines.size()-i>=0;i++) {
			String lineToPrint = lines.get(lines.size()-i); 
			g.drawString(lineToPrint, x, y+(numberOfLines-i)*FONT_SIZE);
		}
		
	}

	@Override
	public GuiHitbox getHitbox() {
		if (ghb==null) {
			ghb = new GuiHitbox(x,y,x+(this.maxWidth*CHAR_WIDTH),y+this.numberOfLines*FONT_SIZE);
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMouseClick() {
		// TODO Auto-generated method stub
		if (this.mouseAction != null) {
			this.mouseAction.run();
		}
	}

	@Override
	public void setMouseAction(Runnable r) {
		this.mouseAction = r;
		
	}
	
	public String getOutput() {
		String output = "";
		for (String line : lines) {
			output += line + "\n";
		}
		return output;
	}

	@Override
	public void onKeyPress(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}


}
