
package remote.messages;

import remote.server.Connection;
import remote.server.Client;
import remote.*;

/*
 *   Message de requête de connexion 
 */
public class ConnectionRequest extends Message {
    private String targetUID;
    private String sourceUID;
    
    public ConnectionRequest(String sourceUID, String targetUID) {
        this.targetUID = targetUID;
        this.sourceUID = sourceUID;
    }
    @Override
    public void onServerReceive(Client client) {
        // essaie de trouver le destinataire
        if(Client.getClient(targetUID)!=null) {          // si il est bien connecté, on crée une nouvelle connection (que des connections à deux pour l'instant)
            Connection connection = new Connection();
            connection.addMember(client);
            connection.addMember(Client.getClient(targetUID));
        } else {                                         // sinon on répond que le destinataire spécifié n'a pas été trouvé
            client.sendMessage(new ConnectionAnswer(ConnectionAnswer.TARGETNOTFOUND));
        }
    }

    @Override
    public void onClientReceive(RemotePen client) {
        
    }
    
}
