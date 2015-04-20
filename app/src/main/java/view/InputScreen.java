
package view;

import android.graphics.Bitmap;

import java.util.ArrayList;

import apps.NetworkApp;
import netzwerk.Connector;
import pact.smartpen.projection;
import remote.messages.Command;
import shape.ShapeProcessor;

import static shape.ShapeProcessor.hasHand;

/*
 *
 */
public class InputScreen {
    public interface ImageListener {
        public void newImage(Bitmap image);
    }

    private ImageListener newImageListener;
    private OutputScreen outputScreen;
    private MyCamera camera;
    private ArrayList<shape.Point> corners;
    private int imageCounter=0;
    private Connector server;

    private void onNewImage(Bitmap image) {
        if(!hasHand(image)) { // if the user's hand is not over the sheet
            if(imageCounter%10==0) {
                server.sendMessage(new Command("standby"));
                System.out.println("InputScreen : no hand over the sheet");
                outputScreen.getMenu().click(-1);
                outputScreen.blackOut();         // shut down projector shortly
                Bitmap currentImage = camera.takePicture();

                if (newImageListener != null && currentImage!=null) {     // call the new image listener if any
                    newImageListener.newImage(currentImage);
                }
            }
            imageCounter++;
        } else { // check if user is clicking the menu bar
            server.sendMessage(new Command("writing"));
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
            camera=null;
        }
    }
    public void restart(projection activity, Connector server) {
        this.server=server;
        if(camera==null) {
            camera = new MyCamera();
        }
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