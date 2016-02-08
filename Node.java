import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.*;
public class Node {
    private Node parent;
    private HashSet<Node> neighbors;
    private double x;
    private double y;
    private Object data;
    //empty constructor
    public Node() {
        neighbors = new HashSet<Node>();
        data = new Object();
    }
    //constructor
    public Node(double x, double y) {
        this();
        this.x = x;
        this.y = y;
    }
    //also a constructor
    public Node(Node parent) {
        this();
        this.parent = parent;
    }
    //also also a constructor
    public Node(Node parent, double x, double y) {
        this();
        this.parent = parent;
        this.x = x;
        this.y = y;
    }
    //returns the set of neighbors
    public HashSet<Node> getNeighbors() {
        return neighbors;
    }
    //replaces all neighbors with the hashset param
    public void setNeighbors(HashSet<Node> neighbors) {
        this.neighbors = neighbors;
    }
    //adds a neighbors to this nodes hashset
    public void addNeighbor(Node node) {
        this.neighbors.add(node);
    }
    //adds multiple neighbors to this nodes hashset
    public void addNeighbors(Node... node) {
        this.neighbors.addAll(Arrays.asList(node));
    }
    //returns this nodes parent
    public Node getParent() {
        return parent;
    }
    //sets the parent of this node
    public void setParent(Node parent) {
        this.parent = parent;
    }
    //returns the x coordinate of this node
    public double getX() {
        return x;
    }
    //sets the x coordinate
    public void setX(double x) {
        this.x = x;
    }
    //returns the y coordinate
    public double getY() {
        return y;
    }
    //sets the y coordinate
    public void setY(double y) {
        this.y = y;
    }
    //set the x and y coordinate at the same time
    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
    //obligatory data just to hook in
    public Object getData() {
        return data;
    }
    //changes the data
    public void setData(Object data) {
        this.data = data;
    }
    //for contains and other comparisons
    public boolean equals(Node n) {
        return this.x == n.x && this.y == n.y;
    }
}
