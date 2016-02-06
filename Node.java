import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Node {

    private Node parent;
    private ArrayList<Node> neighbors;
    private double x;
    private double y;
    private Object data;

    public Node() {
        neighbors = new ArrayList<Node>();
        data = new Object();
    }

    public Node(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Node(Node parent) {
        this();
        this.parent = parent;
    }

    public Node(Node parent, double x, double y) {
        this();
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Node node) {
        this.neighbors.add(node);
    }

    public void addNeighbors(Node... node) {
        this.neighbors.addAll(Arrays.asList(node));
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean equals(Node n) {
        return this.x == n.x && this.y == n.y;
    }
}