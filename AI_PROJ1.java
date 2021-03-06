//Made by Abrahim Ladha
//HASHEMI AI PROJECT 1
import javafx.scene.Scene;
import java.util.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.util.*;
import javafx.scene.shape.Path;
/** Drag the anchors around to change a polygon's points. */
public class AI_PROJ1 extends Application {

  public static void main(String[] args) throws Exception { launch(args); }
    static Polygon vpoly1 = new Polygon();
    static Polygon vpoly2 = new Polygon();
    static Polygon vpoly3 = new Polygon();    
    static Shape megaunion;
    static Polygon poly1 = createFirstPolygon();
    static Polygon poly2 = createSecondPolygon();
    static Polygon poly3 = createThirdPolygon();
    static Polygon error1 = new Polygon();
    static Polygon error2 = new Polygon();
    static Polygon error3 = new Polygon();
    static double a = 30.0;
    static Polygon robit = drawRobit(a);
    static Polygon goal = new Polygon();
    static ArrayList<Polyline> visibles = new ArrayList<>();
    static Group root = new Group();
    static Pane possiblepaths = new Pane();
    static PriorityQueue<Node> openList;
    static HashSet<Node> closedList;
    HashMap<Node, Double> gVals = new HashMap<>();
    HashMap<Node, Double> fVals = new HashMap<>();
    static ArrayList<Node> allnodes = new ArrayList<>();
    static Polyline sline = new Polyline();
    Button calculatebutton = new Button("CALCULATE");
    Button plusbutton = new Button("+");
    Button minusbutton = new Button("-");
    Button resetbutton = new Button("RESET");
    HBox hb = new HBox();
    VBox vb = new VBox();
    Pane box = new Pane();
    static Path path = new Path();
    static PathTransition pt = new PathTransition();
    @Override public void start(final Stage stage) throws Exception {        
    goal.getPoints().setAll(700d, 700d, 700d, 670d, 670d, 670d, 670d, 700d);
    goal.setFill(Color.RED); 
    goal.setStroke(Color.BLACK);
    box.setPrefSize(700,700); 
    plusButton handler1 = new plusButton();
    plusbutton.setOnAction(handler1);
    minusButton handler2 = new minusButton();
    minusbutton.setOnAction(handler2);
    resetButton handler3 = new resetButton();
    resetbutton.setOnAction(handler3);
    calculateButton handler4 = new calculateButton();
    calculatebutton.setOnAction(handler4);
    hb.setHgrow(plusbutton, Priority.ALWAYS);
    hb.setHgrow(minusbutton, Priority.ALWAYS);
    hb.setHgrow(calculatebutton, Priority.ALWAYS);
    hb.setHgrow(resetbutton, Priority.ALWAYS);
    plusbutton.setMaxWidth(Double.MAX_VALUE);
    minusbutton.setMaxWidth(Double.MAX_VALUE);
    resetbutton.setMaxWidth(Double.MAX_VALUE);
    calculatebutton.setMaxWidth(Double.MAX_VALUE);
    hb.getChildren().addAll(plusbutton,minusbutton,resetbutton,calculatebutton);
    vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
    vpoly1.setStroke(Color.BLACK);
    vpoly1.setStrokeWidth(1);
    vpoly1.setFill(Color.TRANSPARENT);
    vpoly1.setVisible(false);
    vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
    vpoly2.setStroke(Color.BLACK);
    vpoly2.setStrokeWidth(1);
    vpoly2.setFill(Color.TRANSPARENT);
    vpoly2.setVisible(false);
    vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
    vpoly3.setStroke(Color.BLACK);
    vpoly3.setStrokeWidth(1);
    vpoly3.setFill(Color.TRANSPARENT);
    vpoly3.setVisible(false);
    error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.99))));
    error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.99))));
    error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.99))));
    box.getChildren().addAll(vpoly1,vpoly2,vpoly3);
    box.getChildren().addAll(poly1,poly2,poly3);
    box.getChildren().addAll(robit,goal);
    box.getChildren().addAll(createControlAnchorsFor(poly1.getPoints()));
    box.getChildren().addAll(createControlAnchorsFor(poly2.getPoints()));
    box.getChildren().addAll(createControlAnchorsFor(poly3.getPoints()));
    box.getChildren().addAll(possiblepaths);
    vb.getChildren().addAll(box, hb);
    root.getChildren().addAll(vb);
    Scene scene1 = new Scene(root, 700, 725, Color.ALICEBLUE);
    scene1.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
         vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
         vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
         vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
         error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.99))));
         error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.99))));
         error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.99))));
        }
	});
    stage.setTitle("AI_PROJ1");
    stage.setScene(
    	scene1
    );
    stage.show();
  }
