
package remote;
import java.util.ArrayList;
import shape.ShapeProcessor;

/*
Cette classe permet le passage automatique d'information d'un client à un autre.
Elle gère aussi la fermeture et l'ouverture des connexions entre les clients.

note: dans l'état actuel, chaque client ne peut se connecter qu'a un seul autre

###### scénario d'une connexion ######
Le premier client envoie un paquet connectionRequest contenant l'UID du client visé.
Si l'UID ne correspond à aucun client pour le serveur, celui-ci renvoie un paquet
connectionAnswer contenant 'u'
Sinon, le deuxième client renvoie un paquet connectionAnswer contenant un caractère 
pour accepter/refuser la connexion. (voir Packet.java)
Si la connexion est acceptée les paquets command et image seront automatiquement
transmis à l'autre client.
 */
public class Connection {
    // ##### Static #####
    private static ArrayList<Connection> connections = new ArrayList<>(); // listes des connexions actives
    
    public static void addConnection(Client c1, Client c2) {
        connections.add(new Connection(c1,c2));
        System.out.println("Server : connection created");
    }
    
    // ##### Dynamic #####
    private Client c1;
    private Client c2;
    
    public Connection(Client c1, Client c2) {
        this.c1 = c1;
        this.c2 = c2;
        // si le client visé n'est pas connecté
        if(c2==null) {
            // on renvoie au client une réponse indiquant que le client n'existe pas
            Packet packet = new Packet(Header.connectionAnswer);
            packet.setMessage("u");
            c1.sendPacket(packet);
            // on détruit la connection
            close();
            return;
        }
        // on ajoute les listeners appelés quand un des clients recoit un paquet
        c1.addPacketListener(new PacketListener() {
            @Override
            public void packetReceived(Packet packet) {
                onC1PacketReceive(packet);
            }  
        });
        c2.addPacketListener(new PacketListener() {
            @Override
            public void packetReceived(Packet packet) {
                onC2PacketReceive(packet);
            }  
        });
        
    }
    private void onC1PacketReceive(Packet packet) {
        onPacketReceive(packet, c1, c2);
    }
    private void onC2PacketReceive(Packet packet) {
        onPacketReceive(packet, c2, c1);
    }
    private void onPacketReceive(Packet packet, Client sender, Client receiver) {
        switch(packet.getHeader()) {
            case connectionClosure:
                close();
                receiver.sendPacket(packet);
                break;
            case connectionAnswer:
                if(packet.getMessage().get(0)=='d') {
                    close();
                }
            case command:
            case image:
                receiver.sendPacket(packet);
                break;
            case connectionRequest:
                packet.setMessage(sender.getUID());
                receiver.sendPacket(packet);
        }
    }
    // appelé quand on veut détruire la connexion
    private void close() {
        c1.addPacketListener(null);
        if(c2!=null)
            c2.addPacketListener(null);
        connections.remove(this);
    }
}
