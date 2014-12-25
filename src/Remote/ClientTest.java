/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Remote;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arnaud
 */
public class ClientTest {
    public static final byte serverIP[] = {(byte)192,(byte)168,(byte)1,(byte)15};
    public static final int port = 2323;
    
    public static void main(String args[]) {
        Client client;
        Socket clientSocket;
        System.out.println("hi");
        try {
            clientSocket = new Socket(InetAddress.getByAddress(serverIP),port);
            System.out.println("socket created");
            client = new Client(clientSocket);
            System.out.println("client created");
            client.send("hi server");
            System.out.println(clientSocket.isClosed());
            System.out.println("message sent");
            System.out.println("enter 'q' to stop the server");
            while(System.in.read()!='q');
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
