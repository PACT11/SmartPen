
package remote.server;

import java.io.IOException;
import java.net.*;
import remote.ipSync.IPsync;

public class Server {
    public final static int port = 2323;
    public static boolean closeRequest = false;
    
    private static ConnectionMaker maker;
    private static ServerSocket connectionSocket;
    @SuppressWarnings("empty-statement")
    public static void main(String args[]) {
        try {
            connectionSocket = new ServerSocket(port);
            connectionSocket.setPerformancePreferences(7, 2, 1);
            maker = new ConnectionMaker(connectionSocket);
            maker.start();
            
            System.out.println("Server running at address " + IPtoString(InetAddress.getLocalHost())
                    + " on port " + port);
            
            IPsync.runDaemon();   // send IP to allow clients to retrieve it
            
            System.out.println("enter 'q' to stop the server");      
            while(System.in.read()!='q');
            
            shutdown();
        } catch (IOException ex) {
            System.err.println("Server : failed in creating connection socket");
            System.err.println(ex);
        }
        
    }
    public static String IPtoString(InetAddress inet) {
        byte ip[] = inet.getAddress();
        return (ip[0]&0xFF) + "." + (ip[1]&0xFF) + "." + (ip[2]&0xFF) + "." + (ip[3]&0xFF);
    }
    public static void shutdown() throws IOException {
        ServerClient.closeAll();
        maker.close();
        connectionSocket.close();
    }
}
