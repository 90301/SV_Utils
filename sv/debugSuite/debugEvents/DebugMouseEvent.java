package sv.debugSuite.debugEvents;

import sv.debugSuite.DebugDrawable;
import sv.debugSuite.debugDrawables.DebugHitbox;

public class DebugMouseEvent implements DebugEvent {
  /*
   * This currently only suppots clicking.
   * Drag support may require renaming this class to:
   * DebugMouseClickEvent
   */
  
  
  String eventName = "";//optional for debugging purposes
  int eventNumber;//another way to retrieve the event
  DebugHitbox hitbox;
  boolean triggered = false;
  public DebugMouseEvent(DebugDrawable dd) {
    this.hitbox = dd.getHitbox();
    // TODO Auto-generated constructor stub
  }
  public DebugMouseEvent(DebugDrawable dd,String eventName) {
    // TODO Auto-generated constructor stub
    this.hitbox = dd.getHitbox();
    this.eventName = eventName;
  }
  public DebugMouseEvent(DebugDrawable dd,int eventNumber) {
    // TODO Auto-generated constructor stub
    this.hitbox = dd.getHitbox();
    this.eventNumber = eventNumber;
  }
  public DebugMouseEvent(DebugDrawable dd,int eventNumber, String eventName) {
    // TODO Auto-generated constructor stub
    this.hitbox = dd.getHitbox();
    this.eventNumber = eventNumber;
    this.eventName = eventName;
  }
  
  public DebugHitbox getHitbox() {
    return this.hitbox;
  }
  /*
   * sets triggered if this return true
   */
  public boolean checkHitbox(int x1L,int y1L) {
    if (this.hitbox.checkWithinBounds(x1L, y1L)) {
      this.triggered = true;
      return true;
    } else {
      return false;
    }
  }
  /*
   * sets triggered to false
   */
  public boolean checkTriggered() {
    boolean rtrn = false;
    if (triggered) {
      triggered = false;
      rtrn = true;
    }
    
    return rtrn;
  }
  public boolean getTriggeredWithoutChange() {
    return triggered;
  }
  public int getEventNumber() {
    return this.eventNumber;
  }
  public String getEventName() {
    return this.eventName;
  }
  public void clearTriggered() {
    this.triggered = false;
    // TODO Auto-generated method stub
    
  }

}
