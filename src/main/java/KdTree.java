import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node rootNode;
    private int size;
    
    public KdTree() {
        this.size = 0;
    }
    
    public static void main(String[] args) {
        testNearestPoint();
        testWithGivenDemoData();
        testWithOwnDemoData();
        
    }
    
    private static void testWithOwnDemoData() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.2, 0.5);
        Point2D point2 = new Point2D(0.1, 0.5);
        Point2D point3 = new Point2D(0.1, 0.7);
        Point2D point4 = new Point2D(0.3, 0.7);
        Point2D point5 = new Point2D(0.4, 0.6);
        Point2D point6 = new Point2D(0.8, 0.6);
        Point2D point7 = new Point2D(0.5, 0.6);
        Point2D pointNemLetezo = new Point2D(0.1, 0.1);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        kdtree.insert(point6);
        kdtree.insert(point7);
        kdtree.insert(point5);
        kdtree.draw();
        assert(kdtree.contains(point7));
        assert(!kdtree.contains(pointNemLetezo));

        Iterable<Point2D> foundPoints = kdtree.range(new RectHV(0.8, 0.8, 0.9, 0.9));
        System.out.println("vege");
    }
    
    private static void testWithGivenDemoData() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.7,  0.2);
        kdtree.insert(point1);
        Point2D point2 = new Point2D(0.5,  0.4);
        kdtree.insert(point2);
        Point2D point3 = new Point2D(0.2,  0.3);
        kdtree.insert(point3);
        Point2D point4 = new Point2D(0.4,  0.7);
        kdtree.insert(point4);
        Point2D point5 = new Point2D(0.9,  0.6);
        kdtree.insert(point5);
        assert(kdtree.rootNode.getRectangle().xmin() == new Double(0));
        assert(kdtree.rootNode.getRectangle().ymax() == new Double(1));
        assert(kdtree.rootNode.getLeftNode().getRectangle().xmax() == new Double(0.7));
        assert(kdtree.rootNode.getLeftNode().getRectangle().xmin() == new Double(0.0));
        assert(kdtree.rootNode.getRightNode().getRectangle().xmax() == new Double(1));
        assert(kdtree.rootNode.getRightNode().getRectangle().xmin() == new Double(0.7));
        assert(kdtree.rootNode.getLeftNode().getLeftNode().getRectangle().ymax() == new Double(0.4));
        assert(kdtree.rootNode.getLeftNode().getLeftNode().getRectangle().ymin() == new Double(0.0));
        assert(kdtree.rootNode.getLeftNode().getLeftNode().getRectangle().xmax() == new Double(0.7));
        assert(kdtree.rootNode.getLeftNode().getLeftNode().getRectangle().xmin() == new Double(0.0));
        // kdtree.draw();
        System.out.println("vege");
    }
    
    private static void testNearestPoint() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.3,  0.9);
        kdtree.insert(point1);
        Point2D point2 = new Point2D(0.2,  0.8);
        kdtree.insert(point2);
        Point2D point3 = new Point2D(0.5,  0.5);
        kdtree.insert(point3);
        Point2D queryPoint = new Point2D(0.9, 0.1);
