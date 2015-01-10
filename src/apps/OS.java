
package apps;
import view.*;
/*
 */
public class OS extends Application {
    @Override
    protected void onLaunch() {
        System.out.println("OS now running !");
        // load main system components
        os = this;
        outputScreen = new OutputScreen();
        inputScreen = new InputScreen();
        inputScreen.setOutputScreen(outputScreen);
        loadMenu();
        for(Application app : applications)
            menu.addItem(app.getClass().getSimpleName());
    } 
    @Override
    protected void onClose() {
        System.out.println("OS closed");
    }
    
}
