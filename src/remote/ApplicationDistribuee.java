
package remote;

import java.net.UnknownHostException;
import netzwerk.listeners.ConnectionListener;
import netzwerk.messages.BlockingMessage;
import netzwerk.messages.Message;
import view.Bitmap;

/*
    cet interface présente les fonctions du module application distribuée 
    rassemblée en un même objet.
 */
public interface ApplicationDistribuee {
    // get/set pour le nom de l'utilisateur
    public void setUID(String UID);
    public String getUID();
   
    /* connecter la socket TCP au serveur distant.*/
    public void connect(byte[] serverIp, int port) throws UnknownHostException;
    
    /*envoyer une requête de connection à l'utilisateur spécifié*/
    public void connectToUser(String targetUID);
    
    // envoie d'un message au client représenté par cet objet
    public void sendMessage(Message message);
    
    // send a message and wait for the answer (returned message has to be the same class)
    public BlockingMessage sendBlockingMessage(BlockingMessage message);
    
     /*envoie un paquet connnectionClosure a l'utilisateur distant contenant 
    l'UID local pour se deconnecter*/
    public void disconnectFromUser();
    
     /* accepter ou non la requete de connection recu*/
    public void acceptConnection(boolean isAccepted);
    
     // getters and setters :
    public void setConnectionListener(ConnectionListener listener) ;
    
    public ConnectionListener getConnectionListener();
    
    // obtenir la liste des utilisateurs
    public String[] getUserList();
    
    // envoyer une image
    public void sendImage(Bitmap Image);
    // envoyer du texte
    public void sendCommand(String command);
    // verifier qu'un utilisateur est enregistré sur le serveur
    public boolean isRegistered(String UID, String password);
    
    // envoie d'une requete de mise à jour de la liste des utilisateur à tout les autres
    public void sendUserlistUpdateRequest();
    
    // #############   CALCULS DISTRIBUES ###############
    
    public void calibrateScreen(Bitmap image);
    public void straightenAndSend(Bitmap image, int width, int height, boolean sendBack);
    public void fitAndDisplay(Bitmap image, int width, int height);
    
    public void close();
}
