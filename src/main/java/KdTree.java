import java.util.Comparator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private Node rootNode;
    
    public static void main(String[] args) {
    }
    
    public void insert(Point2D point) {
        // if such point exists update, otherwise insert a new node
        checkInputData(point);
        
        
    }
    
    public boolean contains(Point2D point) {
        checkInputData(point);
        Node currentNode = rootNode;
        Point2D currentPoint = rootNode.getPoint();
        boolean isContained = false;
        int treeDepthCounter = 0;
        Comparator<Point2D> comparator;
        while (currentNode != null) {
            if ((treeDepthCounter % 2) == 0) {
                comparator = point.X_ORDER;
            } 
            else {
                comparator = point.Y_ORDER;
            }
            
            int comparisonResult = comparator.compare(point, currentPoint);
            if (comparisonResult == -1) {
                currentNode = currentNode.getLeftNode();
                currentPoint = currentNode.getPoint();
            }
            else if (comparisonResult == 0 || comparisonResult == 1) {
                currentNode = currentNode.getRightNode();
                currentPoint = currentNode.getPoint();
            }
        }
        return isContained;
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
