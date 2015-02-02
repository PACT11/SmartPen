
package remote.messages;

import remote.server.Client;
import remote.RemotePen;

/*
 */
public class UserList extends Message {
    private String[] users;
    
    @Override
    public void onServerReceive(Client client) {
        users = Client.getUserList();
        client.sendMessage(this);
    }

    @Override
    public void onClientReceive(RemotePen client) {

    }
}
