import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Defines fitting algorithm over a number of points.
 */
public interface FittingInterface
{
  /** Gives the number of points needed to apply the fitting algorithm. */
  int getNeededPointNb();
  /** Gives the number of points fitting enough to indicate the model is good. */
  int getCloseDataNb();
  /** Finds a line fitting to this set of points, according to this fitting algorithm. */
  Line2D.Double estimateModel(ArrayList<Point> points);
  /** Estimate the error between the given model and the given point. */
  double estimateError(Point point, Line2D model);
}