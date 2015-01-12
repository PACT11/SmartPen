
package remote;

import view.Bitmap;

/* Listener de reception d'une image:
Cet objet permet a l'application de réagir en cas de reception d'une image. 
Il suffit pour cela d'overrider la méthode ImageReceived. Exemple :
RemotePen.addImageReceiveListener(new CommandReceiveListener() {
    public void ImageReceived(Image image) {
        System.out.println("image recu");  // chaque reception d'image fait apparaitre un message dans la console
    }
}
 */
public interface ImageReceiveListener {
    public void imageReceived(Bitmap image);
}
