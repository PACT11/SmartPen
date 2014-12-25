
package Remote;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author arnaud
 */
public class Client {
    // ##### Static #####
    private static ArrayList<Client> clients = new ArrayList<>();
    
    public static void add(Socket newClientSocket) {
        System.out.println("Server : added new client !");
        Client client = new Client(newClientSocket);
        clients.add(client);
        client.send("hi");
    }
    
    // ##### 
    private Socket socket;
    private PrintWriter out;
    private SmartBufferedReader in;
    
    public Client(Socket socket) {
        this.socket = socket;
        
        try {
            in = new SmartBufferedReader(new
                    InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Server : unable to create client input stream");
            System.err.println(ex);
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println("Server : unable to create client output stream");
            System.err.println(ex);
        }
        
        in.addDataListener(new DataListener() {
            @Override
            public void dataReceived(char data) {
                onReceive(data);
            }
        });
        try {
            System.out.println(in.read());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void onReceive(char data) {
        System.out.println(data);
    }
    public void send(String text) {
        out.print(text);
    }
    public void close() {
        out.flush();
        out.close();
        in.close();
        try {
            socket.setSoLinger(true, 1000);
            socket.close();
        } catch (IOException ex) {
            System.err.println("Server : problem closing the socket");
            System.err.println(ex);
        }
    }
}
