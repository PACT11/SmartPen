/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.transformation;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author arnaud
 */
public class RansacCache {
    public static final int STEP = 8;
    public static final float THRESHOLD = (float) 0.1;
    
    private HashMap<String,ArrayList<Point>> cornersCache = new HashMap<>();
    private HashMap<String,BufferedImage> imageCache = new HashMap<>();
    
    public static void init() {
        MainRansac.cache = new RansacCache();
    }
    public boolean needUpdate(BufferedImage image, String UID) {
        if(cornersCache.get(UID)==null || imageCache.get(UID)==null) {
            return true;
        } else if(correlation(imageCache.get(UID), image,STEP)>THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }
    private float correlation(BufferedImage image, BufferedImage newImage, int step) {
        int total=0;
        int norm=0;
        for(int i=0; i<image.getWidth(); i+=step) {
            for(int j=0; j<image.getHeight(); j+=step) {
                total = abs((image.getRGB(i, j))&0xFF-(newImage.getRGB(i, j))&0xFF);
                norm = (image.getRGB(i, j))&0xFF;
            }
        }
        System.out.println("RansacCache : correlation performed : " + ((float)total)/((float)norm));
        return ((float)total)/((float)norm);
    }
    public void update(String UID, BufferedImage image, ArrayList<Point> corners) {
        cornersCache.put(UID, corners);
        imageCache.put(UID, image);
    }
    public ArrayList<Point> getCorners(String UID) {
        return cornersCache.get(UID);
    }
}
