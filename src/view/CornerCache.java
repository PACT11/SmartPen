/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author arnaud
 */
public class CornerCache {
    private HashMap<String,ArrayList<Point>> cornersCache = new HashMap<>();
    
    public static void init() {
        CornerFinder.cache = new CornerCache();
    }
    public void update(String UID, ArrayList<Point> corners) {
        cornersCache.put(UID, corners);
    }
    public ArrayList<Point> getCorners(String UID) {
        return cornersCache.get(UID);
    }
}
