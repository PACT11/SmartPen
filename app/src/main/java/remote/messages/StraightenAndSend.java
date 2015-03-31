
package remote.messages;

import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import android.graphics.Bitmap;

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
        this.img = Image.getBytesFromBitmap(image);
        this.height=height;
        this.width=width;
    }
    @Override
    public void onServerReceive(ServerClient client) {
        //...
    }

    @Override
    public void onClientReceive(Connector client) {
        if(errorListener!=null)
            errorListener.run();
    }
    public static void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
}
