package remote.messages;

import netzwerk.messages.ServerPassiveMessage;
import netzwerk.Connector;
import view.BufferedImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/*
 * Message qui represente une image
 */
public class Image extends ServerPassiveMessage {
    static final long serialVersionUID = 8725811605437849635L;

    private static remote.listeners.ImageReceiveListener listener;
    private byte[] image;
	
    public Image(Bitmap image) {
        this.image = getBytesFromBitmap(image);
    }
	public Image(BufferedImage image) {

    }
    @Override
    public void onClientReceive(Connector client) {
        if(listener!=null) {
            Bitmap imgrecue = BitmapFactory.decodeByteArray(image, 0, image.length);
            listener.imageReceived(imgrecue);
        }
    }

    public static void setListener(remote.listeners.ImageReceiveListener listener) {
        Image.listener = listener;
    }
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}