//plusbutton handler
class plusButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
      if(a < 75.0){
        a += 1.0;
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.99))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.99))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.99))));       
        robit.getPoints().setAll(drawRobit(a).getPoints());
        }
    }
}
//minusbutton handler
class minusButton implements EventHandler<ActionEvent> {
    @Override 
     public void handle(ActionEvent e){
        if(a > 1.1){
        a -= 1.0;
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.99))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.99))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.99))));
        robit.getPoints().setAll(drawRobit(a).getPoints());
        }
    }
}
//resetbutton handler
class resetButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
        visibles.clear();
        calculatebutton.setDisable(false);
        Pane pnew = new Pane();
        box = new VBox();
        a = 30.0;
        robit = drawRobit(a);
        poly1 = createFirstPolygon();
        poly2 = createSecondPolygon();
        poly3 = createThirdPolygon();
        box.getChildren().clear();
        root.getChildren().clear();
        hb.getChildren().clear();
        sline.getPoints().clear();
        possiblepaths.getChildren().clear();
        hb.getChildren().addAll(plusbutton,minusbutton,resetbutton,calculatebutton);
        pnew.getChildren().addAll(poly1,poly2,poly3,robit,goal);
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a)))); 
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a)))); 
        vpoly1.setVisible(false);
        vpoly2.setVisible(false);
        vpoly3.setVisible(false);
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.99))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.99))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.99))));
        pnew.getChildren().addAll(vpoly1,vpoly2);
        pnew.getChildren().addAll(vpoly3);
        pnew.getChildren().addAll(createControlAnchorsFor(poly1.getPoints()));
        pnew.getChildren().addAll(createControlAnchorsFor(poly2.getPoints()));
        pnew.getChildren().addAll(createControlAnchorsFor(poly3.getPoints()));
        box.getChildren().addAll(pnew,hb);
        root.getChildren().addAll(box);
    }
}
//calculatebutton handler
class calculateButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
        possiblepaths.getChildren().clear();
        calculatebutton.setDisable(true);
        vpoly1.setVisible(true);
        vpoly2.setVisible(true);
        vpoly3.setVisible(true);
        boolean intersecter = false;
        Shape s12 = Shape.intersect(poly1,poly2);
        root.getChildren().addAll(s12);
        
        Shape s13 = Shape.intersect(poly1,poly3);
        root.getChildren().addAll(s13);

        Shape s23 = Shape.intersect(poly2,poly3);
        root.getChildren().addAll(s23);

         
        if(s12.getLayoutBounds().getHeight() >= 0 || s12.getLayoutBounds().getWidth() >= 0){
            System.out.println("s12 fired");
            return;
        }
        else if(s13.getLayoutBounds().getHeight() >= 0 || s13.getLayoutBounds().getWidth() >= 0){
            System.out.println("s13 fired");
            return;
        }
        
        else if(s23.getLayoutBounds().getHeight() >= 0 || s23.getLayoutBounds().getWidth() >= 0){
            System.out.println("s23 fired");
            return;
        }
        //if(!intersecter)
            //return;

        
        for(int i = 0; i < vpoly2.getPoints().size(); i+=2){
            setVis(vpoly2.getPoints().get(i),vpoly2.getPoints().get(i+1),vpoly1);
            int size2 = vpoly2.getPoints().size();
            Node n = new Node(vpoly2.getPoints().get(i),vpoly2.getPoints().get(i+1));
            Node next = new Node(vpoly2.getPoints().get((i+2)%size2),vpoly2.getPoints().get((i+3)%size2));
            Node prev = new Node(vpoly2.getPoints().get((i+size2 -2)%size2),vpoly2.getPoints().get((i-1 + size2)%size2));
            if(!n.getNeighbors().contains(next))
                n.addNeighbor(next);
            if(!n.getNeighbors().contains(prev))
                n.addNeighbor(prev);
            allnodes.add(n);
        }
        for(int j = 0; j < vpoly3.getPoints().size(); j+=2){
            setVis(vpoly3.getPoints().get(j),vpoly3.getPoints().get(j+1),vpoly2);
            int size3 = vpoly3.getPoints().size();
            Node n = new Node(vpoly3.getPoints().get(j),vpoly3.getPoints().get(j+1));
            Node next = new Node(vpoly3.getPoints().get((j+2)%size3),vpoly3.getPoints().get((j+3)%size3));
            Node prev = new Node(vpoly3.getPoints().get((j+size3-2)%size3),vpoly3.getPoints().get((j+size3-1)%size3));
            if(!n.getNeighbors().contains(next))
                n.addNeighbor(next);
            if(!n.getNeighbors().contains(prev))
                n.addNeighbor(prev); 
            allnodes.add(n);
        }
        for(int k = 0; k < vpoly1.getPoints().size(); k+=2){
            setVis(vpoly1.getPoints().get(k),vpoly1.getPoints().get(k+1),vpoly3);
            int size1 = vpoly1.getPoints().size();
            Node n = new Node(vpoly1.getPoints().get(k),vpoly1.getPoints().get(k+1));
            Node next = new Node(vpoly1.getPoints().get((k+2)%size1),vpoly1.getPoints().get((k+3)%size1));
            Node prev = new Node(vpoly1.getPoints().get((k+size1-2)%size1),vpoly1.getPoints().get((k+size1-1)%size1));
            if(!n.getNeighbors().contains(next))
                n.addNeighbor(next);
            if(!n.getNeighbors().contains(prev))
                n.addNeighbor(prev);
            allnodes.add(n);
        }
        Node startnode = new Node(robit.getPoints().get(2),robit.getPoints().get(3));
        Node endnode = new Node(goal.getPoints().get(4),goal.getPoints().get(5));
        startnode.setNeighbors(whatsVisible(startnode));
        endnode.setNeighbors(whatsVisible(endnode));
        allnodes.add(startnode);
        allnodes.add(endnode);
        for(int w = 0; w < allnodes.size(); w++){
            allnodes.get(w).setNeighbors(whatsVisible(allnodes.get(w)));
        }
        for(Polyline path : visibles){
            path.setStroke(Color.LIGHTGRAY);   
            root.getChildren().add(path);
        }
        for(Node tempnode : allnodes)
            tempnode.setData(tempnode.getX() + "  " + tempnode.getY());
        traverse(endnode,startnode);
        sline.setStrokeWidth(4);
        root.getChildren().addAll(sline);
        playAnim();
    }
 }
