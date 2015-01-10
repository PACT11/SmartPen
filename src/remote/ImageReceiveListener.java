
package remote;

import java.awt.Image;

/* Listener de reception d'une image:
Cet objet permet a l'application de réagir en cas de reception d'une image. 
Il suffit pour cela d'overrider la méthode ImageReceived. Exemple :
RemotePen.addImageReceiveListener(new CommandReceiveListener() {
    public void ImageReceived(Image image) {
        System.out.println("image recu");  // chaque reception d'image fait apparaitre un message dans la console
    }
}

!!!!!    ATTENTION    !!!!! : j'utilise pour l'instant l'objet Image d'awt qui n'existe pas sous Android !
il faut le remplacer par son equivalent Android, c'est à dire un objet qui répresente une image
 */
public interface ImageReceiveListener {
    public void ImageReceived(Image image);
}
