import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * The method segments() should include each line segment containing 4 points exactly once.
 * If 4 points appear on a line segment in the order p->q->r->s, then you should include
 * either the line segment p->s or s->p (but not both) and you should not include subsegments
 * such as p->r or q->r. For simplicity, we will not supply any input to BruteCollinearPoints
 * that has 5 or more collinear points. 
 */

public class BruteCollinearPoints {
    private static final int COMBINATION_LENGTH = 4;
    
	private Point[] points;
	private LineSegment[] lineSegments;
	/*
	 * check whether the 4 points p, q, r, and s 
	 * are collinear, check whether the three 
	 * slopes between p and q, between p and r, and between p and s are all equal. 
	 */
	public BruteCollinearPoints(Point[] pointsParam) {
		this.points = pointsParam;
		if (points == null) {
			throw new NullPointerException();
		}
		else {
			for (Point point : points) {
				if (point == null) {
					throw new NullPointerException();
				}
			}
		}
//		Point p1 = new Point(1, 1);
//		Point p2 = new Point(2, 2);
//		Point p3 = new Point(3, 3);
//		Point p4 = new Point(4, 4);
//		Point p5 = new Point(4, 5);
		
//		Point[] allPoints = {p1, p2, p3, p4, p5};
//		points = allPoints;
		
		int k = COMBINATION_LENGTH;
		int n = points.length;
		Point[] tmpPoints = new Point[k];
		int initialPosition = 0;
		int nextInt = 0;
		List<Point[]> generatedPointCombinations = new ArrayList<>();
		generatePointCombinations(tmpPoints, this.points, initialPosition, nextInt, k, n, generatedPointCombinations);
		this.lineSegments = getValidLineSegmentsFromPoints(generatedPointCombinations);
		
	}
	
	private LineSegment[] getValidLineSegmentsFromPoints(List<Point[]> generatedPointCombinations) {
		List<LineSegment> tmpLineSegmentList = new ArrayList<>();
		for (Point[] points : generatedPointCombinations) {
			/*
			 * firs sort the array to know the two ending points
			 * could implement a custom version of eg.: mergesort
			 */
			Arrays.sort(points);
			Point referencePoint = points[0];
			double referenceSlope = referencePoint.slopeTo(points[1]);
			boolean isCollinearCombination = true;
			for (int i = 1; i < points.length; i++) {
				double slope = points[i].slopeTo(referencePoint);
				if (slope != referenceSlope) {
					isCollinearCombination = false;
					break;
				}
			}
			if (isCollinearCombination) {
				LineSegment ls = new LineSegment(referencePoint, points[points.length-1]);
				tmpLineSegmentList.add(ls);
			}
		}
		LineSegment[] tmpLineSegmentArray = new LineSegment[tmpLineSegmentList.size()];
		for (int i = 0; i < tmpLineSegmentArray.length; i++) {
			tmpLineSegmentArray[i] = tmpLineSegmentList.get(i);
		}
		return tmpLineSegmentArray;
	}
	
	/**
	 * the number of line segments
	 * @return
	 */
	public int numberOfSegments() {
		return lineSegments.length;
	}
	
	/**
	 * the line segments
	 * @return
	 */
	public LineSegment[] segments() {
		return lineSegments;
	}
	/**
	 * 
	 * @param tmpPoints
	 * @param allPoints
	 * @param position - position of the index
	 * @param nextInt 
	 * @param k - size of the target array
	 * @param n - size of the Point[] array pool
	 */
	private void generatePointCombinations(Point[] tmpPoints, Point[] allPoints, int position, int nextInt, int k, int n
			, List<Point[]> generatedPointCombinations) {
		if (position == k) {
			generatedPointCombinations.add(tmpPoints);
			return;
		}
		for (int i = nextInt; i < n; i++) {
			Point[] tmpPointsCopy = Arrays.copyOf(tmpPoints, tmpPoints.length);
			tmpPointsCopy[position] = allPoints[i];
			generatePointCombinations(tmpPointsCopy, allPoints, position+1, i+1, k, n, generatedPointCombinations);
		}
	}
//	public static void main(String[] args) {
//		Point p1 = new Point(1, 1);
//		Point p2 = new Point(2, 2);
//		Point p3 = new Point(3, 3);
//		Point p4 = new Point(4, 4);
//		Point[] allPoints = {p1, p2, p3, p4};
//		BruteCollinearPoints bcp = new BruteCollinearPoints(allPoints);
//		int k = 3;
//		int n = allPoints.length;
//		Point[] tmpPoints = new Point[k];
//		int initialPosition = 0;
//		int nextInt = 0;
//		List<Point[]> generatedPointCombinations = new ArrayList<>();
//		bcp.generatePointCombinations(tmpPoints, allPoints, initialPosition, nextInt, k, n, generatedPointCombinations);
//		System.out.println(generatedPointCombinations);
//	}
}
