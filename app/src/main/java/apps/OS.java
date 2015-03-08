
package apps;
import view.*;
/*
 */
public class OS extends Application {
    private Object launcher = new Object();
    private String app = ""; // name of the app to be lauched
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

    public Application startApp(String name) {
        for(Application app : applications) {
            if(app.getClass().getSimpleName().equals(name)) {
                app.run();
                return app;
            }
        }
        System.err.println("OS : app " + name + " doesn't exist !");
        return null;
    }
    public Application getApp(String name) {
        for(Application app : applications) {
            if(app.getClass().getSimpleName().equals(name))
                return app;
        }
        System.err.println("OS : app " + name + " doesn't exist !");
        return null;
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
    }
}
