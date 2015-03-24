
package view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/*
 */
public class MenuBar {
    // interface for a listener called when a menu is clicked
    public interface MenuListener {
        public void menuClicked(String menuName);
        public void closeClicked();
    }
    
    private MenuListener menuListener;
    private Runnable updateCaller;    // a listener used to provide a link to the inputScreen update method if the menu bar is currently displayed
    private ArrayList<String> items = new ArrayList<>();
    private int nbItemMenu;
    private int i=0;
    private String currentApplication;

    public void addMenuListener(MenuListener listener) {
        menuListener = listener;
    }
    public void addUpdateRequestListener(Runnable listener) {
        updateCaller = listener;
    }
    private void updateScreen() {
        updateCaller.run();
    }
    public void addItem(String itemName) {
        items.add(itemName);
        System.out.println("MenuBar : added item " + itemName);
    }
    public void click(Point cap) {
        
    }
    // debug
    public void debugClick(String menu) {
        if(menu.equals("close"))
            menuListener.closeClicked();
        else
            menuListener.menuClicked(menu);
    }

    public Bitmap drawMenu(Bitmap feuille){
        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        Paint paintCurrent = new Paint(); paintCurrent.setColor(Color.RED);
        Paint paintMenuBackground = new Paint(); paintCurrent.setColor(Color.WHITE);
        paint.setTextSize(30);
        paintCurrent.setTextSize(30);

        int width =  feuille.getWidth();
        int height =  feuille.getHeight();
        int menuHeight = (int) height/9 ;

        Canvas canvas = new Canvas(feuille);
        Rect background = new Rect(0,0,width,menuHeight);
        canvas.drawRect(background, paintMenuBackground);
        nbItemMenu = items.size();
        for (i =0; i<nbItemMenu; i++) {
            if (i<(nbItemMenu -1 )) canvas.drawLine((i+1)*width/nbItemMenu,0,(i+1)*width/nbItemMenu, 2 * menuHeight/3, paint);
            canvas.drawText(items.get(i), width/20 + i*width/nbItemMenu, menuHeight/3 , paint);
        }
        canvas.drawLine(0,2* menuHeight /3, width, 2 * menuHeight /3, paint);
        canvas.drawText(currentApplication, width/2 - width/10, (2 * menuHeight/3), paintCurrent);


        return feuille;

    }
}
