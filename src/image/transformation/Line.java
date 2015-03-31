package image.transformation;

import java.awt.Point;

/**
 * Simple definition a geometrical line, with special handling of horizontal or vertical lines.
 */
public class Line
{
  // A line can be defined by the equation y = a * x + b
  public double a, b;
  public Line(float pa, float pb) { a = pa; b = pb; } // Base constructor
  public Line(Point p1, Point p2) { define(p1, p2); } // Defined from two points

  /** Defines this line from two points where the line passes to. */
  public void define(Point p1, Point p2)
  {
    // a can be infinte
    a = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());

    if (isVertical()) // a is infinte
    {
      // The two points are on the same x coordinate.
      // Keep it in b (and keep a infinite to make the line as vertical)
      b = p1.getX();
    }
    else
    {
      // y = a * x + b  =>  b = y - a * x
      b = p1.getY() - a * p1.getX();
    }
  }

  public double getX(double y)
  {
    if (isVertical()) // a is infinite
      return b;
    return (y - b) / a;
  }
  public double getY(double x)
  {
    return a * x + b;
  }
  
  /**
   * Horizontal lines might need special code to draw them,
   * and some special handling.
   * This provides a shortcut to test this case.
   */
  public boolean isHorizontal()
  {
    return a == 0;
  }
  /**
   * Vertical lines might need special code to draw them,
   * and some special handling.
   * This provides a shortcut to test this case.
   */
  public boolean isVertical()
  {
    return Float.isInfinite((float) a);
  }
  
}