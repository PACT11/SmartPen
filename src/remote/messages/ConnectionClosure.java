
package remote.messages;

import remote.server.Client;
import remote.RemotePen;

/*
 * Message de fermeture de connexion
 */
public class ConnectionClosure extends Message {
    private String source;         // l'UID du client qui ferme la connexion
    
    // creer un message de fermeture de connexion 
    public ConnectionClosure(String source) {
        this.source = source;
    }
    @Override
    public void onServerReceive(Client client) {
        client.getConnection().close(client);
    }

    @Override
    public void onClientReceive(RemotePen client) {
        
    }
}
