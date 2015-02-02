
package remote.server;

import remote.messages.Message;
import remote.listeners.MessageListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import remote.SmartInputStream;

/*
 */
public class Client {
    // ##### Static #####
    private static ArrayList<Client> clients = new ArrayList<>();
    
    public static Client add(Socket newClientSocket) {
        Client client = new Client(newClientSocket);
        clients.add(client);
        return client;
    }
    public static void closeAll() {
        for(Client c : clients) 
            c.close();
        clients.clear();
    }
    // obtenir la liste des clients connectés
    public static String[] getUserList() {
        ArrayList<String> list = new ArrayList<>();
        for(Client c : clients) {
            if(c.getUID()!=null)
                list.add(c.getUID());
        }
        return (String[]) list.toArray();
    }
    public static Client getClient(String UID) {
        for(Client c : clients) {
            if(c.getUID().compareToIgnoreCase(UID.replace('\n', ' ').trim())==0)
               return c;
        }
        return null;
    }
    
    // ##### dynamic #####
    private Socket socket;
    private ObjectOutputStream out;
    private SmartInputStream in;          // an objectInputStream with built-in listener support (calls onReceive when a message is received)
    private String UID;
    private Connection connection;
    
    public Client(Socket socket) {
        this.socket = socket;
        // creation des flux d'entree/sortie
        try {
            in = new SmartInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("RemotePen : unable to create client input stream");
            System.err.println(ex);
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("RemotePen : unable to create client output stream");
            System.err.println(ex);
        }
        
        in.addMessageListener(new MessageListener() { // appelé quand in recoit un caractere
            @Override
            public void messageReceived(Message message) {
                onReceive(message);
            }
        });

        in.addCloseListener(new Runnable() { // appelé quand in a été fermé
            @Override
            public void run() {
                onClose();
            }          
        });
    }
    // a la reception d'un message complet ( objet Message )
    private void onReceive(Message message) {
        message.onServerReceive(this);
    }
    // envoie d'un message au client représenté par cet objet
    public void sendMessage(Message message) {
        System.out.println("Server : send message " + message.getClass().getSimpleName());
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
            System.err.println("Server : problem while sending a message");
            System.err.println(ex);
        }
    }
    // si le flux d'entrée a été fermé (signifie le client a fermé la socket)
    private void onClose() {
        close();
        clients.remove(this);
        if(connection!=null) {
            connection.close(this);
        }
    }
    
    public void close() {
        try {
            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Server : problem closing the socket");
            System.err.println(ex);
        }
    }
        
    // ######### Getters and Setters #########
    
    // renvoie l'identifiant unique du client
    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }
    
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}