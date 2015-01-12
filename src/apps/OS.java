
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
        System.out.println("OS : Simulate a click on TestApp");
        menu.debugClick("TestApp");
    } 
    @Override
    protected void onMenuClick(String menu) {
        super.onMenuClick(menu);
        for(Application app : applications) {
            if(app.getClass().getSimpleName().equals(menu)) 
                app.run();
        }
    }
    @Override
    protected void onClose() {
        System.out.println("OS closed");
    }
    
    protected void resume() {
        System.out.println("OS : resumed OS !");
        loadMenu();
        System.out.println("OS : Simulate a click on close");
        menu.debugClick("close");
    }
}
