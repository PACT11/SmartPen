
package remote.messages;

import java.io.*;
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
    static final long serialVersionUID= 4298568912354L;
    private static Runnable errorListener;
    
    private int width;
    private int height;
    private byte[] img;
    
    public StraightenAndSend(Bitmap image, int width, int height) {
        // ...
        this.height=height;
        this.width=width;
    }
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            StraightenAndSendTask task = new StraightenAndSendTask(client,
                    ImageIO.read(in),width,height);
            task.start();
        } catch (IOException ex) {
            System.err.println("Transform and send : error while reading image");
            System.err.println(ex);
        }
 
    }

    @Override
    public void onClientReceive(Connector client) {
        if(errorListener!=null)
            errorListener.run();
    }
    public void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
}
