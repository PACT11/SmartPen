
package remote;

import view.CornerCache;
import netzwerk.Server;
import java.io.*;
import java.net.URLDecoder;
import remote.messages.CheckUser;

/**
 * Run the server on port 2323
 */
public class ServerMain {
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws IOException {
        // check the users database exists
        checkInstalled();
        
        // start server
        Server server = new Server(2323);
        server.start();
        System.out.println("Server running at address " + server.getLocalIP()
                    + " on port " + server.getPort());
        
        // start corner detection cache service
        CornerCache.init(); 

        System.out.println("enter 'q' to stop the server");
        while(System.in.read()!='q');
        
        // close the server
        server.close();
    }
    public static void checkInstalled() {
        try {
            String path = ServerMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8").replace("SmartPen.jar", "") + "users";
            if(fileExists(decodedPath)) {
                CheckUser.usersFile=decodedPath;
            } else if(fileExists("src/remote/users")) {
                CheckUser.usersFile="src/remote/users";
            } else {
                System.err.println("User database not found. Please create a file named 'users' in the jar directory");
                System.err.println("users should be added in the format 'USER PASSWORD'" );
                System.exit(0);
            }
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Wrong encoding. Please make sure the path to the jar contains only UFT-8 symbols");
            System.exit(0);
        }
        
        System.out.println("Found users database at " + CheckUser.usersFile);
    }
    public static boolean fileExists(String path) {
        try {
            new FileReader(path);
            return true;
        } catch(FileNotFoundException ex) {
            return false;
        }
    }
}
