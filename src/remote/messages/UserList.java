
package remote.messages;

import remote.RemotePen;
import remote.server.ServerClient;

/*
 */
public class UserList extends Message {
    private String[] users;
    
    @Override
    public void onServerReceive(ServerClient client) {
        users = ServerClient.getUserList();
        client.sendMessage(this);
    }

    @Override
    public synchronized void onClientReceive(RemotePen client) {
        client.setUserList(users);
        client.notify();
    }
}
