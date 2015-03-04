
package remote.messages;

import remote.RemotePen;
import view.Bitmap;

/*
 * Message qui represente une image
 */
public class Image extends ServerPassiveMessage {
	private byte[] image;
	
	public Image(byte[] image)
	{
		this.image=image;
	}
	
	
    
    @Override
    public void onClientReceive(RemotePen client) {
        client.onImageReceive(image);
    }
}
