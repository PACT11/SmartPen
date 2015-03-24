package remote.messages;

import netzwerk.Connector;
import netzwerk.messages.BlockingMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import netzwerk.ServerClient;

/* 
 * Un message pour verifier qu'un utilisateur est bien enregistré sur le serveur
 */
public class CheckUser extends BlockingMessage  {
    public final static String usersFile = "/Library/Application Support/SmartPen/users";
    private String UID;
    private String password;
    private boolean validUser;

    public static boolean check(String UID, String password, Connector server) {
        return((CheckUser) server.sendBlockingMessage(new CheckUser(UID, password))).isValid();
    }
    public CheckUser(String UID, String password) {
        this.UID = UID;
        this.password = password;
    }

    // appelé quand le serveur a recu un message
    @Override
    public void onServerReceive(ServerClient client){
        // read user/password file
        validUser = isInDatabase();
        // send back the answer
        client.sendMessage(this);
    }

    private boolean isInDatabase() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(usersFile)); // open the file

            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if(line.split(" ").length == 2) {
                    String user = line.split(" ")[0];
                    String pass = line.split(" ")[1];
                    if(user.equals(UID) && pass.equals(password)) {
                        reader.close();
                        System.out.println("hey true !");
                        return true;
                    }
                }
            }
            reader.close();
        } catch(java.io.FileNotFoundException e) {
            System.err.println("RemotePen : opening error : file not found");
        } catch (java.io.IOException|java.lang.NumberFormatException e) {
            System.err.println("RemotePen : opening error : invalid file");
        }
        System.out.println("hey false !");
        return false;
    }

    public boolean isValid() {
        return validUser;
    }
}
