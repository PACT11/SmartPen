
package apps;

import remote.*;
import remote.messages.ConnectionAnswer;
import view.*;
/*
 */
public class TestApp extends Application {
    RemotePen pen;
    RemotePen pen2;
    @Override
    protected void onLaunch() {
        byte[] ip = {(byte)192,(byte)168,1,18};
        pen = new RemotePen("john appleseed");
        pen.connect();
        pen2 = new RemotePen("arno");
        pen2.connect();
        configureRemoteListeners(pen);
        configureRemoteListeners(pen2);
        
        String[] users = pen.getConnectedUsers();
        for(String user : users)
            System.out.println(user);
        
        pen.connectToUser("arno");
    }

    @Override
    protected void onClose() {
        pen.close();
        pen2.close();
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
    protected void onConnectionAnswer(short answer){
        if(answer == ConnectionAnswer.ACCEPT) {
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
