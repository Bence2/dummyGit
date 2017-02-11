import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(Point that) {
		if (this.y > that.y || (this.y == that.y && this.x > that.x)) {
			return 1;
		}
		else if (this.y == that.y && this.x == that.x) {
			return 0;
		}
		else {
			return -1;
		}
	}

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
	
	/**
	 * the slope between this point and that point
	 * @param that
	 * @return
	 */
	public double slopeTo(Point that) {
		if (this.x == that.x && this.y != that.y) {
			return Double.POSITIVE_INFINITY;
		}
		if (this.x == that.x && this.y == that.y) {
			return Double.NEGATIVE_INFINITY;
		}
		return (that.y - this.y) / (double)(that.x - this.x);
	}
	
	/**
	 * compares two points by slopes they make with this point
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first argument is less than, equal to, or greater than the second.
	 * @return
	 */
	public Comparator<Point> slopeOrder() {
		return new Comparator<Point>() {
			
			@Override
			public int compare(Point point1, Point point2) {
				double slopeToPoint1 = slopeTo(point1);
				double slopeToPoint2 = slopeTo(point2);
				if (slopeToPoint1 > slopeToPoint2) {
					return 1;
				}
				else if (slopeToPoint1 == slopeToPoint2) {
					return 0;
				}
				else {
					return -1;
				}
			}
		};
		
	}
	
	@Override
	public String toString() {
		return "x coordinate: " + this.x + ", y coordinate: " + this.y;
	}
}
