
package remote.messages;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.listeners.ImageReceiveListener;
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
    private static ImageReceiveListener imageListener;
    
    private int width;
    private int height;
    private byte[] img;
    private boolean sendBack;
    private int[] cornerX;
    private int[] cornerY;
    
    public StraightenAndSend(Bitmap image, int width, int height, boolean sendBack) {
        // ...
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
                cornerX[i]=point.x;
                cornerY[i]=point.y;
                i++;
            }
        }
        img=null;
        if(straightImage!=null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(straightImage, "jpg", out);
                out.flush();
                this.img = out.toByteArray();
                out.close();
            } catch (IOException ex) {
                System.err.println("Image : error while converting image");
                System.err.println(ex);
            }
        }
    }
    
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            StraightenAndSendTask task = new StraightenAndSendTask(client,
                    ImageIO.read(in),width,height,sendBack);
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
            if(img!=null && imageListener !=null) {
                // ...
            }   
        }
    }
    public void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
    public void setCornerListener(CornerListener listener) {
        cornerListener = listener;
    }
    public static void setImageListener(ImageReceiveListener listener) {
        imageListener = listener;
    }
    
}
