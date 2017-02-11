import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Line;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private Point[] points;
	private LineSegment[] lineSegments;
	private int collinearLength = 4;
	
	public static void main(String[] args) {
//		Point p1 = new Point(1, 1);
//		Point p2 = new Point(2, 2);
//		Point p3 = new Point(2, 3);
//		Point p4 = new Point(3, 3);
//		Point p5 = new Point(4, 4);
//		Point p6 = new Point(4, 5);
//		Point p7 = new Point(4, 6);
//		Point[] allPoints = {p1, p2, p3, p4, p5, p6, p7};
	
		Point tp1 = new Point(4, 0);
		Point tp2 = new Point(3, 1);
		Point tp3 = new Point(2, 2);
		Point tp4 = new Point(1, 3);
		Point tp5 = new Point(5, 1);
		Point tp6 = new Point(6, 2);
		Point tp7 = new Point(7, 3);
		Point tp8 = new Point(3, -1);
		Point tp9 = new Point(5, -1);
		
		Point[] trickyPoints = {tp1, tp7, tp3, tp4, tp5, tp6, tp2, tp8, tp9};
		FastCollinearPoints bcp = new FastCollinearPoints(trickyPoints);
	}
	public FastCollinearPoints(Point[] pointsParam) {
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
	    Arrays.sort(points);
	    this.lineSegments = generateLineSegmentCombinations(points);
	}
	
//	public static void main(String[] args) {
//
//	    // read the n points from a file
//	    In in = new In(args[0]);
//	    int n = in.readInt();
//	    Point[] points = new Point[n];
//	    for (int i = 0; i < n; i++) {
//	        int x = in.readInt();
//	        int y = in.readInt();
//	        points[i] = new Point(x, y);
//	    }
//
//	    // draw the points
//	    StdDraw.enableDoubleBuffering();
//	    StdDraw.setXscale(0, 32768);
//	    StdDraw.setYscale(0, 32768);
//	    for (Point p : points) {
//	        p.draw();
//	    }
//	    StdDraw.show();
//
//	    // print and draw the line segments
//	    FastCollinearPoints collinear = new FastCollinearPoints(points);
//	    for (LineSegment segment : collinear.segments()) {
//	        StdOut.println(segment);
//	        segment.draw();
//	    }
//	    StdDraw.show();
//	}
	
	
	public int numberOfSegments() {
		return this.lineSegments.length;
	}
	
	public LineSegment[] segments() {
		return this.lineSegments;
	}
	
	private LineSegment[] generateLineSegmentCombinations(Point[] points) {
		Point[] pointsToSort = Arrays.copyOf(points, points.length);
		LineSegment[] lineSegmentReturnArray;
		List<LineSegment> collinearLineSegments = new ArrayList<>();
		for (Point pointReference : points) {
			Arrays.sort(pointsToSort, pointReference.slopeOrder());
			collinearLineSegments.addAll(containsCollinearPoints(pointsToSort , collinearLength));
		}
		List<LineSegment> filteredLineSegments = filterDuplicateLineSegments(collinearLineSegments);
		lineSegmentReturnArray = new LineSegment[filteredLineSegments.size()];
		lineSegmentReturnArray = filteredLineSegments.toArray(lineSegmentReturnArray);
		return lineSegmentReturnArray;
		
	}
	
	private List<LineSegment> filterDuplicateLineSegments(List<LineSegment> unfilteredLineSegments) {
	    List<String> filteredLineSegmentStrings = new ArrayList<>();
	    List<LineSegment> filteredLineSegments = new ArrayList<>();
	    for (LineSegment lineSegment : unfilteredLineSegments) {
            if (!filteredLineSegmentStrings.contains(lineSegment.toString())) {
                filteredLineSegmentStrings.add(lineSegment.toString());
                filteredLineSegments.add(lineSegment);
            }
        }
	    return filteredLineSegments;
	}
	
	private List<LineSegment> containsCollinearPoints(Point[] sortedPoints, int length) {
		List<LineSegment> collinearLineSegments = new ArrayList<>();
		int collinearPointsCounter = 0;
		double slopeToReferencePoint = 0.9987654321;
		double previousSlopeToReferencePoint = 0.9987654321;
		Point referencePoint = sortedPoints[0];
		for (int i = 1; i < sortedPoints.length; i++) {
			previousSlopeToReferencePoint = slopeToReferencePoint;
			slopeToReferencePoint = referencePoint.slopeTo(sortedPoints[i]);
			if (previousSlopeToReferencePoint == slopeToReferencePoint) {
				if (collinearPointsCounter == 0) {
					collinearPointsCounter = 3;
				}
				else {
					++collinearPointsCounter;
					if (i == (sortedPoints.length - 1) && collinearPointsCounter >= length) {
						Point[] collinearPoints = new Point[collinearPointsCounter];
						collinearPoints[0] = referencePoint;
						System.arraycopy(sortedPoints, i - (collinearPointsCounter - 1) + 1, collinearPoints, 1, collinearPointsCounter - 1);
						Arrays.sort(collinearPoints);
						LineSegment collinearLS = new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length-1]);
						collinearLineSegments.add(collinearLS);
					}
				}
			}
			else {
				if (collinearPointsCounter >= length) {
					Point[] collinearPoints = new Point[collinearPointsCounter];
					collinearPoints[0] = referencePoint;
					System.arraycopy(sortedPoints, i - (collinearPointsCounter - 1), collinearPoints, 1, collinearPointsCounter - 1);
					Arrays.sort(collinearPoints);
					LineSegment collinearLS = new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length-1]);
					collinearLineSegments.add(collinearLS);
				}
				collinearPointsCounter = 0;
			}
			
		}
			
			
		return collinearLineSegments;
	}
}
