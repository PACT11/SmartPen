package remote;

import remote.listeners.ConnectionListener;
import remote.listeners.CommandReceiveListener;
import remote.listeners.ImageReceiveListener;
import remote.listeners.MessageListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import view.Bitmap;

public class RemotePen {
    //listeners
    private ConnectionListener connectionListener;
    private ImageReceiveListener imageListener;
    private CommandReceiveListener commandListener;
    
    private Socket socket;
    private PrintWriter out;
    private SmartInputStream in;
    private String UID;                     // l'identifiant unique du client
    private String distantUID;              // identifiant unique du client a qui on est connecté (if any)
    private boolean userListReceived;       // un flag pour débloquer getUserList quand la liste est recu
    
    // le constructeur doit avoir la forme public RemotePen(String UID); 
    public RemotePen(String localUID){
        this.UID=localUID;
    }

    /* connecter la socket TCP au serveur distant.*/
    public void connectToServer(byte[] serverIp, int port) {
        // open socket connected to server
        try {
            socket = new Socket(InetAddress.getByAddress(serverIp), port);
            System.out.println("RemotePen : connecting to the server" ); 
        } catch (UnknownHostException e) {
            System.err.println("RemotePen : Cannot find specified host : " + serverIp);
            System.err.println(e);
        } catch (IOException e) {
            System.err.println("RemotePen : problem with I/O");
            System.err.println(e);
        }
        // open input stream
        try {
            in = new SmartInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("RemotePen : unable to create client input stream");
            System.err.println(ex);
        }
        //open output stream
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println("RemotePen : unable to create client output stream");
            System.err.println(ex);
        }
        
