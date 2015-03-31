
package view;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import pact.smartpen.projection;

/*
 * 
 */
public class OutputScreen {
    private MenuBar menu;
    private projection activity;
    private ImageView screen;
    private Bitmap black;
    private Bitmap currentImage;

    public void setMenuBar(MenuBar menu) {
        System.out.println("OutputScreen : new menu bar set");
        this.menu = menu;

    }
    // show a bitmap on screen
    public void display(final Bitmap image) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screen.setImageBitmap(image);
            }
        });
        //currentImage = image;
    }
    // fit a bitmap to the sheet and display it
    public void fitAndDisplay(Bitmap image) {
        image = menu.drawMenu(image);

    }
    // temporarly turn the whole screen to black to take a picture of the sheet
    public void blackOut() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screen.setImageBitmap(black);
            }
        });
    }
    // restore to previous screen after a black out
    public void restore() {
        display(currentImage);
    }

    public void initScreen(projection act) {
        activity = act;
        screen = activity.getImageView();
        // create a black bitmap for black out
        black = Bitmap.createBitmap(4,4, Bitmap.Config.ARGB_8888);
        for(int i=0;i<4; i++) {
            for(int j=0;j<4;j++)
                black.setPixel(i,j, Color.BLACK);
        }

    }
}
