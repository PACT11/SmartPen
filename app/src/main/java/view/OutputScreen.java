
package view;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
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
    private Bitmap currentScreen;
    private Bitmap straightImage;
    private CloudServices cloud;
    private boolean isBlackOut=false;

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
        currentScreen = image;
    }
    // fit a bitmap to the sheet and display it
    public void fitAndDisplay(Bitmap image) {
        straightImage = image;
        cloud.fitToSheet(menu.drawMenu(image));
    }
    // temporarly turn the whole screen to black to take a picture of the sheet
    public void blackOut() {
        isBlackOut=true;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screen.setImageBitmap(black);
            }
        });
    }
    // restore to previous screen after a black out
    public void restore() {
        if(isBlackOut) {
            display(currentScreen);
            isBlackOut = false;
        }
    }
    // update the screen
    public void refresh() {
        fitAndDisplay(straightImage);
    }
    public void initScreen(projection act) {
        activity = act;
        screen = activity.getImageView();
        // create a black bitmap for black out
        black = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        for(int i=0;i<100; i++) {
            for(int j=0;j<100;j++)
                black.setPixel(i,j, Color.BLACK);
        }

    }
    public static Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void setCloud(CloudServices cloud) {
        this.cloud = cloud;
    }

    public MenuBar getMenu() {
        return menu;
    }

    public Bitmap getImage() {
        return straightImage;
    }
    public static Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
