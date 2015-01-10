
package remote;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
Cette classe représente un message qui peut être envoyé ou recu. 

######### Structure d'un paquet #########
Pour se comprendre, il faut définir un langage commun, qui va assurer la bonne
transmission de données. Je propose la structure suivante :
(début du paquet) < HEADER (1 char) | LENGTH (2 char) | MESSAGE (LENGTH char) > (fin de paquet)

HEADER spécifie le type de contenu du paquet (voir enum Header), LENGTH spécifie 
la taille du message qui suit, SANS COMPTER HEADER et LENGTH. Cela permet d'etre 
sur que le message est bien terminé avant de le traiter. La taille maximale 
autorisée du message est de plus de 4Go (!). 

##### Utilisation de l'objet paquet #####
On suppose que l'objet contenant la socket TCP a un attribut paquet

à la reception :
si le paquet est complet :
    *on cree un nouveau paquet (paquet = new Packet(caractere recu))
sinon
    * on lit le caractere (paquet.readChar(caractere recu))
    * si le paquet est complet : 
        > selon le type de paquet (décrit dans le Header) on appelle le listener
            ou la méthode correspondante

a l'envoi :
on programme le header et le message dans un nouveau paquet
on envoie le tableau de char obtenu en appelant paquet.getCharArray()
 
####### contenu des packets ########
* connectionRequest :
send : contains target UID
receive : contains source UID
* connectionAnswer :
send : char 'o' for request accepted
       char 'd' for request denied
       char 'u' for non-existing UID
receive : same
* connectionClosure :
void
* UID :
send : the local UID
* userList :
send : void
receive : each name separated by '\n'
/!\ last name is followed by '\n'

*/

public class Packet {
    public static final long timeOutDelay = 10000; //  en ms
    
    private Header header;
    private int length = 1;                        // taille du message en nombre de char (16bits) 
    private ArrayList<Character> message;          
    private Timer timeOut;                        
    private int lengthCharCounter = 0;             // compteur des premier characteres recus qui représentent length
    
    /* Constructeur : pour la création d'un paquet à la RECEPTION
        au début de la reception d'un nouveau paquet, on cree un nouvel objet 
        Packet et on passe le premier char recu au constructeur.
    */
    public Packet(int firstChar) {
        header = Header.fromChar(firstChar);
        timeOut = new Timer();
        timeOut.schedule(new TimerTask() {
            @Override
            public void run() {
                System.err.println("receipt time out !");
                header = Header.error;
            }
        }, timeOutDelay);
        if(!isSane()) {
            System.err.println("wrong header !");
        }
        message = new ArrayList<>();
    }
    /* Constructeur : pour la création d'un paquet à l'ENVOI
        on spécifie le type de paquet dans le constructeur
    */
    public Packet(Header header) {
        this.header = header;
        message = new ArrayList<>();
    }
    
    // renvoi vrai si le message a été intégralement recu ou si le header est invalide
    public boolean isComplete() {
        if(!isSane()) {
            return true;
        }
        return length <= message.size();
    }
    // renvoie vrai si le header est valide et si il n'y a pas eu de timeout
    public final boolean isSane() {
        return header != Header.error;
    }
    // lit un caractere et l'ajoute au paquet
    public void readChar(int c) {
        if(lengthCharCounter < 2) {
            lengthCharCounter++;
            if(lengthCharCounter==1) {
                length = (c<<16) + 1;
            } else {
                length += c - 1;
            }
        } else {
            message.add((char) c);
        }
        if(isComplete())
            timeOut.cancel();
    }
    
    // renvoi un tableau de char pret a etre envoye tel quel
    public char[] getCharArray() {
        length = message.size();
        char array[] = new char[length+3];
        array[0] = (char) Header.toChar(header);
        array[1] = (char) (length>>>16);
        array[2] = (char) (length&0xFFFF);
        for(int i=0;i<message.size();i++)
            array[i+3] = message.get(i);
        return array;
    }
    
    // Getters and Setters
    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }

    public ArrayList<Character> getMessage() {
        return message;
    }
    public String getStringMessage() {
        String msg = "";
        for(Character c : message)
            msg += c.toString();
        return msg;
    }
    public void setMessage(ArrayList<Character> message) {
        this.message = message;
    }
    public void setMessage(String message) {
        char[] array = message.toCharArray();
        this.message.clear();
        for(int i=0;i<array.length;i++) {
            this.message.add(array[i]);
        }
    }
    public void setByteArray(byte[] array) {
        message.clear();
        if(array.length%2 == 1) {
            message.add((char) (array[length-1]*256 + 1));
            System.out.println(array.length%2);
        } else {
            message.add((char)0);
        }
        for(int i=0; i<array.length-1;i+=2) {
            message.add((char)(array[i]*256+ array[i+1]));
        }
    }
    public byte[] getByteArray() {
        int size = (message.size()-1)*2;
        byte[] array;
        if((message.get(0)&0xFF) > 0) {
            size++;
            array = new byte[size];
            array[size-1]= (byte) ((byte) (message.get(0) << 8)&0xFF);
        } else {
            array = new byte[size];
        }
        for(int i=1;i<message.size();i++) {
            array[(i-1)*2] = (byte) ((byte) (message.get(i) << 8)&0xFF);
            array[(i-1)*2+1] = (byte) ((byte) (message.get(i)&0xFF));
        }
        return array;
    }
}
