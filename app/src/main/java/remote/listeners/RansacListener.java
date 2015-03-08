package remote.listeners;

import java.util.ArrayList;

import shape.Point;

/**
 * Created by arnaud on 08/03/15.
 */
public interface RansacListener {
    public void ransacReceived(ArrayList<Point> points);
}
