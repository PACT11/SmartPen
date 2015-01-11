
package apps;
import view.*;
/*
 */
public class TestApp extends Application {
    @Override
    protected void onLaunch() {
        System.out.println("TestApp : Simulate a new image captured by the camera");
        inputScreen.simulateNewImage(null);
        System.out.println("TestApp : Simulate a click on close");
        menu.debugClick("close");
    }

    @Override
    protected void onClose() {
        
    }
    @Override
    protected void onNewImage(Bitmap image) {
        super.onNewImage(image);
    }
}
