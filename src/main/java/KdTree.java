import java.util.Comparator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node rootNode;
    
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.2, 0.5);
        Point2D point2 = new Point2D(0.1, 0.5);
        Point2D point3 = new Point2D(0.1, 0.7);
        Point2D pointNemLetezo = new Point2D(0.1, 0.1);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        System.out.println(kdtree.contains(point3));
        System.out.println(kdtree.contains(pointNemLetezo));
        System.out.println("vege");
    }
    
    public void insert(Point2D point) {
        // if such point exists update, otherwise insert a new node
        checkInputData(point);
        if (rootNode == null) {
            rootNode = new Node(point);
            return;
        }
        Node currentNode = rootNode;
        Point2D currentPoint = rootNode.getPoint();
        int treeDepthCounter = 0;
        Comparator<Point2D> comparator;
        while (true) {
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
            if (comparisonResult == -1) {
                if (currentNode.getLeftNode() != null) {
                    currentNode = currentNode.getLeftNode();
                    currentPoint = currentNode.getPoint();
                }
                else {
                    currentNode.setLeftNode(new Node(point));
                    break;
                }
            }
            else if (comparisonResult == 0 || comparisonResult == 1) {
                if (currentNode.getRightNode() != null) {
                    currentNode = currentNode.getRightNode();
                    currentPoint = currentNode.getPoint();
                }
                else {
                    currentNode.setRightNode(new Node(point));
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
