package sv.debugSuite.debugDrawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import sv.debugSuite.DebugDrawable;

public class DebugSquare implements DebugDrawable {

  
  public static final int drawID = 2;
private static final int fontSize = 10;
  //start point, and size
  int startX,startY,size;
  Color color = Color.black;//default color
  String text = "1";//text to be printed in the middle of the square. optional.
  public DebugSquare(int startX,int startY, int size) {
    // TODO Auto-generated constructor stub
    this.startX = startX;
    this.startY = startY;
    this.size = size;
  }
  
  public void updateSquare(int startX, int startY, int size) {
    this.startX = startX;
    this.startY = startY;
    this.size = size;
  }

  @Override
  public void draw(Graphics2D g2) {
    // TODO Auto-generated method stub
    g2.setColor(color);
    g2.drawRect(startX, startY, size, size);
	g2.drawString(text, startX, startY+fontSize);//this draws above the text
  }

  @Override
  public int getDrawID() {
    // TODO Auto-generated method stub
    return drawID;
  }

  @Override
  public void changeColor(Color color) {
    // TODO Auto-generated method stub
    this.color = color;
  }
  public void setText(String str) {
	  this.text = str;
  }
  DebugHitbox dhb;
  @Override
  public DebugHitbox getHitbox() {
    if (dhb == null) {
      dhb = new DebugHitbox(this.startX,this.startY,this.size);
    }
    // TODO Auto-generated method stub
    return dhb;
  }
}
