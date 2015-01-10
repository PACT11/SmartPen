
package view;

import java.util.ArrayList;

/*
 */
public class MenuBar {
    // interface for a listener called when a menu is clicked
    public interface MenuListener {
        public void menuClicked(String menuName);
    }
    
    private Runnable closeListener;
    private MenuListener menuListener;
    private Runnable updateCaller;    // a listener used to provide a link to the inputScreen update method if the menu bar is currently displayed
    private ArrayList<String> items = new ArrayList<>();
    
    public void addMenuListener(MenuListener listener) {
        menuListener = listener;
    }
    public void addCloseListener(Runnable listener) {
        closeListener = listener;
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
            closeListener.run();
        else
            menuListener.menuClicked(menu);
    }
}
