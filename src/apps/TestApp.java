
package apps;

import remote.*;
import view.*;
/*
 */
public class TestApp extends Application {
    RemotePen pen;
    @Override
    protected void onLaunch() {
        pen = new RemotePen("john appleseed");
        pen.connectToServerFromFile("/serverAddress");
        configureRemoteListeners(pen);
        
        String[] users = pen.getConnectedUsers();
        for(String user : users)
            System.out.println(user);
        
        pen.connectToUser("john appleseed");
    }

    @Override
    protected void onClose() {
        pen.close();
    }
    @Override
    protected void onNewImage(Bitmap image) {
        super.onNewImage(image);
    }
    @Override
    protected void onConnectionRequest(String distantUID){
        pen.acceptConnection(true);
    }
    @Override
    protected void onConnectionAnswer(boolean isAccepted){
        if(isAccepted) {
            System.out.println("TestApp: client accepted request !");
            pen.sendCommand("hello you");
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
        pen.disconnectFromUser();
    }
}
