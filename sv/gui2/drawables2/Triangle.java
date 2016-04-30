package sv.gui2.drawables2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


public class Triangle implements Drawable2 {

	public static final int NORTH = 0;// 0
	public static final int SOUTH = 2;// 180
	public static final int EAST = 1;// 90
	public static final int WEST = 3;// 270
	int x, y, size;
	private int orintation;
	private Color color;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * creates a triangle at a given X,Y that is Size pixels large. Note that
	 * this is basically a square or equalatral triangle
	 * 
	 * @param x
	 *            top x coordinate
	 * @param y
	 *            top y coordinate
	 * @param size
	 *            size of triangle in pixels
	 */
	public Triangle(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.setOrintation(NORTH);
		calcPoints();
	}

	/**
	 * Sets the x,y and size, also changes the orientation valid orientations
	 * are: NORTH, SOUTH, EAST, WEST
	 * 
	 * @param x
	 *            north west corner
	 * @param y
	 *            north west corner
	 * @param size
	 *            size of surrounding square
	 * @param orientation
	 *            direction point faces
	 */
	public Triangle(int x, int y, int size, int orientation) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.setOrintation(orientation);
		calcPoints();
	}

	int[] xPoints = new int[4];
	int[] yPoints = new int[4];
	public static final int nPoints = 4;

	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.drawPolyline(xPoints, yPoints, nPoints);
		//g.fillPolygon(xPoints, yPoints, nPoints);

	}

	double[][] pointArray = new double[4][2];

	public void calcPoints() {
		xPoints[0] = (int) (x + size / 2);
		yPoints[0] = y;

		xPoints[1] = x;
		yPoints[1] = y + size;

		xPoints[2] = x + size;
		yPoints[2] = y + size;

		xPoints[3] = xPoints[0];
		yPoints[3] = yPoints[0];
		// north facing triangle
		/*
		 * (x1) (x2) (x3) (y1) (y2) (y3)
		 */

		pointArray[0][0] = xPoints[0];
		pointArray[0][1] = yPoints[0];

		pointArray[1][0] = xPoints[1];
		pointArray[1][1] = yPoints[1];

		pointArray[2][0] = xPoints[2];
		pointArray[2][1] = yPoints[2];

		pointArray[3][0] = xPoints[3];
		pointArray[3][1] = yPoints[3];

		double angle = Math.PI / 2 * orintation;
		rotateTriangle(angle);
		for (int i=0; i<xPoints.length;i++) {
		xPoints[i] = (int) pointArray[i][0];
		yPoints[i] = (int) pointArray[i][1];
		}

	}

	public void rotateTriangle(double angle) {
		double[][] angleArray = new double[2][2];
		angleArray[0][0] = Math.cos(angle);
		angleArray[0][1] = -Math.sin(angle);
		angleArray[1][0] = Math.sin(angle);
		angleArray[1][1] = Math.cos(angle);
		/*
		Matrix rotationMatrix = new Matrix(angleArray);
		for (int i = 0; i < pointArray.length; i++) {
			double xDisplace = this.size/2 + this.x;
			double yDisplace = this.size/2 + this.y;
			double[][] point = new double[2][2];
			point[0][0] = pointArray[i][0] - xDisplace;
			point[1][1] = pointArray[i][1] - yDisplace;
			Matrix p = new Matrix(point);
			Matrix nPt = rotationMatrix.arrayTimes(p);
			pointArray[i][0] = nPt.getArray()[0][0] + xDisplace;
			pointArray[i][1] = nPt.getArray()[1][1] + yDisplace;
			System.out.println(nPt.getArray()[0][0] + " " + nPt.getArray()[1][1]);
		}
		*/
	}
	public void centerAbout(int x,int y) {
		this.x = x - this.size/2;
		this.y = y - this.size/2;
		calcPoints();
	}

	@Override
	public GuiHitbox getHitbox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMouseClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMouseAction(Runnable r) {
		// TODO Auto-generated method stub

	}

	public int getOrintation() {
		return orintation;
	}

	public void setOrintation(int orintation) {
		this.orintation = orintation;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void onKeyPress(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}

}
