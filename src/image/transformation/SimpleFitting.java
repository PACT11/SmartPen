import java.awt.Point;
import java.util.ArrayList;

/**
 * A simple implementation, just taking the line going through two points.
 */
public class SimpleFitting implements FittingInterface
{
  public int getNeededPointNb() { return 2; }
  public int getCloseDataNb() { return 50; }

  public Line estimateModel(ArrayList<Point> points)
  {
    assert points.size() == getNeededPointNb() : "You should provide the number of points required by the fitting algorithm";
    Point p1 = points.get(0);
    Point p2 = points.get(1);
    return new Line(p1, p2);
  }

  public double estimateError(Point point, Line model)
  {
    // Projection of the point to the line following the vertical axis
    float y = (float) (model.a * point.getX() + model.b);
    return (float) (Math.abs(point.y - y) / Math.sqrt(1 + model.a * model.a));
  }
}