//        kdtree.draw();
        Point2D closestPoint = kdtree.nearest(queryPoint);
        System.out.println(closestPoint);
    }
    
    public Point2D nearest(Point2D queryPoint) {
        checkInputData(queryPoint);
        if (rootNode == null) return null;
        
//        Point2D closestPoint = rootNode.getPoint();
        Point2D closestPoint = null;
//        double closestDistance = closestPoint.distanceSquaredTo(queryPoint);
        double closestDistance = Double.MAX_VALUE;
        
        Queue<Node> nodeQueue = new LinkedList<>();
        nodeQueue.add(rootNode);
        
        while (nodeQueue.size() != 0) {
            Node currentNode = nodeQueue.poll();
            double currentDistance = currentNode.getPoint().distanceSquaredTo(queryPoint);
            if (currentDistance < closestDistance) {
                closestPoint = currentNode.getPoint();
                closestDistance = currentDistance;
            }
            Node leftNode = currentNode.getLeftNode();
            Node rightNode = currentNode.getRightNode();
            if (leftNode != null && leftNode.getRectangle().contains(queryPoint)) {
                exploreNode(leftNode, queryPoint, closestDistance, nodeQueue);
                exploreNode(rightNode, queryPoint, closestDistance, nodeQueue);
            } else {
                exploreNode(rightNode, queryPoint, closestDistance, nodeQueue);
                exploreNode(leftNode, queryPoint, closestDistance, nodeQueue);
            }
        }
        return closestPoint;
    }
    
    private void exploreNode(Node node, Point2D queryPoint, double closestDistance, Queue<Node> nodeQueue) {
        if (node == null) return;
        double nodesRectangleDistance = node.getRectangle().distanceSquaredTo(queryPoint);
        if (nodesRectangleDistance < closestDistance) {
            nodeQueue.add(node);
        }
    }
    
    public void draw() {
        Queue<Node> nodeQueue = new LinkedList<>();
        if (rootNode == null) {
            return;
        }
        nodeQueue.add(rootNode);
        Queue<Node> newNodeQueue = new LinkedList<>();
        int treeDepthCounter = 0;
        while (nodeQueue.size() != 0) {
            Node currentNode = nodeQueue.poll();
            drawHelper(currentNode, treeDepthCounter);
            Node rightNode = currentNode.getRightNode();
            if (rightNode != null) newNodeQueue.add(rightNode);
            Node leftNode = currentNode.getLeftNode();
            if (leftNode != null) newNodeQueue.add(leftNode);
            if (nodeQueue.size() == 0) {
                nodeQueue = newNodeQueue;
                newNodeQueue = new LinkedList<>();
                ++treeDepthCounter;
            }
        }
    }
    
    private void drawHelper(Node currentNode, int treeDepthCounter) {
        Point2D point = currentNode.getPoint();
        drawPoint(currentNode);
        
        double lowerBoundary;
        double upperBoundary;
        double x0;
        double y0;
        double x1;
        double y1;
        
        int parity;
        if (treeDepthCounter % 2 == 0) {
            parity = 0;
            // to every node belongs a rectangle
            // the line has to be drawn within the boundaries of that rectangle
            // in case the first level of the tree, that boundary is the ymin ymax coordinates
            lowerBoundary = currentNode.getRectangle().ymin();
            upperBoundary = currentNode.getRectangle().ymax();
            x0 = point.x();
            x1 = point.x();
            y0 = lowerBoundary;
            y1 = upperBoundary;
            
        } else {
            parity = 1;
            lowerBoundary = currentNode.getRectangle().xmin();
            upperBoundary = currentNode.getRectangle().xmax();
            x0 = lowerBoundary;
            x1 = upperBoundary;
            y0 = point.y();
            y1 = point.y();
            
        }
        drawSplittingLines(parity, x0, y0, x1, y1);
    }
    
    private void drawPoint(Node currentNode) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(currentNode.getPoint().x(), currentNode.getPoint().y());
    }
    
    private void drawSplittingLines(int parity, double x0, double y0, double x1, double y1) {
        if (parity == 0) {
            StdDraw.setPenColor(StdDraw.RED);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
        }
        StdDraw.line(x0, y0, x1, y1);
    }
    
    public Iterable<Point2D> range(RectHV rectangle) {
        checkInputData(rectangle);
        // first take the x coordinates into account, whether the given point in the tree
        // is between the points
        // then at the next level the y coordinates then x then y so on..
		Queue<Node> nodesQueue = new LinkedList<>();
		nodesQueue.add(rootNode);
		List<Point2D> pointsWithinRectangle = new ArrayList<>();
        if (rootNode == null) {
            return null;
        }
        else {
			while (nodesQueue.size() != 0)
			{
				Node currentNode = nodesQueue.poll();
				if (currentNode != null && currentNode.getRectangle().intersects(rectangle))
				{
					if (rectangle.contains(currentNode.getPoint()))
					{
						pointsWithinRectangle.add(currentNode.getPoint());
					}

					nodesQueue.add(currentNode.leftNode);
					nodesQueue.add(currentNode.rightNode);
				}
			}
        }
		return pointsWithinRectangle;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void insert(Point2D point) {
        // if such point exists update, otherwise insert a new node
        checkInputData(point);
        if (rootNode == null) {
            rootNode = new Node(point);
            RectHV rectangle = new RectHV(0, 0, 1, 1);
			rootNode.setRectangle(rectangle);
			this.size = 1;
            return;
        }
        
        // creating rectangle: take into consideration the 2 or 4 parent node?
        // keep information about the rectangle youre in
        Node currentNode = rootNode;
        Point2D currentPoint = rootNode.getPoint();
        int treeDepthCounter = 0;
        Comparator<Point2D> comparator;
        while (true) {
            // it might not needs to be updated
            if (point.equals(currentPoint)) {
                // currentNode.setPoint(point);
                return;
            }
            if ((treeDepthCounter++ % 2) == 0) {
                comparator = point.X_ORDER;
            } 
            else {
                comparator = point.Y_ORDER;
            }
            
            int comparisonResult = comparator.compare(point, currentPoint);
			RectHV previousRectangle = currentNode.getRectangle();
            if (comparisonResult == -1) {
                if (currentNode.getLeftNode() != null) {
                    currentNode = currentNode.getLeftNode();
                    currentPoint = currentNode.getPoint();
                }
                else {
					Node newNode = new Node(point);
					RectHV rectangle;
					if (comparator.getClass() == point.X_ORDER.getClass())
					{
						rectangle = new RectHV(previousRectangle.xmin(), previousRectangle.ymin(), currentPoint.x(), previousRectangle.ymax());
					}
					else
					{
						rectangle = new RectHV(previousRectangle.xmin(), previousRectangle.ymin(), previousRectangle.xmax(), currentPoint.y());
					}
					newNode.setRectangle(rectangle);
					currentNode.setLeftNode(newNode);
                    break;
                }
            }
            else if (comparisonResult == 0 || comparisonResult == 1) {
                if (currentNode.getRightNode() != null) {
                    currentNode = currentNode.getRightNode();
                    currentPoint = currentNode.getPoint();
                }
                else {
					Node newNode = new Node(point);
					RectHV rectangle;
					if (comparator.getClass() == point.X_ORDER.getClass())
					{
						rectangle = new RectHV(currentPoint.x(), previousRectangle.ymin(), previousRectangle.xmax(), previousRectangle.ymax());
					}
					else
					{
						rectangle = new RectHV(previousRectangle.xmin(), currentPoint.y(), previousRectangle.xmax(), previousRectangle.ymax());
					}
					newNode.setRectangle(rectangle);
					currentNode.setRightNode(newNode);
                    break;
                }
            }
        }
        ++size;
    }
    
    public boolean contains(Point2D point) {
        checkInputData(point);
        Node currentNode = rootNode;
        int treeDepthCounter = 0;
        Comparator<Point2D> comparator;
        while (currentNode != null) {
            if (point.equals(currentNode.getPoint())) {
                return true;
            }
            if ((treeDepthCounter % 2) == 0) {
                comparator = point.X_ORDER;
            } 
            else {
                comparator = point.Y_ORDER;
            }
            
            int comparisonResult = comparator.compare(point, currentNode.getPoint());
            if (comparisonResult == -1) {
                currentNode = currentNode.getLeftNode();
            }
            else if (comparisonResult == 0 || comparisonResult == 1) {
                currentNode = currentNode.getRightNode();
            }
            ++treeDepthCounter;
        }
        return false;
    }
    
    
    private void checkInputData(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }
    
    private static class Node {
        private Point2D point;
        private RectHV rectangle;
        private Node leftNode;
        private Node rightNode;
        
        public Node() {
            
        }
        public Node(Point2D point) {
            this.point = point;
        }
        
        public Point2D getPoint() {
            return point;
        }
        public void setPoint(Point2D point) {
            this.point = point;
        }
        public RectHV getRectangle() {
            return rectangle;
        }
        public void setRectangle(RectHV rectangle) {
            this.rectangle = rectangle;
        }
        public Node getLeftNode() {
            return leftNode;
        }
        public void setLeftNode(Node leftNode) {
            this.leftNode = leftNode;
        }
        public Node getRightNode() {
            return rightNode;
        }
        public void setRightNode(Node rightNode) {
            this.rightNode = rightNode;
        }
    }

}
