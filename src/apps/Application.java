
package apps;

import view.*;
import java.util.ArrayList;

/*
 * A general class for all applications. 
 */
public abstract class Application {
    protected static OS os;
    protected static InputScreen inputScreen;
    protected static OutputScreen outputScreen;
    
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
    
}
