
package view;

import android.graphics.Bitmap;

import java.util.ArrayList;
import pact.smartpen.projection;
import shape.ShapeProcessor;

/*
 *
 */
public class InputScreen {
    public interface ImageListener {
        public void newImage(Bitmap image);
    }
    
    private MenuBar menu;
    private ImageListener newImageListener;
    private OutputScreen outputScreen;
    private MyCamera camera;
    private Bitmap currentImage;
    private ArrayList<shape.Point> corners;
    private int imageCounter=0;

    public InputScreen() {

    }
    private void onNewImage(Bitmap image) {
        if(!ShapeProcessor.hasHand(image)) { // if the user's hand is not over the sheet
            if(imageCounter%10==0) {
                System.out.println("InputScreen : no hand over the sheet");
                outputScreen.getMenu().click(-1);
                outputScreen.blackOut();         // shut down projector shortly
                currentImage = camera.takePicture();

                if (newImageListener != null) {// && currentImage!=null) {     // call the new image listener if any
                    newImageListener.newImage(image);
                }
            }
        } else { // check if user is clicking the menu bar
            ShapeProcessor.findFinger(image,corners);
            imageCounter=0;
        }
    }
    public void addNewImageListener(ImageListener listener) {
        newImageListener = listener;
    }
    public void setOutputScreen(OutputScreen screen) {
        outputScreen = screen;
    }
    public void close() {
        if(camera!=null) {
            camera.close();
        }
    }
    public void restart(projection activity) {
        camera = new MyCamera();
        camera.setActivity(activity);
        camera.addNewImageListener(new ImageListener() {
            @Override
            public void newImage(Bitmap image) {
                onNewImage(image);
            }
        });
    }

    public void setCorners(ArrayList<shape.Point> corners) {
        this.corners = corners;
    }
}