
package remote.messages;

import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.listeners.ImageReceiveListener;
import shape.Point;
import view.BufferedImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 *
 * @author arnaud
 */
public class StraightenAndSend  extends Message {
    public interface CornerListener {
        public void cornerUpdate(ArrayList<Point> corners);
    }

    static final long serialVersionUID= 4298568912354L;
    private static Runnable errorListener;
    private static CornerListener cornerListener;
    private static ImageReceiveListener imageListener;

    private int width;
    private int height;
    private byte[] img;
    private boolean sendBack;
    private int[] cornerX;
    private int[] cornerY;

    public StraightenAndSend(Bitmap image, int width, int height, boolean sendBack) {
        this.img = Image.getBytesFromBitmap(image);
        this.sendBack = sendBack;
        this.height=height;
        this.width=width;
    }

    // a constructor for the server, sending back where the corners are
    public StraightenAndSend(ArrayList<Point> corners, BufferedImage straightImage) {
        if(corners!=null) {
            int i=0;
            cornerX= new int[4];
            cornerY= new int[4];

            for(Point point : corners) {
                cornerX[i]=(int) point.x;
                cornerY[i]=(int) point.y;
                i++;
            }
        }
        //...
    }

    @Override
    public void onServerReceive(ServerClient client) {
        // ...
    }

    @Override
    public void onClientReceive(Connector client) {
        if(cornerX==null) {
            if(errorListener!=null)
                errorListener.run();
        } else {
            ArrayList<Point> corners = new ArrayList<>();
            for(int i=0;i<4;i++) {
                corners.add(new Point(cornerX[i],cornerY[i]));
            }
            if(cornerListener!=null)
                cornerListener.cornerUpdate(corners);
            if(img!=null && imageListener !=null) {
                imageListener.imageReceived(BitmapFactory.decodeByteArray(img, 0, img.length));
            }
        }
    }
    public static void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
    public static void setCornerListener(CornerListener listener) {
        cornerListener = listener;
    }
    public static void setImageListener(ImageReceiveListener listener) {
        imageListener = listener;
    }
}
