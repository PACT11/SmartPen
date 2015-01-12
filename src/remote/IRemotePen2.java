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
			
			     socket = new Socket(localAddress,0);	//0=pour dire que le serveur peut se connecter � n'importe quel port
			     System.out.println("L'adresse locale est : "+localAddress ); 
		         
			     socket.close();

			}catch (UnknownHostException e) {
				
				e.printStackTrace();
			}catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		
	    
	    /* connecter la socket TCP � un serveur distant dont l'IP est stock�e 
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
	    
	    /*envoyer un paquet command � l'utilisateur distant. command devra �tre 
	    converti en un tableau de char pour �tre envoy�.*/
	    
	    public void sendCommand(String command) {
	    	
	    	Packet packet = new Packet(Header.command);
	    	
	    	if(c==null){
	    		 packet.setMessage("u");
	             c1.sendPacket(packet);
	             System.err.println("Aucun utilisateur connect�");
	    	}
	    	else{
	    		packet.getCharArray();
	    		sendCommand(command); //Blocage: comment envoyer le tableau de char??
	    	}
	    	
	    	
		}
	    
	    /* envoyer un paquet image � l'utilisateur distant contenant 'image. image devra �tre 
	    convertie en un tableau de char puis envoy�e. Il faut g�rer le cas ou 
	    l'on ne soit pas connect� � un utilisateur distant en affichant un message 
	    d'erreur (utiliser System.err.println) 
	    !!! Attention !!! : l'objet java.awt.Image n'est pas adapt� � Android, il 
	    faut trouver l'equivalent, c'est � dire un objet capable de repr�senter une
	    image et remplacer. 
	    */
	    public void sendImage(Image image) {
		}
	    
	    /*envoie un paquet connnectionClosure � l'utilisateur distant contenant 
	    l'UID local pour se d�connecter*/
	    public void disconnectFromUser(){
	    	   Packet packet = new Packet(Header.connectionClosure);
	    	   close();
               c.sendPacket(packet);
	    }
	    
	    /* cette m�thode doit �tre appel� quand on recoit des donn�es via la socket 
	    TCP. Elle cr�e le paquet et quand il est termin�, elle appelle le listener
	    associ� et/ou r�alise l'action n�ccessaire*/
	    void OnReceive() {
		}
	    
	    /* se d�connecte du serveur et ferme la socket*/
	    public void close() {
	    	c1.close();
	    	c2.close();
		}
}
