
package remote.server;

import remote.messages.Message;
import java.net.Socket;
import java.util.ArrayList;
import remote.Client;

/*
 * Représente un client conneté au serveur (du coté serveur)
 */
public class ServerClient extends Client{
    // ##### Static #####
    private static ArrayList<ServerClient> clients = new ArrayList<>();
    
    public static ServerClient add(Socket newClientSocket) {
        ServerClient client = new ServerClient(newClientSocket);
        clients.add(client);
        return client;
    }
    public static void closeAll() {
        for(ServerClient c : clients) 
            c.close();
        clients.clear();
    }
    // obtenir la liste des clients connectés
    public static String[] getUserList() {
        int i=0;
        for(ServerClient c : clients) {
            if(c.getUID()!=null)
                i++;
        }
        String[] list = new String[i];
        i=0;
        for(ServerClient c : clients) {
            if(c.getUID()!=null) {
                list[i]=c.getUID();
                i++;
            }
        }
        return list;
    }
    public static ServerClient getClient(String UID) {
        for(ServerClient c : clients) {
            if(c.getUID().compareToIgnoreCase(UID.replace('\n', ' ').trim())==0)
               return c;
        }
        return null;
    }
    
    // ##### dynamic #####
    private Connection connection;
    
    public ServerClient(Socket socket) {
        connect(socket);
    }
    
    // a la reception d'un message complet ( objet Message )
    @Override
    protected void onReceive(Message message) {
        System.out.println("Server : received message " + message.getClass().getSimpleName());
        message.onServerReceive(this);
    }
    // si le flux d'entrée a été fermé (signifie le client a fermé la socket)
    @Override
    protected void onClose() {
        close();
        clients.remove(this);
        if(connection!=null) {
            connection.close(this);
        }
    }
    
    // ######### Getters and Setters #########
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}