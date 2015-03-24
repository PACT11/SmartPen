package remote.messages;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import netzwerk.messages.ServerPassiveMessage;
import netzwerk.Connector;
import view.Bitmap;

/*
 * Message qui represente une image
 */
public class Image extends ServerPassiveMessage {
    private static remote.listeners.ImageReceiveListener listener;
    private byte[] image;
	
    public Image(Bitmap image) {
        byte[] imageBuff=null;
        // ...
	this.image=imageBuff; 
    }
    public Image(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            this.image = out.toByteArray();
            out.close();
        } catch (IOException ex) {
            System.err.println("Image : error while converting image");
            System.err.println(ex);
        }
    }
    @Override
    public void onClientReceive(Connector client) {
        if(listener!=null) {
            
            // ...
            
            listener.imageReceived(null);
        }
    }

    public static void setListener(remote.listeners.ImageReceiveListener listener) {
        Image.listener = listener;
    }
}