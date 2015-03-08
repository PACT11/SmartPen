
package view;
import android.graphics.Bitmap;
import android.widget.ImageView;

import pact.smartpen.projection;

/*
 * 
 */
public class OutputScreen {
    private MenuBar menu;
    private projection activity;
    private ImageView screen;

    public void setMenuBar(MenuBar menu) {
        System.out.println("OutputScreen : new menu bar set");
        // prevent a previous menu bar from requesting an update
        if(this.menu!=null)
            menu.addUpdateRequestListener(null);
        this.menu = menu;
        // allow this menu object to update the screen by providing a link to update() 
        //menu.addUpdateRequestListener(new Runnable() {
        //    @Override
        //    public void run() {
        //        update();
        //    }
        //});
    }
    public void display(final Bitmap image) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screen.setImageBitmap(image);
            }
        });
    }
    // temporarly turn the whole screen to black to take a picture of the sheet
    public void blackOut() {
        System.out.println("OutputScreen : black out");
    }
    // restore to previous screen after a black out
    public void restore() {
        System.out.println("OutputScreen : screen restored from black out");
    }
    public void updateTransformation(Transformation newTransformation) {
        System.out.println("OutputScreen : updated transformation to compute");
    }
    public void initScreen(projection act) {
        activity = act;
        screen = activity.getImageView();
    }
}
