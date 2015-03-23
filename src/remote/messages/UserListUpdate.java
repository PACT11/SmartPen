package remote.messages;

import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;

/**
 * A client requests to send an update of the user list to all connected clients (including himself)
 */
public class UserListUpdate extends Message {
    public interface UserListListener {
        public void onUpdate(String[] users);
    }

    private static  UserListListener listener;

    public static void setListener(UserListListener listener) {
        UserListUpdate.listener = listener;
    }

    String[] users;
    @Override
    public void onServerReceive(ServerClient client) {
        users = ServerClient.getUserList();
        for(String user : users) {
            ServerClient.getClient(user).sendMessage(this);
        }
    }

    @Override
    public void onClientReceive(Connector connector) {
        if(listener!=null)
            listener.onUpdate(users);
    }
}
