
package Remote;

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
    public Packet(char firstChar) {
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
    }
    /* Constructeur : pour la création d'un paquet à l'ENVOI
        on spécifie le type de paquet dans le constructeur
    */
    public Packet(Header header) {
        this.header = header;
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
    public void readChar(char c) {
        if(lengthCharCounter < 2) {
            lengthCharCounter++;
            if(lengthCharCounter==1) {
                length += (int) (c)*2^16;
            } else {
                length += (int) (c) - 1;
            }
        } else {
            message.add(c);
        }
    }
    
    // renvoi un tableau de char pret a etre envoye tel quel
    public char[] getCharArray() {
        length = message.size()+3;
        char array[] = new char[length];
        array[0] = Header.toChar(header);
        array[1] = (char) (short) (length/2^16);
        array[2] =  (char) (length&0xFFFF);
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
    public void setMessage(ArrayList<Character> message) {
        this.message = message;
    }
}
