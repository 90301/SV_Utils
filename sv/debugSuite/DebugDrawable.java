package sv.debugSuite;

import java.awt.Color;
import java.awt.Graphics2D;

import sv.debugSuite.debugDrawables.DebugHitbox;

public interface DebugDrawable {
  public void draw(Graphics2D g2);
  public int getDrawID();
  public void changeColor(Color color);
  public DebugHitbox getHitbox();
}
