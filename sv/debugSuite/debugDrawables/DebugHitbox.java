package sv.debugSuite.debugDrawables;

public class DebugHitbox {
  int x1,y1,x2,y2;
  public DebugHitbox(int x1,int y1,int x2,int y2) {
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
  public DebugHitbox(int x1,int y1, int squareSize) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = this.x1 + squareSize;
    this.y2 = this.y1 + squareSize;
  }
  
  public boolean checkWithinBounds(int x1L,int y1L) {
    boolean result = false;
    if (this.x1 >= x1L && this.x2 <= x1L) {
      if (this.y1 >= y1L && this.y2 <= y1L) {
        result = true;
      }
    }
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
}
