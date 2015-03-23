package remote.messages;

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