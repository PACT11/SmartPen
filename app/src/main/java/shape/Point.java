package shape;

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


    public Point scalarMul(float scalar) {
        x=x*scalar;
        y=y*scalar;
        return this;
    }
    public Point add(Point pt) {
        return new Point(x+pt.x,y+pt.y);
    }
    public Point substract(Point pt) {
        return new Point(x-pt.x,y-pt.y);
    }
    public boolean isOrigin() {
        return x==0 && y==0;
    }
    public String toString()
    {
        return "Point(" + x + ", " + y + ")";
    }
}