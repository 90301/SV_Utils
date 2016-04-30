package sv.debugSuite.debugDrawables;

import java.awt.Color;
import java.awt.Graphics2D;

import sv.debugSuite.DebugDrawable;
import sv.debugSuite.DebugSuite;

public class DebugRectangle implements DebugDrawable {

  private int x1;
  private int y1;
  private int width;
  private int height;

  public DebugRectangle(int arg0, int arg1, int arg2, int arg3) {
    this.x1 = arg0;
    this.y1 = arg1;
    this.width = arg2;
    this.height = arg3;
    // TODO Auto-generated constructor stub
  }
  public void updateRect(int arg0, int arg1, int arg2, int arg3) {
    this.x1 = arg0;
    this.y1 = arg1;
    this.width = arg2;
    this.height = arg3;
  }
  Color color = Color.BLACK;//Default is black
  public void changeColor(Color color) {
    this.color = color;
    DebugSuite.output("Set color: " + color);
  }

  @Override
  public void draw(Graphics2D g2) {
    g2.setColor(color);
    g2.drawRect(x1, y1, width, height);
    // TODO Auto-generated method stub

  }
  
  public static final int drawID = 1;
  @Override
  public int getDrawID() {
    // TODO Auto-generated method stub
    return drawID;
  }
  DebugHitbox dhb;
  @Override
  public DebugHitbox getHitbox() {
    if (dhb == null) {
      dhb = new DebugHitbox(x1,y1,x1+width,y1+height);
    }
    // TODO Auto-generated method stub
    return dhb;
  }

}
