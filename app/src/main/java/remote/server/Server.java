
package remote.server;

import java.io.IOException;
import java.net.*;
import remote.ipSync.IPsync;

public class Server {
    public final static int port = 2323;
    public static boolean closeRequest = false;
    
    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        try {
            ServerSocket connectionSocket = new ServerSocket(port);
            connectionSocket.setPerformancePreferences(7, 2, 1);
            ConnectionMaker maker = new ConnectionMaker(connectionSocket);
            maker.start();
            
            System.out.println("Server running at address " + IPtoString(InetAddress.getLocalHost())
                    + " on port " + port);
            
            IPsync.runDaemon();   // send IP to allow clients to retrieve it\n" +
                    "            \n" +
                    "            System.out.println(\"enter 'q' to stop the server");
            while(System.in.read()!='q');
            
            ServerClient.closeAll();
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
