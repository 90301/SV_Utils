package sv.debugSuite.debugDrawables;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import sv.debugSuite.DebugDrawable;
import sv.debugSuite.DebugSuite;

public class SquareArray implements DebugDrawable {

	/*
	 * let's be honest, there's always an array of squares this class provides
	 * cool ways to use them
	 */
	ArrayList<DebugSquare> squares = new ArrayList<DebugSquare>();
	int size = 10; // default size
	int startX = 10;
	int startY = 10;
	int squareSize = 20; // this can't be changed after starting the program

	public SquareArray() {
		// TODO Auto-generated constructor stub
	}

	public SquareArray(int size) {
		this.size = size;
	}

	public SquareArray(int size, int squareSize) {
		this.size = size;
		this.squareSize = squareSize;
	}

	/*
	 * be careful using this with x and y coordinates
	 */
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		for (DebugSquare square : this.squares) {
			square.draw(g2);
		}
	}

	@Override
	public int getDrawID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void changeColor(Color color) {
		// TODO Auto-generated method stub

	}

	public int getPosition(int x, int y) {
		int rtrn = x + y * this.size;
		return rtrn;
	}

	public int getX(int p) {
		int rtrn = p % this.size;
		return rtrn;
	}

	public int getY(int p) {
		int rtrn = (int) Math.floor(p/this.size);
		return rtrn;
	}

	/*
	 * Returns the int so the newly added square can be recalled.
	 */
	public int addSquare() {
		int p = this.squares.size();// currently one over
		int xx = this.startX + this.getX(p) * this.squareSize;
		int yy = this.startY + this.getY(p) * this.squareSize;
		DebugSquare e = new DebugSquare(xx, yy, this.squareSize);
		squares.add(e);
		return p;
	}

	/*
	 * @Param size: total number of squares to create
	 * 
	 */
	public void createSuperSquare(int size) {
		// bounds checking
		for (int i = 0; i < size; i++) {
			this.addSquare();
		}
	}

	/*
	 * THIS CLEARS ALL SQUARES AND MAKES NEW SQUARES WITH THE NEW SIZE Use with
	 * caution
	 * creates a 
	 */
	public void createSuperSquareMap(int sizeX, int sizeY) {
		if (sizeX >= 0 && sizeX != this.size) {
			this.size = sizeX;
			this.squares.clear();
		} else {
			this.squares.clear();
		}

		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				this.addSquare();
			}
		}
	}
	/*
	 * returns a shallow copy of the square, makes modifying it easier
	 * since getting this square will require many stacked function calls.
	 */
	public DebugSquare getSquare(int i) {
		if (this.squares.size()>i) {
			return this.squares.get(i);
		} else {
			return null;
		}
	}
	/*
	 * x , y cord method
	 */
	public DebugSquare getSquare(int x, int y) {
		int p = this.getPosition(x, y);
		
		return getSquare(p);
	}
	
  DebugHitbox dhb;
  @Override
  public DebugHitbox getHitbox() {
    if (dhb == null) {
      int x2 = this.startX+this.size*this.squareSize;
      int y2 = this.startY + (this.squares.size()/this.size+1)*this.squareSize;
      dhb = new DebugHitbox(this.startX,this.startY,x2, y2);
    } else {
      int x2 = this.startX+this.size*this.squareSize;
      int y2 = this.startY + (this.squares.size()/this.size+1)*this.squareSize;
      dhb.update(this.startX,this.startY,x2, y2);
    }
    DebugSuite.output("Warning: You really should not be using the getHitbox function on a Square Array.");
    // TODO Auto-generated method stub
    return dhb;
  }

}
