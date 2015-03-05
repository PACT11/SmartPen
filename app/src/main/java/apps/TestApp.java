
package apps;

import remote.*;
import remote.messages.ConnectionAnswer;
import view.MyCamera;

import android.graphics.Bitmap;
/*
 */
public class TestApp extends Application {
    RemotePen bob;
    RemotePen john;
    boolean readyForImage = false;
    @Override
    protected void onLaunch() {
        bob = new RemotePen("bob");
        bob.connect(RemotePen.DEFAULTIP,2323);
        john = new RemotePen("john");
        john.connect(RemotePen.DEFAULTIP,2323);

        configureRemoteListeners(bob);
        configureRemoteListeners(john);
        
        String[] users = bob.getConnectedUsers();
        for(String user : users)
            System.out.println(user);
        
        bob.connectToUser("john");
    }

    @Override
    protected void onClose() {
        bob.close();
        john.close();
    }
    @Override
    protected void onNewImage(Bitmap image) {
        if(readyForImage) {
            System.out.println("Test App : Received a new image");
            john.sendImage(image);
            readyForImage = false;
        }
    }
    @Override
    protected void onConnectionRequest(String distantUID){
        john.acceptConnection(true);
    }
    @Override
    protected void onConnectionAnswer(short answer){
        if(answer == ConnectionAnswer.ACCEPT) {
            System.out.println("TestApp: client accepted request !");
            bob.sendCommand("hello you");
        } else {
            System.out.println("TestApp: client refused connection");
        }
    }
    @Override
    protected void onConnectionClosure(String distantUID){
        System.out.println("TestApp: connection with client closed");
        menu.debugClick("close");
    }
    @Override
    protected void onCommandReceived(String command){
        System.out.println("received command : "+command);
        readyForImage=true;
    }

    @Override
    protected void onImageReceived(Bitmap image) {
        System.out.println("Test : received image");
        MyCamera.savePicture(image);
        bob.disconnectFromUser();
    }
}
