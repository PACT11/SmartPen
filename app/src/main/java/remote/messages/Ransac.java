package remote.messages;

import java.util.ArrayList;

import remote.RemotePen;
import remote.server.ServerClient;
import shape.Point;

/**
 * Created by arnaud on 08/03/15.
 */
public class Ransac extends ServerPassiveMessage {
    float x1;
    float y1;
    float x2;
    float y2;
    float x3;
    float y3;
    float x4;
    float y4;

    public Ransac(ArrayList<Point> points) {
        x1=points.get(0).x;
        y1=points.get(0).y;
        x2=points.get(1).x;
        y2=points.get(1).y;
        x3=points.get(2).x;
        y3=points.get(2).y;
        x4=points.get(3).x;
        y4=points.get(3).y;
    }
    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(x1,y1));
        points.add(new Point(x2,y2));
        points.add(new Point(x3,y3));
        points.add(new Point(x4,y4));
        return points;
    }
    @Override
    public void onClientReceive(RemotePen client) {

    }
}
