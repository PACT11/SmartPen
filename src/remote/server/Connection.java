
package remote.server;

import java.util.ArrayList;
import remote.messages.*;

/*
 *  Cette classe permet le passage automatique d'information d'un client aux autres.
 *  Pour l'instant, on ne peut que faire des connections à deux même si l'architecture
 *  est prévue pour pouvoir plus facilement étendre à plusieurs
 */
public class Connection {
    
    private ArrayList<Client> members;
    
    // envoyer un message a tous les membres connectés (excepté l'envoyeur)
    public void sendMessage(Message message, Client sender) {
        for(Client target : members) {
            if(!target.equals(sender))
                target.sendMessage(message);
        }
    }
    public void addMember(Client newMember) {
        members.add(newMember);
        newMember.setConnection(this);
    }
    public void close(Client sender) {
        sendMessage(new ConnectionClosure(sender.getUID()), sender);
        
    }
    // enlever toutes les références vers cette connection (qui est alors détruite si le garbage collector fait son boulot)
    public void disconnectAll() {
        for(Client member : members)
            member.setConnection(null);
    }
}
