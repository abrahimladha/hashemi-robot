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

/** Drag the anchors around to change a polygon's points. */
public class AI_PROJ1 extends Application {

  public static void main(String[] args) throws Exception { launch(args); }
    static Polygon vpoly1 = new Polygon();
    static Polygon vpoly2 = new Polygon();
    static Polygon vpoly3 = new Polygon();
    
    static Polygon poly1 = createFirstPolygon();
    static Polygon poly2 = createSecondPolygon();
    static Polygon poly3 = createThirdPolygon();
    
    static Polygon error1 = new Polygon();
    static Polygon error2 = new Polygon();
    static Polygon error3 = new Polygon();

    static double a = 30.0;
    static Polygon robit = drawRobit(a);
    static Polygon goal = new Polygon();

    static ArrayList<Point2D> vertices = new ArrayList<>();
    static ArrayList<Polyline> edges = new ArrayList<>();

    static ArrayList<Polyline> visibles = new ArrayList<>();
    
    static Group root = new Group();
  
    static PriorityQueue<Node> openList;
    static ArrayList<Node> closedList;
    static HashMap<Node, Double> gVals = new HashMap<Node, Double>();
    static HashMap<Node, Double> fVals = new HashMap<Node, Double>();
    @Override public void start(final Stage stage) throws Exception {
        
        
        
        
        
        
        
        
        
        goal.getPoints().setAll(1000d, 1000d, 1000d, 950d, 950d, 950d, 950d, 1000d);
    goal.setFill(Color.RED); 
    // Group root = new Group();
    Button plusbutton = new Button("+");
    Button minusbutton = new Button("-");
    Button resetbutton = new Button("RESET");
    Button calculatebutton = new Button("CALCULATE");

    plusButton handler1 = new plusButton();
    plusbutton.setOnAction(handler1);


    minusButton handler2 = new minusButton();
    minusbutton.setOnAction(handler2);


    resetButton handler3 = new resetButton();
    resetbutton.setOnAction(handler3);
    

    calculateButton handler4 = new calculateButton();
    calculatebutton.setOnAction(handler4);


    HBox hb = new HBox();
    VBox vb = new VBox();
    Pane box = new Pane();

    hb.setHgrow(plusbutton, Priority.ALWAYS);
    hb.setHgrow(minusbutton, Priority.ALWAYS);
    hb.setHgrow(calculatebutton, Priority.ALWAYS);
    hb.setHgrow(resetbutton, Priority.ALWAYS);

    plusbutton.setMaxWidth(Double.MAX_VALUE);
    minusbutton.setMaxWidth(Double.MAX_VALUE);
    resetbutton.setMaxWidth(Double.MAX_VALUE);
    calculatebutton.setMaxWidth(Double.MAX_VALUE);
    
    hb.getChildren().addAll(plusbutton,minusbutton,resetbutton,calculatebutton);
    //box.getChildren().add(poly1);
    //box.getChildren().addAll(createControlAnchorsFor(poly1.getPoints()));
    //box.getChildren().add(poly2);
    //box.getChildren().addAll(createControlAnchorsFor(poly2.getPoints()));
    
    vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
    vpoly1.setStroke(Color.BLACK);
    vpoly1.setStrokeWidth(1);
    vpoly1.setFill(Color.TRANSPARENT);

    vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
    vpoly2.setStroke(Color.BLACK);
    vpoly2.setStrokeWidth(1);
    vpoly2.setFill(Color.TRANSPARENT);

    vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
    vpoly3.setStroke(Color.BLACK);
    vpoly3.setStrokeWidth(1);
    vpoly3.setFill(Color.TRANSPARENT);

    error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.9))));
    error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.9))));
    error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.9))));

    box.getChildren().addAll(vpoly1,vpoly2,vpoly3);
    box.getChildren().addAll(poly1,poly2,poly3);
    box.getChildren().addAll(robit,goal);
    box.getChildren().addAll(createControlAnchorsFor(poly1.getPoints()));
    box.getChildren().addAll(createControlAnchorsFor(poly2.getPoints()));
    box.getChildren().addAll(createControlAnchorsFor(poly3.getPoints()));
    
    vb.getChildren().addAll(box, hb);
    root.getChildren().addAll(vb);
    
    Scene scene1 = new Scene(root, 1080, 1080, Color.ALICEBLUE);
    //vpoly1.getPoints().setAll(pointSorter(vpoly1.getPoints()));
    //vpoly2.getPoints().setAll(pointSorter(vpoly2.getPoints()));
    //vpoly3.getPoints().setAll(pointSorter(vpoly3.getPoints()));
    //generateLists();
    System.out.println(edges.size());
    System.out.println(vertices.size());
    scene1.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
         vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
         vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
         vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
         error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.9))));
         error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.9))));
         error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.9))));
        }
	});
    stage.setTitle("AI_PROJ1");
    stage.setScene(
    	scene1
    );
    stage.show();
  }
  class plusButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
        a += 1.0;
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
        
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.9))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.9))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.9))));
        
        robit.getPoints().setAll(drawRobit(a).getPoints());
    
    }
}
  class minusButton implements EventHandler<ActionEvent> {
    @Override 
     public void handle(ActionEvent e){
        if(a > 1.1){
        a -= 1.0;
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
        
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.9))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.9))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.9))));

        robit.getPoints().setAll(drawRobit(a).getPoints());
        }
    }
  }
 class resetButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
       a = 30.0;
       poly1.getPoints().setAll(
          400d, 150d,
          500d, 200d, 
          550d, 300d,
          540d, 400d,
          480d, 480d,
          370d, 420d,
          320d, 330d,
          350d, 240d
    );
       poly2.getPoints().setAll(
          330d, 580d,
          200d, 600d,
          150d, 700d, 
          110d, 830d, 
          280d, 840d, 
          370d, 680d
     );
       poly3.getPoints().setAll(
          600d, 900d,
          730d, 850d,
          800d, 730d,
          750d, 630d,
          580d, 700d,
          500d, 800d
     );
       robit.getPoints().setAll(
          (double)(1.0 + 0.5*a), 1.0,
          1.0,(double)(1.0 + .866*a), 
          (double)(1.0 + a), (double)(1.0 + .866*a) 
     );
        vpoly1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a))));
        vpoly2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a))));
        vpoly3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a))));
        error1.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly1.getPoints(),a*0.9))));
        error2.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly2.getPoints(),a*0.9))));
        error3.getPoints().setAll(getConvexHull(pointSorter(virtualToRealPolygon(poly3.getPoints(),a*0.9))));
    }
  }

 class calculateButton implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e){
        visibles.clear();
        //generateLists();
        //generateVisibles();
        //Shape temp1 = Shape.union(vpoly1,poly1);
        for(int i = 0; i < vpoly2.getPoints().size(); i+=2)
            setVis(vpoly2.getPoints().get(i),vpoly2.getPoints().get(i+1),vpoly1);
        for(int j = 0; j < vpoly3.getPoints().size(); j+=2)
            setVis(vpoly3.getPoints().get(j),vpoly3.getPoints().get(j+1),vpoly2);
        for(int k = 0; k < vpoly3.getPoints().size(); k+=2)
            setVis(vpoly3.getPoints().get(k),vpoly3.getPoints().get(k+1),vpoly1);
        for(Polyline path : visibles){
            path.setStroke(Color.LIGHTGRAY);
            root.getChildren().addAll(path);     
        }
    }
 }

