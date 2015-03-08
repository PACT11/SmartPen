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


    public void scalarMul(float scalar) {
        x=x*scalar;
        y=y*scalar;
    }
    public boolean isOrigin() {
        return x==0 && y==0;
    }
    public String toString()
    {
        return "Point(" + x + ", " + y + ")";
    }
}