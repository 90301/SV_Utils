package sv.gui2.drawables2;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public interface Drawable2 {
/*
 * interface for drawable items and 
 */
	public void paint(Graphics2D g);
	public GuiHitbox getHitbox();
	public void onMouseClick();
	public void setMouseAction(Runnable r);
	public void onKeyPress(KeyEvent k);
	
}
