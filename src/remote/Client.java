
package remote;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


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
    public static ArrayList<Client> getClients() {
        return clients;
    }
    public static void closeAll() {
        for(Client c : clients) 
            c.close();
        clients.clear();
    }
    // obtenir la liste des clients connectés
    public String getUserList() {
        String userList = "";
        for(Client c : clients) {
            if(c.getUID()!=null)
               userList += c.getUID() + "\n";
        }
        return userList;
    }
    public Client getClient(String UID) {
        for(Client c : clients) {
            if(c.getUID().compareToIgnoreCase(UID.replace('\n', ' ').trim())==0)
               return c;
        }
        return null;
    }
    // ##### dynamic #####
    private Socket socket;
    private PrintWriter out;
    private SmartBufferedReader in;
    private String UID;
    private Packet currentPacket;
    
    private DataListener dataListener;
    private Runnable closeListener;
    private PacketListener packetListener;

    private boolean manual = false;
    
    public Client(Socket socket) {
        this.socket = socket;
        // creation des flux d'entree/sortie
        try {
            in = new SmartBufferedReader(new
                    InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.err.println("Server : unable to create client input stream");
            System.err.println(ex);
        }
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println("Server : unable to create client output stream");
            System.err.println(ex);
        }
        
        in.addDataListener(new DataListener() { // appelé quand in recoit un caractere
            @Override
            public void dataReceived(int data) {
                onReceive(data);
            }
        });

        in.addCloseListener(new Runnable() { // appelé quand in a été fermé
            @Override
            public void run() {
                onClose();
            }          
        });
    }
    // a la reception d'un caractere
    private void onReceive(int data) {
        if(dataListener!=null)
            dataListener.dataReceived(data);
        if(currentPacket==null || currentPacket.isComplete()) {
            currentPacket = new Packet(data);
        } else {
            currentPacket.readChar(data);
            if(currentPacket.isComplete()&&currentPacket.isSane()&&!manual)
                onPacketReceive();
        }
    }
    // a la reception d'un paquet (donnee structurés)
    private void onPacketReceive() {
        switch(currentPacket.getHeader()) {
            case connectionRequest:
                Connection.addConnection(this,
                        getClient(currentPacket.getStringMessage()));
            case command:
            case image:
            case connectionAnswer:
            case connectionClosure:  
                if(packetListener!=null)
                    packetListener.packetReceived(currentPacket);
                break;
            case UID:
                UID = currentPacket.getStringMessage().replace('\n', ' ').trim();
                break;
            case userList:
                Packet packet = new Packet(Header.userList);
                packet.setMessage(getUserList());
                sendPacket(packet);
                break;
        }
    }
    // si le flux d'entrée a été fermé (signifie le client a fermé la socket)
    private void onClose() {
        close();
        clients.remove(this);
        if(closeListener!=null)
            closeListener.run();
    }
    // envoie libre
    public void send(String text) {
        System.out.println("send " + text);
        out.print(text);
        out.flush();
    }
    // envoie d'un packet (données structurées)
    public void sendPacket(Packet packet) {
        System.out.println("send packet " + packet.getHeader() + " containing " + packet.getStringMessage());
        out.write(packet.getCharArray());
        out.flush();
    }
    public void close() {
        out.flush();
        out.close();
        try {
            in.close();
            socket.close();
        } catch (IOException ex) {
            System.err.println("Server : problem closing the socket");
            System.err.println(ex);
        }
    }
    // ajouter un listener appelé quand on recoit un caractere
    public void addDataListener(DataListener listener) {
        dataListener = listener;
    }
    // ajouter un listener appelé quand la connexion avec la client est fermé
    public void addCloseListener(Runnable listener) {
        closeListener = listener;
    }
    // ajout d'un listener appelé quand on a recu un paquet valide 
    public void addPacketListener(PacketListener listener) {
        packetListener= listener;   
    }
    // renvoie l'identifiant unique du client
    public String getUID() {
        return UID;
    }
    
    public void manual() { //debug
        manual = true;
    }
}