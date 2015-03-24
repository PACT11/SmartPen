
package view;

import java.util.Timer;
import java.util.TimerTask;

/*
 *
 */

public class Camera {
    private InputScreen.ImageListener imageListener;
    
    public Camera() {
        // TODO : create a timer that take a picture on a regular basis and call imageListener for processing 
        System.out.println("Camera : new camera created : starting taking pictures");
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ontakingPicture();
            }
        }, 100, 500);
    }
    
    // add a listener called each time a picture is taken (EXCEPT when taken with takePicture())
    public void addNewImageListener(InputScreen.ImageListener listener) {
        imageListener = listener;
    }
    // take and return a picture
    public Bitmap takePicture() {
        System.out.println("Camera : taking a picture");
        imageListener.newImage(null);
        return null;
    }
    private void ontakingPicture() {
        Bitmap bmp = takePicture();
        imageListener.newImage(bmp);
    }
    // close the camera 
    public void close() {
        
    }
    // debug
    public void simulateNewImage(Bitmap image) {
        imageListener.newImage(image);
    }
}
