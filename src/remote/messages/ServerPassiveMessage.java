package remote.messages;

import remote.server.Client;

/*
 * un message qui est juste retransmis au client connecté sans traitement de la part du serveur
 */
public abstract class ServerPassiveMessage extends Message {
    // on se contente de renvoyer le message aux clients connectés
    @Override
    public void onServerReceive(Client client) {
        if(client.getConnection()!=null)
            client.getConnection().sendMessage(this, client);
    }
}

