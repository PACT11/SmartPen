
package remote.messages;

import remote.server.ServerClient;
import remote.RemotePen;

/*
 * A message containing an answer to a connection request
 */
public class ConnectionAnswer extends Message {
    public static final short ACCEPT = 1;
    public static final short DECLINE = 2;
    public static final short TARGETNOTFOUND = 3;
    
    private short answer;
    
    public ConnectionAnswer(short acceptConnection) {
        this.answer = acceptConnection;
    }
    @Override
    public void onServerReceive(ServerClient client) {
        // on retransmet ce message au client connecté
        if(client.getConnection()!=null)
            client.getConnection().sendMessage(this, client);
        // si la connexion n'est pas acceptée, on détruit la connexion
        if(answer==DECLINE)
            client.getConnection().disconnectAll();
    }

    @Override
    public void onClientReceive(RemotePen client) {
        if(client.getConnectionListener()!=null)
            client.getConnectionListener().connectionAnswer(answer);
    }
    
}
