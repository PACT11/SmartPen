package remote.messages;

import netzwerk.messages.ServerPassiveMessage;
import netzwerk.Connector;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/*
 * Message qui represente une image
 */
public class Image extends ServerPassiveMessage {
    private static remote.listeners.ImageReceiveListener listener;
    private byte[] image;
	
    public Image(Bitmap image) {
        this.image = getBytesFromBitmap(image);
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
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}