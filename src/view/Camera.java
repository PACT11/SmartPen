
package view;

/*
 *
 */

public class Camera {
    private InputScreen.ImageListener imageListener;
    
    public Camera() {
        // TODO : create a timer that take a picture on a regular basis and call imageListener for processing 
        System.out.println("Camera : new camera created : starting taking pictures");
    }
    
    // add a listener called each time a picture is taken (EXCEPT when taken with takePicture())
    public void addNewImageListener(InputScreen.ImageListener listener) {
        imageListener = listener;
    }
    // take and return a picture
    public Bitmap takePicture() {
        System.out.println("Camera : taking a picture");
        return null;
    }
}