public void traverse(Node start, Node end){
    openList.clear();
    closedList.clear();
    gVals.put(start, 0.0);
    openList.add(start);

    while(!openList.isEmpty()) {
        Node current = openList.element();
        if (current.equals(end)) {
            System.out.println("Goal Reached!");
            printPath(current);
            return;
        }
        closedList.add(openList.poll());
        ArrayList<Node> neighbors = current.getNeighbors();
        for (Node neighbor : neighbors) {
            double gScore = gVals.get(current) + EuclideanDistance(neighbor.getX(),neighbor.getY(),current.getX(),current.getY());
            double fScore = gScore + h(neighbor, current);

            if(closedList.contains(neighbor)) {
                if(gVals.get(neighbor) == null) {
                    gVals.put(neighbor,fScore);
                }
                if(fVals.get(neighbor) == null) {
                    fVals.put(neighbor,fScore);
                }
                if(fScore >= fVals.get(neighbor)) {
                    continue;
                }
            }
            if (!openList.contains(neighbor) || fScore < fVals.get(neighbor)) {
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
public double EuclideanDistance(double x1, double y1, double x2, double y2){
    return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2 - y1,2));
}
private double h(Node node, Node goal) {
    double x = node.getX() - goal.getX();
    double y = node.getY()- goal.getY();
    return x*x + y*y;
}
public void printPath(Node node) {
    System.out.println(node.getData());
    while (node.getParent() != null) {
        node = node.getParent();
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
            return -1;
    }
}
private static ArrayList<Node> whatsVisible(double x, double y){
    ArrayList<Node> childs = new ArrayList<>();
    for(int i = 0; i < vpoly1.getPoints().size(); i+=2){
        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                x,y,
                vpoly1.getPoints().get(i),
                vpoly1.getPoints().get(i+1),
                vpoly1.getPoints().get((i+2)%vpoly1.getPoints().size()),
                vpoly1.getPoints().get((i+3)%vpoly1.getPoints().size())
        );
        Shape totalError = Shape.union(error1,error2);
        totalError = Shape.union(totalError,triangle);
        Shape inter = Shape.intersect(totalError,triangle);
        if(inter.getLayoutBounds().getHeight() <= 0 || inter.getLayoutBounds().getHeight() <= 0){
            Node one = new Node(vpoly1.getPoints().get(i),vpoly1.getPoints().get(i+1));
            Node two = new Node(vpoly1.getPoints().get((i+2)%vpoly1.getPoints().size()),vpoly1.getPoints().get((i+3)%vpoly1.getPoints().size()));
            if(!childs.contains(one))
                childs.add(one);
            if(!childs.contains(two))
                childs.add(two);
        }
    
    
    
    }
    return childs;
}
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
        System.out.println("before if");
        Shape totalError = Shape.union(error1,error2);
        totalError = Shape.union(error3,totalError);
        Shape inter = Shape.intersect(totalError,triangle);
        if(inter.getLayoutBounds().getHeight() <= 0 || inter.getLayoutBounds().getWidth() <= 0){
          //if(triangle.getBoundsInLocal().contains(vpoly1.getBoundsInLocal())){  
            Polyline one = new Polyline();
            Polyline two = new Polyline();
            one.getPoints().setAll(x,y,poly.getPoints().get(i),poly.getPoints().get(i+1));
            two.getPoints().setAll(x,y,poly.getPoints().get((i+2)%poly.getPoints().size()),
                    poly.getPoints().get((i+3)%poly.getPoints().size()));
            System.out.println(visibles.size());

            visibles.add(one);
            visibles.add(two);
        }
        else
            System.out.println("in else");
        
    }


}
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
   private static Polygon createFirstPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        400d, 150d,
        500d, 200d, 
        550d, 300d,
        540d, 400d,
        480d, 480d,
        370d, 420d,
        320d, 330d,
        350d, 240d
    );
    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
  }
   private static Polygon createSecondPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        330d, 580d,
        200d, 600d,
        150d, 700d, 
        110d, 830d, 
        280d, 840d, 
        370d, 680d
    );

    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
  }
  private static Polygon createThirdPolygon() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        600d, 900d, 
        730d, 850d,
        800d, 730d,
        750d, 630d,
        580d, 700d,
        500d, 800d
    );
    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;  
  } 
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
  //private ObservableList<Double> virtualToRealPolygon()

  // @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]
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

  // a draggable anchor displayed around a point.
  
}
