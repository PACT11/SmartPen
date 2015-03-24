
package remote.messages;

import remote.RemotePen;

/*
 * Message qui represente une image
 */
public class Image extends ServerPassiveMessage {
    
    @Override
    public void onClientReceive(RemotePen client) {
        client.onImageReceive();
    }
}
