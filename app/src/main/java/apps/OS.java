
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
        launcher(); // start a method that start
    }
    // start the apps given in startapp() in OS's Thread
    private void launcher() {
        do {
            synchronized (launcher) {
                try {
                    launcher.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("hey hey");
            for(Application app : applications) {
                if(app.getClass().getSimpleName().equals(this.app))
                    app.run();
            }
        } while (!app.equals(""));
        menu.debugClick("close");
    }
    public void startApp(String name) {
        app = name;
        synchronized (launcher) {
            launcher.notify();
        }
    }
    public Application getApp(String name) {
        for(Application app : applications) {
            if(app.getClass().getSimpleName().equals(this.app))
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
