
package remote.messages;

import android.graphics.Bitmap;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;


/**
 *
 * @author arnaud
 */
public class CalibrateScreen  extends Message {    
    static final long serialVersionUID= 4298568912355L;
    
    private byte[] img;

    
    public CalibrateScreen(Bitmap image) {
        this.img = Image.getBytesFromBitmap(image);
    }
    
    @Override
    public void onServerReceive(ServerClient client) {
        // ...
    }

    @Override
    public void onClientReceive(Connector client) {
        
    }
}
