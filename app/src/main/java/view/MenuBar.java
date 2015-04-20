
package view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import apps.Application;
import apps.NetworkApp;

import static java.lang.Math.floor;

/*
 */
public class MenuBar {
    // interface for a listener called when a menu is clicked
    public interface MenuListener {
        public void menuClicked(String menuName);
        public void closeClicked();
    }
    
    private MenuListener menuListener;
    private ArrayList<String> items = new ArrayList<String>();
    private int nbItemMenu;
    private int clickedIndex=-1;
    private String currentApplication;
    private boolean startup = true;
    private boolean writing = false;
    private String distantUID;


    public MenuBar(String appName) {
        currentApplication=appName;
    }
    public void addMenuListener(MenuListener listener) {
        menuListener = listener;
    }
    public void setAppName(String name) {
        currentApplication=name;
    }
    public void setDistantUID(String distantUID) {
        this.distantUID = distantUID;
    }
    public void isWriting(boolean writing) {
        if(this.writing!=writing) {
            this.writing = writing;
            Application.outputScreen.refresh();
        }
    }

    public void addItem(String itemName) {
        items.add(itemName);
        System.out.println("MenuBar : added item " + itemName);
    }
    public void click(int x) {
        int index = (int) floor((float)(x*items.size())/100);
        if(index>=0 && index<items.size() && index!=clickedIndex) {
            clickedIndex= index;
            if(!startup) {      // ignore the click if the menu was just created (user may be clicking on the previous menu)
                menuListener.menuClicked(items.get(index));
                Application.outputScreen.refresh();
            }
        } else if(x<0 && clickedIndex!=-1) {
            clickedIndex=-1;
            Application.outputScreen.refresh();
        }
        startup=false;
    }


    public Bitmap drawMenu(Bitmap feuilleSansMenu){
        Bitmap feuille = OutputScreen.rotateBitmap(feuilleSansMenu,-90);
        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        Paint selectedItemPaint = new Paint(); selectedItemPaint.setColor(Color.BLUE);
        Paint paintCurrent = new Paint(); paintCurrent.setColor(Color.RED);
        Paint paintMenuBackground = new Paint();
        paintMenuBackground.setColor(Color.WHITE);
        paint.setTextSize(14);
        paintCurrent.setTextSize(14);
        selectedItemPaint.setTextSize(14);


        int width =  feuille.getWidth();
        int height =  feuille.getHeight();
        int menuHeight = height/12 ;

        Canvas canvas = new Canvas(feuille);
        Rect background = new Rect(0,0,width,menuHeight);
        canvas.drawRect(background, paintMenuBackground);

        nbItemMenu = items.size();
        for (int i =0; i<nbItemMenu; i++) {
            if (i<(nbItemMenu -1 )) canvas.drawLine((i+1)*width/nbItemMenu,0,(i+1)*width/nbItemMenu, menuHeight/2+5, paint);
            if(i!=clickedIndex)
                canvas.drawText(items.get(i), width/20 + i*width/nbItemMenu, menuHeight/2 , paint);
            else
                canvas.drawText(items.get(i), width/20 + i*width/nbItemMenu, menuHeight/2 , selectedItemPaint);
        }

        canvas.drawLine(0,menuHeight/2+5, width,menuHeight /2 + 5, paint);
        canvas.drawText(currentApplication, width/2 - width/10,  menuHeight-menuHeight/15, paintCurrent);
        if(writing && distantUID!=null) {
            canvas.drawText(distantUID + " écrit ...", width / 15, menuHeight - menuHeight / 15, paintCurrent);
        }

        return OutputScreen.rotateBitmap(feuille,90);
    }

    public ArrayList<String> getItems() {
        return items;
    }
}
