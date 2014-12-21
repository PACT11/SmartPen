
package Remote;

/* Listener de reception de commande :
Cet objet permet a l'application de réagir en cas de reception d'une commande. 
Il suffit pour cela d'overrider la méthode commandReceived. Exemple :
RemotePen.addCommandReceiveListener(new CommandReceiveListener() {
    public void commandReceived(String command) {
        System.out.println(command);  // on affiche la commande à chaque nouvelle commande recu
    }
}
 */
public interface CommandReceiveListener {
    public void commandReceived(String command);
}
