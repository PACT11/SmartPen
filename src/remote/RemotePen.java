package remote;

import remote.listeners.*;
import remote.messages.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import remote.ipSync.IPsyncClient;

import view.Bitmap;

public class RemotePen extends Client {
    public final int DEFAULTPORT = 2323;
    //listeners
    private ConnectionListener connectionListener;
    private ImageReceiveListener imageListener;
    private CommandReceiveListener commandListener;
    
    private Message lastMessage; //last received message
    
    // le constructeur doit avoir la forme public RemotePen(String UID); 
    public RemotePen(String localUID){
        this.UID=localUID;
    }

    /* connecter la socket TCP au serveur distant.*/
    public void connect(byte[] serverIp, int port) {
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
        connect(socket); // connecter les flux d'entrée/sortie
        
        // handshake : on envoie notre UID au serveur pour que les autres clients puissent nous identifier
        sendMessage(new UID(UID));
    }

    /* connecter la socket TCP a un serveur distant dont l'IP est stockee 
    dans un fichier. */
    public void connect(String filename) {
        byte[] serverIp = {10,0,1,4};
        int port= DEFAULTPORT;
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
        connect(serverIp, port);
    }
    
    /*se connecter au serveur dont on récupere l'IP via IPsync et le port DEFAULTPORT*/
    public void connect() {
        connect(IPsyncClient.getIP(),DEFAULTPORT);
    }
    
    /* appelee quand on a recu un message*/
    @Override
    protected synchronized void onReceive(Message message) {
        lastMessage = message;
        message.onClientReceive(this);
    }
    /* quand une image est recue */
    public void onImageReceive(byte[] image) {
    	//Bitmap imgrecue = BitmapFactory.decodeByteArray(image,0,image.length);
        
        //if(imageListener!=null)
        //    imageListener.imageReceived(imgrecue);
    }
    
    /* quand une image est recue */
    public void onCommandReceive(String command) {
        if(commandListener!=null)
            commandListener.commandReceived(command);
    }

    /* obtenir la liste des utilisateurs connectés (bloque l'execution)*/
    synchronized public String[] getConnectedUsers() {
        sendMessage(new UserList());
        lastMessage = null;
        try {
            this.wait(1000);
        } catch (InterruptedException ex) {}

        if(lastMessage!=null && lastMessage instanceof UserList)
            return ((UserList) lastMessage).getUsers();
        else
            System.err.println("RemotePen : reception timeout ! No packet received");

        return null;
    }

    public boolean isRegistered(String UID, String password) {
        // on se connecte au serveur
        this.connect();
        // envoyer un message de type CheckUser
        this.sendMessage(new CheckUser(UID, password));
        // on attend la reponse
        try {
            this.wait(1000);
        } catch (InterruptedException ex) {}
        // si le dernier message recu est bien la reponse au message envoyé
        if(lastMessage!=null && lastMessage instanceof CheckUser)
            return ((CheckUser) lastMessage).isValid();
        else
            System.err.println("RemotePen : reception timeout ! No packet received");

        return false;
    }
    /*envoyer une requête de connection à l'utilisateur spécifié*/
    public void connectToUser(String targetUID) {
        sendMessage(new ConnectionRequest(UID,targetUID));
    }
    /*envoie un paquet connnectionClosure a l'utilisateur distant contenant 
    l'UID local pour se deconnecter*/
    public void disconnectFromUser(){
        sendMessage(new ConnectionClosure(UID));
    }
    /* accepter ou non la requete de connection recu*/
    public void acceptConnection(boolean isAccepted) {
        if(isAccepted) {
            sendMessage(new ConnectionAnswer(ConnectionAnswer.ACCEPT));
        }
        else {
            sendMessage(new ConnectionAnswer(ConnectionAnswer.DECLINE));
        }
    }
    /*envoyer une commande à l'utilisateur distant*/
    public void sendCommand(String command) {           
        sendMessage(new Command(command));
    }
    /* envoyer un paquet image a l'utilisateur distant contenant l'image. image devra etre 
    convertie en un tableau de char puis envoyee.*/
    public void sendImage(Bitmap image) {
         byte[] imageTableauDeBytes = this.getBytesFromBitmap(image);
         Image img = new Image(imageTableauDeBytes);
    }
    
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
    // appelée si le flux d'entrée est fermé (symptome que le serveur s'est déconnecté)
    @Override
    protected void onClose() {
        System.out.println("Remote Pen: connection lost !");
        close();
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
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }
}
