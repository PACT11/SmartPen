
package remote;

import image.transformation.RansacCache;
import netzwerk.Server;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import remote.messages.CheckUser;

/**
 * Run the server on port 2323
 */
public class ServerMain {
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws IOException {
        checkInstalled();
        // start server
        Server server = new Server(2323);
        server.start();
        System.out.println("Server running at address " + server.getLocalIP()
                    + " on port " + server.getPort());
        
        //IPsync.runDaemon();   // send IP to allow clients to retrieve it
        
        RansacCache.init(); 
        
        System.out.println("enter 'q' to stop the server");
        while(System.in.read()!='q');
        // close the server
        server.close();
    }
    public static void checkInstalled() {
        try {
            new FileReader(CheckUser.usersFile);
        } catch(FileNotFoundException ex) {
            System.err.println("User database not found. Please create " + CheckUser.usersFile);
            System.err.println("users should be added in the format 'USER PASSWORD'" );
        }
    }
}
