package sv.debugSuite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import sv.debugSuite.debugEvents.DebugEvent;
import sv.debugSuite.debugEvents.DebugMouseEvent;

public class DebugSuiteGui extends JComponent implements ActionListener,WindowListener,MouseListener {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  public DebugSuiteGui() {
    this.addMouseListener(this);
    // TODO Auto-generated constructor stub
  }

  /*
   * these are directly modified by debugSuite, seal with it.
   */
  public int guiStyle = 0;
  public ArrayList<String> consoleDebug = new ArrayList<String>();
  public ArrayList<String> updateableInfo = new ArrayList<String>();
  public ArrayList<DebugDrawable> drawables = new ArrayList<DebugDrawable>();
  //public ArrayList<DebugEvent> debugEvents = new ArrayList<DebugEvent>();
  
  //Events:
  ArrayList<DebugMouseEvent> mouseEvents = new ArrayList<DebugMouseEvent>();
  ArrayList<DebugMouseEvent> activatedMouseEvents = new ArrayList<DebugMouseEvent>();
  
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    if (guiStyle == 0) {
      //dual console style
      int xx = 400;
      int yy = 20;
      int yy2 = 20;
      
      for (int i = consoleDebug.size() - 1; i >= 0; i--) {
        g2.drawString(consoleDebug.get(i), xx, yy);
        yy += 15;
      }

      for (int i = 0; i < updateableInfo.size(); i++) {
        g2.drawString(updateableInfo.get(i), 5, yy2);
        yy2 += 15;
      }
    }
    if (guiStyle == 1) {
      //updateable console and drawing area
      
      int yy2 = 20;
      for (int i = 0; i < updateableInfo.size(); i++) {
        g2.drawString(updateableInfo.get(i), 420, yy2);
        yy2 += 15;
      }
      
      for (DebugDrawable d:this.drawables) {
        d.draw(g2);
      }
      
    }
    
  }
  public ArrayList<DebugMouseEvent> getActivatedMouseEvents() {
    return activatedMouseEvents;
  }
  
  public void clearActivatedMouseEvents() {
    for (DebugMouseEvent dme : this.activatedMouseEvents) {
      dme.clearTriggered();
    }
  }
  
  /*
   * Listeners below
   */

  @Override
  public void windowActivated(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowClosed(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowClosing(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowIconified(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void windowOpened(WindowEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseClicked(MouseEvent arg0) {
    
    // TODO Auto-generated method stub
    int x = arg0.getX();
    int y = arg0.getY();
    DebugSuite.output("Mouse Clicked: " + x + " , " + y);
    for (DebugMouseEvent dme: this.mouseEvents) {
      if (dme.getTriggeredWithoutChange()!= true) {
        if (dme.checkHitbox(x, y)) {
          this.activatedMouseEvents.add(dme);
        }
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
    // TODO Auto-generated method stub
    
  }

}
