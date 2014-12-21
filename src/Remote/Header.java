
package Remote;

/*
Cet enumerateur représente les différentes valeurs possibles d'en-tête d'un paquet, c'est à 
dire du premier caractère du message, qui précise quel sera le contenu du 
message lui-même.
 */
public enum Header {
    command,            // le paquet contient une commande envoyée par une application d'un SmartPen à un autre
    image,              // contient une image
    connectionRequest,  // contient une demande de connection à un autre SmartPen
    connectionAnswer,   // contient la réponse à une demande de connection
    connectionClosure,  // contient une information de deconnexion 
    userList,           // contient une demande d'obtention de la liste des utilisateur (client), ou la liste elle-meme (serveur) 
    UID,                // contient l'UID d'un utilisateur qui se connecte au serveur
    error;              // header non reconnu ou probleme de reception
    
    // converti un char recu en Header 
    public static Header fromChar(char header) {
        switch(header) {
            case 'c':
                return command;
            case 'i':
                return image;
            case 'r':
                return connectionRequest;
            case 'a':
                return connectionAnswer;
            case 'x':
                return connectionClosure;
            case 'u':
                return userList;
            case 'n':
                return UID;
            default:
                return error;
        }
    }
    // converti un Header en char qui peut alors être envoyé
    public static char toChar(Header header) {
        switch(header) {
            case command:
                return 'c';
            case image:
                return 'i';
            case connectionRequest:
                return 'r';
            case connectionAnswer:
                return 'a';
            case connectionClosure:
                return 'x';
            case userList:
                return 'u';
            case UID :
                return 'n';
            default:
                return 'e';
        }
    }
}
