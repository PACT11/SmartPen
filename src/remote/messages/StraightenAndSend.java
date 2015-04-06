
package remote.messages;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.tasks.StraightenAndSendTask;
import view.Bitmap;

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
    
    private int width;
    private int height;
    private byte[] img;
    private boolean useCache;
    private int[] cornerX;
    private int[] cornerY;
    
    public StraightenAndSend(Bitmap image, int width, int height, boolean useCache) {
        // ...
        this.useCache = useCache;
        this.height=height;
        this.width=width;
    }
    
    // a constructor for the server, sending back where the corners are
    public StraightenAndSend(ArrayList<Point> corners) {
        if(corners!=null) {
            int i=0;
            cornerX= new int[4];
            cornerY= new int[4];
        
            for(Point point : corners) {
                cornerX[i]=point.x;
                cornerY[i]=point.y;
                i++;
            }
        }
    }
    
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            StraightenAndSendTask task = new StraightenAndSendTask(client,
                    ImageIO.read(in),width,height,useCache);
            task.start();
        } catch (IOException ex) {
            System.err.println("Transform and send : error while reading image");
            System.err.println(ex);
        }
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
        }
    }
    public void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
    public void setCornerListener(CornerListener listener) {
        cornerListener = listener;
    }
}
