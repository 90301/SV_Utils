package sv.gui2.drawables2;

import java.awt.Graphics2D;

public class GuiHitbox {
	  int x1,y1,x2,y2;
	  public GuiHitbox(int x1,int y1,int x2,int y2) {
	    // TODO Auto-generated constructor stub
	    if (x1 < x2) {
	    this.x1 = x1;
	    this.x2 = x2;
	    } else {
	      this.x2 = x1;
	      this.x1 = x2;
	    }
	    if (y1<y2) {
	    this.y1 = y1;
	    this.y2 = y2;
	    } else {
	      this.y2 = y1;
	      this.y1 = y2;
	    }
	    
	  }
	  public GuiHitbox(int x1,int y1, int squareSize) {
	    this.x1 = x1;
	    this.y1 = y1;
	    this.x2 = this.x1 + squareSize;
	    this.y2 = this.y1 + squareSize;
	  }
	  
	  public boolean checkWithinBounds(int x1L,int y1L) {
	    boolean result = false;
	    if (this.x1 <= x1L && this.x2 >= x1L) {
	      if (this.y1 <= y1L && this.y2 >= y1L) {
	        result = true;
	      }
	    }
	    //System.out.println("check results: " + result + ":" + x1 + " < " + x1L + " < "+ x2);
	    return result;
	  }
	  public void update(int x1, int y1, int x2, int y2) {
	    // TODO Auto-generated method stub
	    if (x1 < x2) {
	    this.x1 = x1;
	    this.x2 = x2;
	    } else {
	      this.x2 = x1;
	      this.x1 = x2;
	    }
	    if (y1<y2) {
	    this.y1 = y1;
	    this.y2 = y2;
	    } else {
	      this.y2 = y1;
	      this.y1 = y2;
	    }
	  }
	  public void drawHitBox(Graphics2D g) {
		  g.drawRect(x1, y1, x2-x1, y2-y1);
	  }
	  public String toString() {
		  return "hitbox: " + x1 + " , " + y1 + " ( " + x2 + " , " + y2 + " ) ";
	  }
}
