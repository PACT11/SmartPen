
package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*Fichier de test du serveur. Simule un client qui se connecte au serveur 
 */
public class ClientTest {
    public static final byte serverIP[] = {(byte)10,(byte)0,(byte)1,(byte)4};
    public static final int port = 2324;
    
    public static void main(String args[]) {
        Client client;
        Socket clientSocket;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        
        try {
            clientSocket = new Socket(InetAddress.getByAddress(serverIP),port);
            client = new Client(clientSocket);
            client.addDataListener(new DataListener() {
                @Override
                public void dataReceived(int data) {
                    onReceive(data);
                }
            });
            client.manual();
            
            Packet packet = new Packet(Header.UID);
            System.out.println("enter UID :");
            packet.setMessage(reader.readLine());
            client.sendPacket(packet);
            System.out.println("here are other connected users :");
            packet = new Packet(Header.userList);
            client.sendPacket(packet);
            
            System.out.println("enter 'q' to stop the client");
            System.out.println("enter any other character to send a packet");
            while(reader.read()!='q') {
                System.out.println("Choose the type of packet :");
                System.out.println("command: c\nimage: i\nconnectionRequest: r\n" +
                    "connectionAnswer: a\nconnectionClosure: x\nuserList: u\nUID : n");
                packet = new Packet(Header.fromChar(reader.read()));
                reader.skip(1);
                System.out.println("enter packet content :");
                packet.setMessage(reader.readLine());
                client.sendPacket(packet);
                System.out.println("packet sent");
            }
            reader.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void onReceive(int data) {
        System.out.print((char) data);
    }
}
