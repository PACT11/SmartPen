package remote.messages;

import java.lang.String;

/**
 *
 * @author fatimata
 */
public class CheckUserMessage extends Message  {

    private String sourceUID;
    private String targetUID;

    public ConnectionRequest(String sourceUID, String targetUID) {
        this.targetUID = targetUID;
        this.sourceUID = sourceUID;
    }

    // appelé quand le serveur a recu un message
    public abstract void onServerReceive(ServerClient client){
        if(ServerClient.getClient(sourceUID)!=null) {          // si il est bien connecté, on crée une nouvelle connection (que des connections à deux pour l'instant)
           client.sendMessage(new ConnectionAnswer(ConnectionAnswer.USERPRESENT));
        } else {                                         // sinon on répond que le destinataire spécifié n'a pas été trouvé
            client.sendMessage(new ConnectionAnswer(ConnectionAnswer.TARGETNOTFOUND));
        }
    }

    }
    // appelé quand le client a recu un message
    public abstract void onClientReceive(RemotePen client){

    }
}
