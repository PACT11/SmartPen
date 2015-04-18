
package remote.messages;

import java.io.*;
import javax.imageio.ImageIO;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.tasks.CalibrateTask;
import view.Bitmap;

/**
 *
 * @author arnaud
 */
public class CalibrateScreen  extends Message {    
    static final long serialVersionUID= 4298568912355L;
    
    private byte[] img;

    
    public CalibrateScreen(Bitmap image) {
        // ...
    }
    
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            CalibrateTask task = new CalibrateTask(client,ImageIO.read(in));
            task.start();
        } catch (IOException ex) {
            System.err.println("Transform and send : error while reading image");
            System.err.println(ex);
        }
    }

    @Override
    public void onClientReceive(Connector client) {
        
    }
}
