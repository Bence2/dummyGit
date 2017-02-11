import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class FastCollinearPointsRegiContains {
	private Point[] points;
	private LineSegment[] lineSegments;
	private int collinearLength = 4;
	
	public static void main(String[] args) {
		Point p1 = new Point(1, 1);
		Point p2 = new Point(2, 2);
		Point p3 = new Point(2, 3);
		Point p4 = new Point(3, 3);
		Point p5 = new Point(4, 4);
		Point p6 = new Point(4, 5);
		Point p7 = new Point(4, 6);
		Point[] allPoints = {p1, p2, p3, p4, p5, p6, p7};
		FastCollinearPointsRegiContains bcp = new FastCollinearPointsRegiContains(allPoints);
		
//		Point tp1 = new Point(4, 0);
//		Point tp2 = new Point(3, 1);
//		Point tp3 = new Point(2, 2);
//		Point tp4 = new Point(1, 3);
//		Point tp5 = new Point(5, 1);
//		Point tp6 = new Point(6, 2);
//		Point tp7 = new Point(7, 3);
//		Point[] trickyPoints = {tp1, tp7, tp3, tp4, tp5, tp6, tp2};
//		Arrays.sort(trickyPoints);
//		Arrays.sort(trickyPoints, tp1.slopeOrder());
//		System.out.println(trickyPoints);
		
		
//		int k = 3;
//		int n = allPoints.length;
//		Point[] tmpPoints = new Point[k];
//		int initialPosition = 0;
//		int nextInt = 0;
//		List<Point[]> generatedPointCombinations = new ArrayList<>();
//		bcp.generatePointCombinations(tmpPoints, allPoints, initialPosition, nextInt, k, n, generatedPointCombinations);
//		System.out.println(generatedPointCombinations);
	}
	
	public FastCollinearPointsRegiContains(Point[] points) {
		this.points = points;
		Arrays.sort(points);
		this.lineSegments = generateLineSegmentCombinations(points);
	}
	
	private LineSegment[] generateLineSegmentCombinations(Point[] points) {
		Point[] pointsToSort = Arrays.copyOf(points, points.length);
		LineSegment[] lineSegmentReturnArray;
		List<LineSegment> collinearLineSegments = new ArrayList<>();
		for (Point pointReference : points) {
			Arrays.sort(pointsToSort, pointReference.slopeOrder());
			collinearLineSegments.addAll(containsCollinearPoints(pointsToSort , collinearLength));
		}
		lineSegmentReturnArray = new LineSegment[collinearLineSegments.size()];
		lineSegmentReturnArray = collinearLineSegments.toArray(lineSegmentReturnArray);
		return lineSegmentReturnArray;
		
	}
	
	private List<LineSegment> containsCollinearPoints(Point[] sortedPoints, int length) {
		List<LineSegment> collinearLineSegments = new ArrayList<>();
		// we need length - 1 collinearSlopeLength, because eg.: there are 3 slopes between 4 points
		int collinearSlopeLength = 0;
		double previousSlope = 0;
		double currentSlope = 0;
		for (int i = 1; i < sortedPoints.length; i++) {
			previousSlope = currentSlope;
			currentSlope = sortedPoints[i].slopeTo(sortedPoints[i-1]);
			
			if (previousSlope == currentSlope) {
				
				/*
				 * might need starting and ending indices
				 * the length of the collinear points has to be equal or GREATER
				 * thus if collinearLenght == length it is not necessary a stopping condition
				 * once we know we found collinear points, we can only stop when we know it is ruined by the next Point
				 */
				if (collinearSlopeLength == 0) {
					collinearSlopeLength = 2;
				} 
				else {
					collinearSlopeLength++;
				}
			}
			/*
			 * if the new slope differs from the previous slope, it means, that this point is
			 * not on the szoval erted nincs rajta az egyenesen
			 */
			else if (collinearSlopeLength >= length-1) {
				int startingIndex = i-1-collinearSlopeLength;
				int endingIndex = i-1;
				int collinearPointsLength = collinearSlopeLength + 1;
				Point[] collinearPoints = new Point[collinearPointsLength];
				System.arraycopy(sortedPoints, startingIndex, collinearPoints, 0, collinearPointsLength);
				Arrays.sort(collinearPoints);
				LineSegment collinearLS = new LineSegment(sortedPoints[0], sortedPoints[collinearPointsLength-1]);
				// somehow prevent, to add the same LineSegment
				collinearLineSegments.add(collinearLS);
				collinearSlopeLength = 0;
			}
			else {
				collinearSlopeLength = 0;
			}
			
		}
		return collinearLineSegments;
	}
}
