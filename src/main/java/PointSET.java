import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private  Set<Point2D> pointSet; 
    
    public static void main(String[] args) {

    }
    
    public PointSET() {
        pointSet = new TreeSet<>();
        
    }
    
    public Iterable<Point2D> range(RectHV rectangle) {
        checkInputData(rectangle);
        Set<Point2D> pointsWithinRectangle = new TreeSet<>();
        for (Point2D point2d : pointSet) {
            if (rectangle.contains(point2d)) {
                pointsWithinRectangle.add(point2d);
            }
        }
        return pointsWithinRectangle;
    }
    
    public Point2D nearest(Point2D refPoint) {
        checkInputData(refPoint);
        Point2D nearestPoint = null;
        double smallestDistance = Double.MAX_VALUE;
        for (Point2D point2d : pointSet) {
            double distance = refPoint.distanceSquaredTo(point2d);
            if (distance < smallestDistance) {
                nearestPoint = point2d;
                smallestDistance = distance;
            }
        }
        return nearestPoint;
    }
    
    private void checkInputData(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }
    
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }
    
    public int size() {
        return pointSet.size();
    }
    
    public void insert(Point2D point) {
        checkInputData(point);
        pointSet.add(point);
    }
    
    public boolean contains(Point2D point) {
        checkInputData(point);
        return pointSet.contains(point);
    }
    
    public void draw() {
        for (Point2D point2d : pointSet) {
            point2d.draw();
        }
    }

}