        in.addDataListener(new MessageListener() { // appelé quand in recoit un caractere
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
        // handshake : on envoie notre UID au serveur pour que les autres clients puissent nous identifier 
        Packet packet = new Packet(Header.UID);
        packet.setMessage(UID);
        sendPacket(packet);
    }

    /* connecter la socket TCP a un serveur distant dont l'IP est stockee 
    dans un fichier. */
    public void connectToServerFromFile(String filename) {
        byte[] serverIp = {10,0,1,4};
        int port= 2323;
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(getClass().getResource(filename).getFile())); // open the file
            
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if(line.startsWith("port:")) {
                    port = Integer.parseInt(line.replaceFirst("port:", "").trim());
                } else if(line.startsWith("ip:")) {
                    line = line.replaceFirst("ip:", "").trim();
                    int i=0;
                    for(String ipiece : line.split(line)) {
                        serverIp[i] =(byte) Integer.parseInt(ipiece);
                    }
                }
            }
            reader.close();
        } catch(java.io.FileNotFoundException e) {
            System.err.println("RemotePen : opening error : file not found");
        } catch (java.io.IOException|java.lang.NumberFormatException e) {
            System.err.println("RemotePen : opening error : invalid file");
        }
        connectToServer(serverIp, port);
    }

    /* cette methode doit etre appele quand on recoit des donnees via la socket 
    TCP. Elle cree le paquet et quand il est termine, elle appelle le listener
    associe et/ou realise l'action neccessaire*/
    private void onReceive(int data) {
        if(currentPacket==null || currentPacket.isComplete()) {
            currentPacket = new Packet(data);
        } else {
            currentPacket.readChar(data);
            if(currentPacket.isComplete()&&currentPacket.isSane())
                onPacketReceive();
        }
    }
    
    /* appelee quand on a recu un paquet complet et valide*/
    private synchronized void onPacketReceive() {
        switch(currentPacket.getHeader()) {
            case userList:
                userListReceived = true;
                this.notify(); 
                break;
            case connectionClosure:
                if(connectionListener!=null)
                    connectionListener.connectionClosed(distantUID);
                distantUID = null;
                break;
            case connectionRequest:
                distantUID = currentPacket.getStringMessage();
                if(connectionListener!=null)
                    connectionListener.connectionRequest(distantUID);
                break;
            case connectionAnswer:
                if(!currentPacket.getStringMessage().equals("o"))
                    distantUID=null;
                if(connectionListener!=null)
                    connectionListener.connectionAnswer(currentPacket.getStringMessage().equals("o"));
                break;
            case command:
                if(commandListener!=null)
                    commandListener.commandReceived(currentPacket.getStringMessage());
                break;
            case image:
                onImageReceive();
                break;
        }
    }
    /* quand une image est recu */
    private void onImageReceive() {
        Bitmap image = null;
        
        // TODO : algo
        
        if(imageListener!=null)
            imageListener.imageReceived(image);
    }
    /* envoyer un paquet au serveur*/
    private void sendPacket(Packet packet) {
        System.out.println("RemotePen : send packet " + packet.getHeader());
        out.write(packet.getCharArray());
        out.flush();
    }
    /* obtenir la liste des utilisateurs connectés. Dans ce cas, et ce cas 
    seulement, pour des questions de simplicité, on envoie un paquet userList 
    vide et on bloque le programme jusqu'à obtenir la réponse du serveur.*/
    synchronized public String[] getConnectedUsers() {
        if(!isReady())         // verifier que la socket est bien connectée
            return null;
        
        sendPacket(new Packet(Header.userList));
        userListReceived = false;
        try {
            this.wait(1000);
        } catch (InterruptedException ex) {
        }
        
        if(userListReceived) {
            return currentPacket.getStringMessage().split("\n");
        } else {
            System.err.println("reception timeout ! No packet received");
            return null;
        }
    }
    /*envoyer une requête de connection à l'utilisateur spécifié*/
    public void connectToUser(String UID) {
        if(!isReady())     // verifier que la socket est bien connectée
            return;
        
        Packet packet = new Packet(Header.connectionRequest);
        packet.setMessage(UID);
        sendPacket(packet);
        distantUID = UID;
    }
    /*envoie un paquet connnectionClosure a l'utilisateur distant contenant 
    l'UID local pour se deconnecter*/
    public void disconnectFromUser(){
        if(!isReady())     // verifier que la socket est bien connectée
            return;
        
        Packet packet = new Packet(Header.connectionClosure);
        distantUID = null;
        sendPacket(packet);
    }
    /* accepter ou non la requete de connection recu*/
    public void acceptConnection(boolean isAccepted) {
        if(!isReady())      // verifier que la socket est bien connectée
            return;
        
        Packet packet = new Packet(Header.connectionAnswer);
        if(isAccepted) {
            packet.setMessage("o");
        }
        else {
            packet.setMessage("d");
            distantUID = null;
        }
        sendPacket(packet);
    }
    /*envoyer un paquet command à l'utilisateur distant. command devra �tre 
    converti en un tableau de char pour etre envoye.*/
    public void sendCommand(String command) {
        if(!isReady())   // verifier que la socket est bien connectée
            return;
            
        Packet packet = new Packet(Header.command);
        packet.setMessage(command);
        sendPacket(packet);
    }
    /* envoyer un paquet image a l'utilisateur distant contenant 'image. image devra etre 
    convertie en un tableau de char puis envoyee.*/
    public void sendImage(Bitmap image) {
        if(!isReady())   // verifier que la socket est bien connectée
            return;
        if(distantUID==null) {
            System.err.println("RemotePen : not connected to any user");
        }
        
        Packet packet = new Packet(Header.image);
        
        //TODO algo
        
    }
    // renvoie vrai si la socket est connectee a un serveur
    private boolean isReady() {
        return socket!=null && socket.isConnected();
    }
    // appelée si le flux d'entrée est fermé (symptome que le serveur s'est déconnecté)
    void onClose() {
        if(connectionListener!=null&&distantUID!=null)       // si on était connecté a un client
            connectionListener.connectionClosed(distantUID);
        close();
    }
    /* se deconnecte du serveur et ferme la socket*/
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
    
    // listeners :
    public void addConnectionListener(ConnectionListener listener) {
        connectionListener= listener;
    }
    public void addCommandListener(CommandReceiveListener listener) {
        commandListener=listener;
    }
    public void addImageListener(ImageReceiveListener listener) {
        imageListener = listener;
    }
}
