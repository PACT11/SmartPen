
package remote.messages;

import remote.RemotePen;
import remote.server.ServerClient;

/*
 */
public class UID extends Message {
    private String UID;
    
    public UID(String UID) {
        this.UID = UID;
    }
    @Override
    public void onServerReceive(ServerClient client) {
        client.setUID(UID);
    }

    @Override
    public void onClientReceive(RemotePen client) {

    }
}
