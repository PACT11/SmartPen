
package Remote;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author arnaud
 */
public class Client {
    // ##### Static #####
    private static ArrayList<Client> clients = new ArrayList<>();
    
    public static void add(Socket newClientSocket) {
        System.out.println("Server : added new client !");
        clients.add(new Client(newClientSocket));
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
    }
    
    void onReceive(char data) {
        System.out.println(data);
    }
}
