
package remote;

import java.awt.Image;

/* cet interface détaille les méthodes que doit contenir la classe RemotePen
RemotePen fourni le code pour mettre en place et faire fonctionner une socket 
TCP. Le code devra être implémenté pour être compatible avec Android.

note : on dit par exemple : un paquet userList pour dire un paquet dont le 
header contient userList.
*/

public interface IRemotePen {
    // le constructructeur doit avoir la forme : 
    //public RemotePen(String UID); 
    // avec UID : Unique IDentifier le nom unique le l'utilisateur local
    
    /* connecter la socket TCP au serveur distant. Une fois la connection 
    établie, la méthode envoie un packet UID contenant son UID.
    Ip : string de la forme "X.X.X.X" avec X des nombres de 0 a 255
    */
    public void connectToServer(String Ip);
    
    /* connecter la socket TCP à un serveur distant dont l'IP est stockée 
    dans un fichier. Une fois le fichier lu et le texte de l'IP récupéré, cette 
    méthode appelle connectToServer pour faire la connection à proprement parlé
    filename : le chemin d'accès au fichier contenant l'ip
    */
    public void connectToServerFromFile(String filename);
    
    /* obtenir la liste des utilisateurs connectés. Dans ce cas, et ce cas 
    seulement, pour des questions de simplicité, on envoie un paquet userList 
    vide et on bloque le programme jusqu'à obtenir la réponse du serveur. 
    La fonction renvoie un tableau dont la dimension est exactement le nombre 
    d'utilisateur connectés. 
    Le serveur renvoie un paquet userList qui contient les UID des utilisateurs
    connectés. Chaque UID est séparé par '\n'. Il faut convertir le tableau de
    char brut en un tableau de String séparant les UID. */
    public String[] getConnectedUsers();
    
    /* se connecter à un SmartPen distant pour pouvoir échanger des commandes et
    des images. La méthode envoie un paquet connectionRequest contenant l'UID de
    l'utilisateur visé, et met en mémoire le listener. La méthode NE DOIT PAS 
    bloquer le programme en attendant la réponse. Quand on recoit en réponse un 
    paquet connectionAnswer, on appelle listener.connectionAnswer(contenu du
    paquet converti en String) */
    public void connectToUser(String UID, UserAnswerListener listener);
    
    /* ajouter un listener, qui devra être stocké en attribut, qui sera appelé 
    si on recoit une demande de connection. Le contenu du paquet (l'UID de 
    l'utilisateur qui fait la demande) devra etre converti en string puis passé 
    en argument du listener*/
    public void addConnectionRequestListener(ConnectionRequestListener listener);
    
    /* ajoute un listener qui sera executé quand l'utilisateur distant se 
    déconnecte. Fonctionnement semblable à addConnectionRequestListener*/
    public void addConnectionClosureListener(ConnectionClosureListener listener);
    
    /* ajoute un listener qui sera executé quand l'utilisateur distant a envoyé 
    un paquet command. Fonctionnement semblable à addConnectionRequestListener*/
    public void addCommandReceiveListener(CommandReceiveListener listener);
    
    /* ajoute un listener qui sera executé quand on recoit un paquet image. Un 
    algorithme devra convertir le contenu du paquet en image puis la passer en 
    argument du listener*/
    public void addImageReceiveListener(ImageReceiveListener listener);
    
    /*envoyer un paquet command à l'utilisateur distant. command devra être 
    converti en un tableau de char pour être envoyé. Il faut gérer le cas ou 
    l'on ne soit pas connecté a un utilisateur distant en affichant un message 
    d'erreur (utiliser System.err.println) */
    public void sendCommand(String command);
    
    /* envoyer un paquet image à l'utilisateur distant contenant 'image. image devra être 
    convertie en un tableau de char puis envoyée. Il faut gérer le cas ou 
    l'on ne soit pas connecté à un utilisateur distant en affichant un message 
    d'erreur (utiliser System.err.println) 
    !!! Attention !!! : l'objet java.awt.Image n'est pas adapté à Android, il 
    faut trouver l'equivalent, c'est à dire un objet capable de représenter une
    image et remplacer. 
    */
    public void sendImage(Image image);
    
    /*envoie un paquet connnectionClosure à l'utilisateur distant contenant 
    l'UID local pour se déconnecter*/
    public void disconnectFromUser();
    
    /* cette méthode doit être appelé quand on recoit des données via la socket 
    TCP. Elle crée le paquet et quand il est terminé, elle appelle le listener
    associé et/ou réalise l'action néccessaire*/
    void OnReceive();
    
    /* se déconnecte du serveur et ferme la socket*/
    public void close();
}
