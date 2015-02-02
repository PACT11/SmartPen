
package remote.messages;

import remote.server.Client;
import remote.RemotePen;

/*
 */
public class UID extends Message {
    private String UID;
    
    public UID(String UID) {
        this.UID = UID;
    }
    @Override
    public void onServerReceive(Client client) {
        client.setUID(UID);
    }

    @Override
    public void onClientReceive(RemotePen client) {

    }
}
