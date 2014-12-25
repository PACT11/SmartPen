
package Remote;

import java.io.IOException;
import java.net.*;

public class Server {
    public final static int port = 2323;
    
    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        try {
            ServerSocket connectionSocket = new ServerSocket(port);
            connectionSocket.setPerformancePreferences(7, 2, 1);
            ConnectionMaker maker = new ConnectionMaker(connectionSocket);
            maker.start();
            
            System.out.println("Server running at address " + IPtoString(InetAddress.getLocalHost())
                    + " on port " + port);
            System.out.println("enter 'q' to stop the server");
            while(System.in.read()!='q');
            
            maker.close();
            connectionSocket.close();
            
        } catch (IOException ex) {
            System.err.println("Server : failed in creating connection socket");
            System.err.println(ex);
        }
        
    }
    public static String IPtoString(InetAddress inet) {
        byte ip[] = inet.getAddress();
        return (ip[0]&0xFF) + "." + (ip[1]&0xFF) + "." + (ip[2]&0xFF) + "." + (ip[3]&0xFF);
    }
}