//put all the animation stuff in one method
public void playAnim(){
    pt = new PathTransition();
    path = new Path();
    MoveTo mt = new MoveTo(robit.getPoints().get(2),robit.getPoints().get(3));
    path.getElements().add(mt);
    Polygon balance = new Polygon();
    balance.getPoints().addAll(
        robit.getPoints().get(2),robit.getPoints().get(3),
        robit.getPoints().get(2) - a,robit.getPoints().get(3),
        robit.getPoints().get(2) - 0.5*a, robit.getPoints().get(3) + 0.866*a
        );
    for(int i = 0; i < sline.getPoints().size(); i+=2){
        LineTo lt = new LineTo();
        lt.setX(sline.getPoints().get(i));
        lt.setY(sline.getPoints().get(i+1));
        path.getElements().add(lt);
    }
    pt.setDuration(Duration.seconds(8));
    pt.setPath(path);
    Group g = new Group();
    g.getChildren().addAll(balance,robit);
    balance.setFill(Color.TRANSPARENT);
    root.getChildren().addAll(g);
    pt.setNode(g);
    pt.setAutoReverse(true);
    robit.toFront();
    pt.play();
}
//literally the implementation of A*
public void traverse(Node begin, Node end) {
   openList = new PriorityQueue<Node>(1000, new fCompare());
    closedList = new HashSet<>();
     openList.clear(); 
    HashMap<Node,Node> cameFrom = new HashMap<Node,Node>();
    gVals.put(begin,0.0);
    openList.add(begin);
    while(!openList.isEmpty()) {
      Node current = openList.element();
        if (current.equals(end)) {
            begin.setParent(null);
            System.out.println("Goal Reached!");
            printPath(current);
            return;
        }
        closedList.add(openList.poll());
        HashSet<Node> neighbors = current.getNeighbors();
        for (Node neighbor : neighbors) {
            double gScore = gVals.get(current);
            double fScore = gScore + h(neighbor, end); 
            if(closedList.contains(neighbor)) {
                    gVals.put(neighbor, gScore);
                    fVals.put(neighbor, fScore);
                if(fScore >= fVals.get(neighbor)) {
                    continue;
                }
            }
            if (!openList.contains(neighbor) || fScore < fVals.get(neighbor)){
                neighbor.setParent(current);
                gVals.put(neighbor, gScore);
                fVals.put(neighbor, fScore);           
                if(!openList.contains(neighbor)) {
                    openList.add(neighbor);
                }
            }           
        }
    }
      System.out.println("FAIL");
}
public double h(Node node, Node goal) {
    double x = node.getX() - goal.getX();
    double y = node.getY()- goal.getY();
    return Math.sqrt(x*x +y*y);
}
public void printPath(Node node) {
    sline.getPoints().clear();
    sline.getPoints().add(node.getX());
    sline.getPoints().add(node.getY());
    while (node.getParent() != null) {
        node = node.getParent();
        sline.getPoints().add(node.getX());
        sline.getPoints().add(node.getY());
        System.out.println(node.getData());
    }
}
class fCompare implements Comparator<Node> {
    public int compare(Node o1, Node o2) {
        if(fVals.get(o1) < fVals.get(o2)) {
            return -1;
        }
        else if(fVals.get(o1) > fVals.get(o2)) {
        return 1;
        }
        else
            return 0;
    }
}
//For some vertex param, returns the hashset of whats visible
private static HashSet<Node> whatsVisible(Node vertex){
    HashSet<Node> childs = new HashSet<>();
    ArrayList<Polygon> triangles = new ArrayList<>();
    for(int i = 0; i < vpoly1.getPoints().size(); i+=2){
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                vertex.getX(),vertex.getY(),
                vpoly1.getPoints().get(i),
                vpoly1.getPoints().get(i+1),
                vpoly1.getPoints().get((i+2)%vpoly1.getPoints().size()),
                vpoly1.getPoints().get((i+3)%vpoly1.getPoints().size())
        );
        triangles.add(triangle);
    }
    for(int i = 0; i < vpoly2.getPoints().size(); i+=2){
       Polygon triangle = new Polygon();
       triangle.getPoints().setAll(
                vertex.getX(),vertex.getY(),
                vpoly2.getPoints().get(i),
                vpoly2.getPoints().get(i+1),
                vpoly2.getPoints().get((i+2)%vpoly2.getPoints().size()),
                vpoly2.getPoints().get((i+3)%vpoly2.getPoints().size())
        );
        triangles.add(triangle);
    }
    for(int i = 0; i < vpoly3.getPoints().size(); i+=2){
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                vertex.getX(),vertex.getY(),
                vpoly3.getPoints().get(i),
                vpoly3.getPoints().get(i+1),
                vpoly3.getPoints().get((i+2)%vpoly3.getPoints().size()),
                vpoly3.getPoints().get((i+3)%vpoly3.getPoints().size())
       );
       triangles.add(triangle);
    }
       Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                vertex.getX(),vertex.getY(),
                670d, 670d,
                670.0000001,670.0000001
                );
        triangles.add(triangle);
        triangle = new Polygon();
        triangle.getPoints().setAll(
                vertex.getX(),vertex.getY(),
                robit.getPoints().get(2),
                robit.getPoints().get(3),
                robit.getPoints().get(2) +0.0000001,
                robit.getPoints().get(3) + 0.0000001 
                );
        triangles.add(triangle);
        Shape totalError = Shape.union(error1,error2);
        totalError = Shape.union(totalError,error3);
    for(Polygon tri : triangles){   
        Shape inter = Shape.intersect(totalError,tri);
        if(inter.getLayoutBounds().getHeight() < 0 || inter.getLayoutBounds().getWidth() < 0){
            Node one = new Node(tri.getPoints().get(2),tri.getPoints().get(3));
            Node two = new Node(tri.getPoints().get(4),tri.getPoints().get(5));
            int dex1 = allnodes.indexOf(one);
            int dex2 = allnodes.indexOf(two);
            for(Node node : allnodes){
                if(one.equals(node)){
                    if(!childs.contains(node))
                        childs.add(node);
                }
                if(two.equals(node)){
                    if(!childs.contains(node))
                        childs.add(node);
                }
            }
        }
    }   
    return childs;
}
//adds cools lines to draw to visibles based on what doesnt intersect
private static void setVis(double x, double y, Polygon poly){
    for(int i = 0; i < poly.getPoints().size(); i+=2){
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                x,y,
                poly.getPoints().get(i),
                poly.getPoints().get(i+1),
                poly.getPoints().get((i+2)%poly.getPoints().size()),
                poly.getPoints().get((i+3)%poly.getPoints().size())
        );
        Shape totalError = Shape.union(error1,error2);
        totalError = Shape.union(error3,totalError);
        Shape inter = Shape.intersect(totalError,triangle);
        if(inter.getLayoutBounds().getHeight() <= 0 || inter.getLayoutBounds().getWidth() <= 0){
            Polyline one = new Polyline();
            Polyline two = new Polyline();
            one.getPoints().setAll(x,y,poly.getPoints().get(i),poly.getPoints().get(i+1));
            two.getPoints().setAll(x,y,poly.getPoints().get((i+2)%poly.getPoints().size()),
                    poly.getPoints().get((i+3)%poly.getPoints().size()));
            visibles.add(one);
            visibles.add(two);
        }
    }
}
//allows robit to change with the double value of a
private static Polygon drawRobit(double a) {
    double corner = 1.0;
   Polygon robit = new Polygon();
   robit.getPoints().setAll(
           (double)(corner + 0.5*a), corner,
           corner,(double)(corner + .866*a), 
           (double)(corner + a), (double)(corner + .866*a) 
           );
   robit.setStroke(Color.RED);
   robit.setFill(Color.YELLOW);
   return robit;
}
//creates first polygon
private static Polygon createFirstPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        280d, 110d,
        350d, 150d, 
        330d, 210d,
        300d, 280d,
        180d, 270d,
        170d, 220d,
        200d, 160d
    );
    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
}
//creates second polygon
private static Polygon createSecondPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        320d, 410d,
        390d, 440d,
        420d, 480d, 
        400d, 540d, 
        360d, 590d, 
        280d, 580d,
        250d, 520d,
        270d, 460d
    );
    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
}
//creates third polygon
  private static Polygon createThirdPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        560d, 200d,
        630d, 220d,
        650d, 300d,
        610d, 370d,
        580d, 400d,
        510d, 380d,
        460d, 340d,
        430d, 270d,
        490d, 210d
    );
    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;  
}
//sorts points to comply with javafxs polygon renderer
private ObservableList<Double> pointSorter(ObservableList<Double> s){
    double centerX = 0, centerY = 0;
    for(int i = 0; i < s.size(); i+=2){
        centerX += s.get(i);
        centerY += s.get(i+1);
    }
    centerX /= (s.size()/2);
    centerY /= (s.size()/2);
    ArrayList<Double> angles = new ArrayList<>();
    for (int j = 0; j < s.size(); j+=2){
        angles.add(Math.atan2(s.get(j+1) - centerY , s.get(j) - centerX));
    }
    for(int k = angles.size() - 1; k >= 0; k--){
        for(int w = 1; w < k; w++){
            if(angles.get(w-1) > angles.get(w)){
                double temp = angles.get(w-1);
                angles.set(w-1,angles.get(w));
                angles.set(w, temp);
                double tempx = s.get(2*(w-1));
                double tempy = s.get(2*(w-1) + 1);
                s.set(2*(w-1), s.get(2*w));
                s.set(2*(w-1)+1, s.get((2*w) + 1));
                s.set(2*w, tempx);
                s.set((2*w)+1, tempy);
            }
        }
    }
    return s;
}
//convex implementation modified slightly from liangs book
private ObservableList<Double> getConvexHull(ObservableList<Double> s){
    Point2D[] myPoints = new Point2D[s.size()/2];
    for(int i = 0; i < myPoints.length; i++)
        myPoints[i] = new Point2D(s.get(2*i),s.get((2*i)+1));
      Point2D h0 = getRightMostLowestPoint(myPoints);
      ArrayList<Point2D> H = new ArrayList<Point2D>();
      H.add(h0);
      Point2D t0 = h0;
      while (true) {
        Point2D t1 = myPoints[0];
        for(int i = 1; i < myPoints.length; i++){
            double status = whichSide(t0.getX(), t0.getY(), t1.getX(), t1.getY(), myPoints[i].getX(), myPoints[i].getY());
            if(status > 0)
                t1 = myPoints[i];
            else  if (status == 0){
                if(distance(myPoints[i].getX(), myPoints[i].getY(), t0.getX(), t0.getY()) > distance(t1.getX(), t1.getY(), t0.getX(), t0.getY()))
                    t1 = myPoints[i];
            } 
        }
      if(t1.getX() == h0.getX() && t1.getY() == h0.getY())
          break;
      else {
        H.add(t1);
        t0 = t1;
      }
   }
    ObservableList<Double> pnts = FXCollections.observableArrayList();
    for(int j = 0; j < H.size(); j++){
        pnts.add(H.get(j).getX());
        pnts.add(H.get(j).getY());
    }
    return pnts;
  }
  public double whichSide(double x0, double y0, double x1, double y1, double x2, double y2){
    return (x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0);
  }
  public double distance(double x1, double y1, double x2, double y2) {
  return Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
  }
  static Point2D getRightMostLowestPoint(Point2D[] p){
    int rightMostIndex = 0;
    double rightMostX = p[0].getX();
    double rightMostY = p[0].getY();
    for(int i = 0; i < p.length; i++){
        if(rightMostY < p[i].getY()) {
            rightMostY = p[i].getY();
            rightMostX = p[i].getX();
            rightMostIndex = i;
        }
        else if (rightMostY == p[i].getY() && rightMostX < p[i].getX()){
            rightMostX = p[i].getX();
            rightMostIndex = i;
        }     
   }
    return p[rightMostIndex];
}
//creates virtual polygon per a value and polygons observable list values. used with convex hull
private ObservableList<Double> virtualToRealPolygon(final ObservableList<Double> rpoints, double a){
   ObservableList<Double> vpoints = FXCollections.observableArrayList();
    for(int i = 0; i < rpoints.size(); i+= 2){
        vpoints.add(rpoints.get(i));
        vpoints.add(rpoints.get(i+1));
    }
    for(int j = 0; j < rpoints.size(); j+= 2){
        vpoints.add(rpoints.get(j)-(a/2));
        vpoints.add(rpoints.get(j+1)+(.866*a));
        vpoints.add(rpoints.get(j) - a);
        vpoints.add(rpoints.get(j+1));
    }
    return vpoints;
}
//returns a list of draggable anchors for polygon specified at each vertice
private ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();
    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;
        DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
        DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));
        xProperty.addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
                points.set(idx, (double) x);
            }
        });
        yProperty.addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
                points.set(idx + 1, (double) y);
            }
        });
        anchors.add(new Anchor(Color.BLUE, xProperty, yProperty));
    }
    return anchors;
  }
}
