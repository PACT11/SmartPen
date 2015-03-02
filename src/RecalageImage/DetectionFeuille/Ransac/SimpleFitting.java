import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * A simple implementation, just taking the line going through two points.
 */
public class SimpleFitting implements FittingInterface
{
  public int getNeededPointNb() { return 2; }
  public int getCloseDataNb() { return 5; }

  public Line2D.Double estimateModel(ArrayList<Point> points)
  {
    assert points.size() == getNeededPointNb() : "You should provide the number of points required by the fitting algorithm";
    Point p1 = points.get(0);
    Point p2 = points.get(1);
    return new Line2D.Double(p1, p2);
  }

  public double estimateError(Point point, Line2D model)
  {
    // return Projection of the point to the line following the vertical axis
    return model.ptLineDist(point);
  }
}