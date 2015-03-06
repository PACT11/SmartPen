package hu.recalage;

/**
 * Created by hu on 05/03/15.
 */
/**
 * Simple definition a geometrical point.
 */
public class Point
{
    public float x, y;

    public Point(float px, float py)
    {
        x = px;
        y = py;
    }



    public String toString()
    {
        return "Point(" + x + ", " + y + ")";
    }
}