import javafx.scene.Scene;
//test
import java.util.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.geometry.Point2D;

/** Drag the anchors around to change a polygon's points. */
public class AI_PROJ1 extends Application {
  public static void main(String[] args) throws Exception { launch(args); }

  // main application layout logic.
  @Override public void start(final Stage stage) throws Exception {
    Polygon triangle = createStartingTriangle();
    Polygon triangle1 = createSecondTriangle();
    Group root = new Group();
    
    root.getChildren().add(triangle);
    root.getChildren().addAll(createControlAnchorsFor(triangle.getPoints()));
    root.getChildren().add(triangle1);
    root.getChildren().addAll(createControlAnchorsFor(triangle1.getPoints()));
     //= new virtualToRealPolygon(triangle.getPoints());
    //List<Double> vtriangletestlist = virtualToRealPolygon(triangle.getPoints().toList().toBlocking().single());
    Polygon vtriangle = new Polygon();
    vtriangle.getPoints().setAll(getConvexHull(virtualToRealPolygon(triangle.getPoints())));
    vtriangle.setStroke(Color.BLACK);
    vtriangle.setStrokeWidth(1);
    root.getChildren().addAll(vtriangle);
    //root.getChildren().addAll(createControlAnchorsFor(vtriangle.getPoints()));
    stage.setTitle("AI_PROJ1");
    stage.setScene(
        new Scene(
            root,
            400, 400, Color.ALICEBLUE
        )
    );
    stage.show();
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
        pnts.add(myPoints[j].getX());
        pnts.add(myPoints[j].getY());
    }
    return pnts;
  }
  //}
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
  


  // creates a triangle.
  private Polygon createStartingTriangle() {
    Polygon triangle = new Polygon();

    triangle.getPoints().setAll(
        50d, 50d,
        150d, 50d,
        150d, 150d,
        50d, 150d
    );

    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
  }
  private ObservableList<Double> virtualToRealPolygon(final ObservableList<Double> rpoints){
   ObservableList<Double> vpoints = FXCollections.observableArrayList();
    for(int i = 0; i < rpoints.size(); i+= 2){
       // vpoints.add(rpoints.get(i));
       // vpoints.add(rpoints.get(i+1));
        vpoints.add(rpoints.get(i) + 100.0);
        vpoints.add(rpoints.get(i+1) + 100.0);
    }
    for(int j = 0; j < rpoints.size(); j+= 2){
        vpoints.add(rpoints.get(j));
        vpoints.add(rpoints.get(j+1));
    }
    return vpoints;
    //Polygon triangle = new Polygon();
    //triangle.getPoints().setAll(vpoints);
    //return triangle;
  
  }
  private Polygon createSecondTriangle() {
    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        400d, 400d,
        400d, 425d,
        425d, 425d,
        600d, 600d
    );

    triangle.setStroke(Color.BLUE);
    triangle.setStrokeWidth(4);
    triangle.setStrokeLineCap(StrokeLineCap.ROUND);
    triangle.setFill(Color.BLUE);
    return triangle;
  }

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

      anchors.add(new Anchor(Color.BLACK, xProperty, yProperty));
    }

    return anchors;
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {
    private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color);
      setStroke(color);
      setStrokeWidth(3);
      setStrokeType(StrokeType.OUTSIDE);

      this.x = x;
      this.y = y;

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }
          double newY = mouseEvent.getY() + dragDelta.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }
}
