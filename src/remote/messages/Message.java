/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.messages;

import java.io.Serializable;
import remote.server.Client;
import remote.RemotePen;

/**
 *
 * @author arnaud
 */
public abstract class Message implements Serializable {
    // appelé quand le serveur a recu un message
    public abstract void onServerReceive(Client client);
    // appelé quand le client a recu un message
    public abstract void onClientReceive(RemotePen client);
}
