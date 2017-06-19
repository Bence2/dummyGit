import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node rootNode;
    
    public static void main(String[] args) {
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
        Point2D pointNemLetezo = new Point2D(0.1, 0.1);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        assert(kdtree.contains(point3));
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
        System.out.println("vege");
    }
    
    public void draw() {
        // depth first search-csel proba
        Stack<Node> nodeStack = new Stack<>();
        nodeStack.push(rootNode);
        while (nodeStack.size() != 0) {
            Node currentNode = nodeStack.pop();
            Node rightNode = currentNode.getRightNode();
            nodeStack.push(rightNode);
            Node leftNode = currentNode.getLeftNode();
            nodeStack.push(leftNode);
        }
    }
    
    public Iterable<Point2D> range(RectHV rectangle) {
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
    
    public void insert(Point2D point) {
        // if such point exists update, otherwise insert a new node
        checkInputData(point);
        if (rootNode == null) {
            rootNode = new Node(point);
            RectHV rectangle = new RectHV(0, 0, 1, 1);
			rootNode.setRectangle(rectangle);
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
                currentNode.setPoint(point);
                break;
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
