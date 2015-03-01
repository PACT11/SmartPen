package remote.messages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.String;
import remote.server.ServerClient;
import remote.RemotePen;
/**
 *
 * @author fatimata
 *
 * Un message pour verifier qu'un utilisateur est bien enregistré sur le serveur
 */
public class CheckUser extends Message  {
    private final static String usersFile = "remote/users";
    private String UID;
    private String password;
    private boolean validUser;

    public CheckUser(String UID, String password) {
        this.UID = UID;
        this.password = password;
    }

    // appelé quand le serveur a recu un message
    public void onServerReceive(ServerClient client){
        // read user/password file
        validUser = isInDatabase();
        // send back the answer
        client.sendMessage(this);
    }
    // appelé quand le client a recu un message
    public synchronized void onClientReceive(RemotePen client){
        client.notify();
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
        return false;
    }

    public boolean isValid() {
        return validUser;
    }
}
