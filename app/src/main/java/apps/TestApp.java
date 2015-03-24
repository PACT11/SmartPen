
package apps;

import netzwerk.*;
import netzwerk.messages.*;
import remote.messages.*;
import view.MyCamera;

import android.graphics.Bitmap;
/*
 */
public class TestApp extends Application {
    Connector bob;
    Connector john;
    boolean readyForImage = false;
    @Override
    protected void onLaunch() {
        bob = new Connector("bob");
        //bob.connect(Login.serverIP,2323);
        john = new Connector("john");
        //john.connect(Login.serverIP,2323);

        configureRemoteListeners(bob);
        configureRemoteListeners(john);
        
        String[] users = UserList.get(bob);
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
            john.sendMessage(new Image(image));
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
            bob.sendMessage(new Command("hello you"));
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
