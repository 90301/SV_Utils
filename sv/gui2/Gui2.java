package sv.gui2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.util.ArrayList;

import javax.swing.JComponent;

import sv.gui2.drawables2.Drawable2;

public class Gui2 extends JComponent implements MouseListener,KeyListener {

	// create arraylist of drawable items
	ArrayList<Drawable2> drawables = new ArrayList<Drawable2>();

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		//g2.setBackground(Color.BLACK);
		try {
			for (Drawable2 d : drawables) {
				d.paint(g2);

				// g2.setColor(Color.RED);
				// d.getHitbox().drawHitBox(g2);
			}
		} catch (Exception e) {
			//ignore the error
		}
		rPaintReady = true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getX() - 8;
		int y = e.getY() - 30;
		// System.out.println("Clicked: " + x + " , " + y);
		for (Drawable2 d : drawables) {
			// System.out.println(d.getHitbox());
		  try {
			if (d.getHitbox().checkWithinBounds(x, y)) {
				d.onMouseClick();

			}
		  } catch (Exception excpt) {
		    System.err.println(excpt.toString());
		  }
		}
		repaint();
	}

	int skipDraw = 0;

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void add(Drawable2 d) {
		drawables.add(d);

	}

	public void addDrawable2(Drawable2 d) {
		drawables.add(d);

	}

	private boolean rPaintReady = true;

	public boolean rPaintReady() {
		// TODO Auto-generated method stub
		return rPaintReady;
	}

	public void setRPaintReady() {
		// TODO Auto-generated method stub
		rPaintReady = false;
	}

	public void remove(Drawable2 p) {
		drawables.remove(p);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		for (Drawable2 d : drawables) {
			d.onKeyPress(arg0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println(arg0.getKeyCode());	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println(arg0.getKeyCode());
	}

}
