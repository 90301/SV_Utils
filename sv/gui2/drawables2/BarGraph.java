package sv.gui2.drawables2;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BarGraph implements Drawable2 {
	
	boolean vertical = true;//vertical or horizontal
	int barWidth = 1;//width of the bars
	int maxHeight = 100;//the tallest bar
	int maxWidth = 100;//the number of pixels the graph can extend
	int scaleFactor = 1;//increase as necessary, bar height = value/scaleFactor
	
	int x=0;
	int y=0;
	
	
	ArrayList<Integer> values = new ArrayList<Integer>();
	
	/**
	 * Default values, vertical bar graph
	 */
	public BarGraph() {
		// TODO Auto-generated constructor stub
	}
	public BarGraph(int x,int y,int maxWidth, int maxHeight) {
		this.x = x;
		this.y = y;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	public void addValue(int val) {
		values.add(val);
		while (val/scaleFactor > maxHeight) {
			scaleFactor++;
		}
	}
	
	public void clear() {
		values.clear();
	}

	@Override
	public void paint(Graphics2D g) {
		if (barWidth==1) {
			//use lines
			for (int i=1; i<maxWidth && values.size()-i>=0;i++) {
				int value = values.get(values.size()-i)/scaleFactor;
				g.drawLine(x+maxWidth-i, y+maxHeight, x+maxWidth-i, y+maxHeight-value);
			}
		} else {
			//use rectangles
			for (int i=1; i<maxWidth/barWidth && values.size()-i>=0;i++) {
				int value = values.get(values.size()-i)/scaleFactor;
				g.drawRect(x+maxWidth-i*barWidth, y+maxHeight, x+maxWidth-(i+1)*barWidth, y+maxHeight-value);
			}
		}
		// TODO Auto-generated method stub

	}
	
	GuiHitbox h;
	@Override
	public GuiHitbox getHitbox() {
		if (h==null) {
			h = new GuiHitbox(x,y,x+maxWidth,y+maxHeight);
		}
		return h;
	}

	@Override
	public void onMouseClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMouseAction(Runnable r) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onKeyPress(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}

}
