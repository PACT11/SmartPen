package remote;

import java.awt.Image;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class IRemotePen2 {

	
	
	/* private ConnectionClosureListener connectionClosureListener;
	 private CommandReceiveListener commandReceiveListener;
	 private ImageReceiveListener imageReceiveListener;
	 private ConnectionRequestListener connectionRequestListener;
	 */
	 private String UID;
	 private Client c,c1,c2;

	 
	 // le constructeur doit avoir la forme public RemotePen(String UID); 
	 
	 
	 public IRemotePen2(String UID){
		 
		 this.UID=UID;
		 
	 }

	    
	    /* connecter la socket TCP au serveur distant.*/
	    public void connectToServer(String Ip) {
	    	
	    	Socket socket;
	    	InetAddress localAddress;

			try {
				localAddress=InetAddress.getLocalHost();
			
			     socket = new Socket(localAddress,0);	//0=pour dire que le serveur peut se connecter à n'importe quel port
			     System.out.println("L'adresse locale est : "+localAddress ); 
		         
			     socket.close();

			}catch (UnknownHostException e) {
				
				e.printStackTrace();
			}catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		
	    
	    /* connecter la socket TCP à un serveur distant dont l'IP est stockée 
	    dans un fichier. */
	    public void connectToServerFromFile(String filename) {
	    	
	    	Socket socket;
			BufferedReader in;

			try {
			
				socket = new Socket(InetAddress.getLocalHost(),0);	
			     
				in= new BufferedReader (new InputStreamReader (socket.getInputStream()));
			        String ip = in.readLine();
			        connectToServer(ip);
			        socket.close();
			       
			}catch (UnknownHostException e) {
				
				e.printStackTrace();
			}catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	  
	    public String[] getConnectedUsers() {
			return null;
		}
	 
	    public void connectToUser(String UID, UserAnswerListener listener) {
	    	Packet packet = new Packet(Header.connectionRequest);
	    	Connection.addConnection( c1,c2);
	    	listener.connectionAnswer(packet.getStringMessage());
		}
	    
	   
	    public void addConnectionRequestListener(ConnectionRequestListener listener) {
	    	
	    	//connectionRequestListener= listener;
	    	
	    	listener.connectionRequest(c.getUID());
	    	addConnectionRequestListener(listener);
		}
	    
	    public void addConnectionClosureListener(ConnectionClosureListener listener) {
	    	
	    	//connectionClosureListener=listener;
	    	
	    	listener.connectionClosed(c.getUID());
	    	addConnectionClosureListener(listener);
		}
	
	    public void addCommandReceiveListener(CommandReceiveListener listener) {
	    	
	    	Packet packet = new Packet(Header.command);
	    	//commandReceiveListener=listener;
	    	
	    	listener.commandReceived(packet.getStringMessage());
	    	addCommandReceiveListener(listener);
		}
	    
	   
	    public void addImageReceiveListener(ImageReceiveListener listener) {
	    	
	    	// imageReceiveListener = listener;
	    	 
	    	 listener.ImageReceived(null); //l'argument sera modifiee apres obtention de l'algorithme qui va convertir le contenu du paquet en image
		     addImageReceiveListener(listener);
		}
	    
	    /*envoyer un paquet command à l'utilisateur distant. command devra être 
	    converti en un tableau de char pour être envoyé.*/
	    
	    public void sendCommand(String command) {
	    	
	    	Packet packet = new Packet(Header.command);
	    	
	    	if(c==null){
	    		 packet.setMessage("u");
	             c1.sendPacket(packet);
	             System.err.println("Aucun utilisateur connecté");
	    	}
	    	else{
	    		packet.getCharArray();
	    		sendCommand(command); //Blocage: comment envoyer le tableau de char??
	    	}
	    	
	    	
		}
	    
	    /* envoyer un paquet image à l'utilisateur distant contenant 'image. image devra être 
	    convertie en un tableau de char puis envoyée. Il faut gérer le cas ou 
	    l'on ne soit pas connecté à un utilisateur distant en affichant un message 
	    d'erreur (utiliser System.err.println) 
	    !!! Attention !!! : l'objet java.awt.Image n'est pas adapté à Android, il 
	    faut trouver l'equivalent, c'est à dire un objet capable de représenter une
	    image et remplacer. 
	    */
	    public void sendImage(Image image) {
		}
	    
	    /*envoie un paquet connnectionClosure à l'utilisateur distant contenant 
	    l'UID local pour se déconnecter*/
	    public void disconnectFromUser(){
	    	   Packet packet = new Packet(Header.connectionClosure);
	    	   close();
               c.sendPacket(packet);
	    }
	    
	    /* cette méthode doit être appelé quand on recoit des données via la socket 
	    TCP. Elle crée le paquet et quand il est terminé, elle appelle le listener
	    associé et/ou réalise l'action néccessaire*/
	    void OnReceive() {
		}
	    
	    /* se déconnecte du serveur et ferme la socket*/
	    public void close() {
	    	c1.close();
	    	c2.close();
		}
}
