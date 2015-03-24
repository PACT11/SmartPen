
package apps;

import netzwerk.Connector;
import netzwerk.listeners.ConnectionListener;
import java.util.ArrayList;
import remote.messages.*;
import remote.listeners.*;
import view.*;

import android.graphics.Bitmap;
import android.os.Handler;

/*
 * A general class for all applications. 
 */
public abstract class Application {
    public static OS os;
    public static InputScreen inputScreen;
    public static OutputScreen outputScreen;
    public static Handler handler;              //used to launch action from mainProject Thread
    
    protected static final ArrayList<Application> applications = new ArrayList<>();
    protected MenuBar menu;

    protected abstract void onLaunch();
    protected abstract void onClose();
    
    // if the app wishes to use the menu, it should override this method
    protected void onMenuClick(String menu) {
        System.out.println("App : the menu " + menu + " has been clicked");
    }
    // if the app wishes to receive sheet images, it should override this method
    protected void onNewImage(Bitmap image) {
        System.out.println("App : a new image has been received");
    }
    private void onCloseMenuClick() {
        System.out.println("App : the close menu has been clicked");
        onClose();
        if(!(this instanceof OS))
            os.resume();
    }
    public final void run() {
        // TODO : prepare the system for app launch
        System.out.println("App : Preparing the system to launch " + this.getClass().getName());
        if(outputScreen != null)
            loadMenu();               // load a new menu bar for the app
        if(inputScreen != null) {
            inputScreen.addNewImageListener(new InputScreen.ImageListener() {   // attach a listener called if a sheet image is captured
                @Override
                public void newImage(Bitmap image) {
                    onNewImage(image);
                }
            });
        }
        
        onLaunch();
    }
    protected final void loadMenu() {
        menu = new MenuBar();
        outputScreen.setMenuBar(menu);
        menu.addMenuListener(new MenuBar.MenuListener(){
            @Override
            public void menuClicked(String menuName) {
                onMenuClick(menuName);
            }
            @Override
            public void closeClicked() {
                onCloseMenuClick();
            }
        });
    }
    protected final void configureRemoteListeners(Connector pen) {
        pen.setConnectionListener(new ConnectionListener() {
            @Override
            public void connectionRequest(String distantUID){
                onConnectionRequest(distantUID);
            }
            @Override
            public void connectionClosed(String distantUID){
                onConnectionClosure(distantUID);
            }
            @Override
            public void connectionAnswer(short answer){
                onConnectionAnswer(answer);
            }
        });
        Command.setListener(new CommandReceiveListener() {
            @Override
            public void commandReceived(String command) {
                onCommandReceived(command);
            }  
        });
        Image.setListener(new ImageReceiveListener() {
            @Override
            public void imageReceived(Bitmap image) {
                onImageReceived(image);
            }
        });
    }
    // methods called when a remotePen event occurs
    protected void onConnectionRequest(String distantUID){}
    protected void onConnectionAnswer(short answer){}
    protected void onConnectionClosure(String distantUID){}
    protected void onCommandReceived(String command){}
    protected void onImageReceived(Bitmap image){}
}
