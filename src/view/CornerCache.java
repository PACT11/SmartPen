
package view;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import remote.messages.CheckUser;

/*
 *
 */
public class CornerCache {
    private HashMap<String,ArrayList<Point>> cornersCache = new HashMap<>();
    private HashMap<String,ArrayList<Point>> calibrationCache = new HashMap<>();
    
    public static void init() {
        CornerFinder.cache = new CornerCache();
    }
    public void update(String UID, ArrayList<Point> corners) {
        cornersCache.put(UID, corners);
    }
    public ArrayList<Point> getCorners(String UID) {
        return cornersCache.get(UID);
    }
    
    public void updateCalibration(String UID, ArrayList<Point> corners) {
        calibrationCache.put(UID, corners);
        writeScreenCorners(corners, UID);
    }
    
    public ArrayList<Point> getScreenCorners(String UID) {
        ArrayList<Point> corners;
        if(calibrationCache.get(UID)!=null)
            corners = calibrationCache.get(UID);
        else
            corners = readScreenCorners(UID);
        
        ArrayList<Point> result = new ArrayList<>();
        result.add(corners.get(3));
        result.add(corners.get(2));
        result.add(corners.get(1));
        result.add(corners.get(0));
        
        return result;
    }
    private ArrayList<Point> readScreenCorners(String UID) {
        ArrayList<Point> screenCorners = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(CheckUser.usersFile.replaceFirst("users", UID + "Corners"))); // open the file

            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if(line.split(" ").length == 2) {
                    int x = Integer.parseInt(line.split(" ")[0]);
                    int y = Integer.parseInt(line.split(" ")[1]);
                    screenCorners.add(new Point(x,y));
                }
            }
            reader.close();
        } catch(java.io.FileNotFoundException e) {
            System.err.println("FitToScreen : opening error : file not found");
        } catch (java.io.IOException|java.lang.NumberFormatException e) {
            System.err.println("FitToScreen : opening error : invalid file");
        }
        return screenCorners;
    }
    private void writeScreenCorners(ArrayList<Point> corners, String UID) {
        String fileName = CheckUser.usersFile.replaceFirst("users", UID + "Corners");
        
        try {
            PrintWriter writer;
            writer =  new PrintWriter(new BufferedWriter(new FileWriter(fileName))); // open a writer into the specified file
            for(Point point : corners) {
                writer.println(point.x + " " + point.y);
            }
            writer.close();
        } catch (java.io.IOException ex) {
            System.err.println("saving file error : cannot write the file");
        }
    }
}
