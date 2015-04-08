
package remote.tasks;

import view.ApplicationHomographie;
import view.CornerFinder;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import netzwerk.ServerClient;
import remote.messages.CheckUser;
import remote.messages.FitToSheet;

/**
 *
 * @author arnaud
 */
public class FitToSheetTask extends CloudTask {
    private BufferedImage image;
    private int width;
    private int height;
    
    public FitToSheetTask(ServerClient serverCallbackRef, BufferedImage image, int width, int height) {
        super(serverCallbackRef);
        this.image = image;
        this.width=width;
        this.height=height;
    }
    @Override
    public void run() {
        if(CornerFinder.cache.getCorners(server.getUID())==null) {
            server.sendMessage(new FitToSheet(null));
        } else {
            BufferedImage resultImage;
            ArrayList<Point> sheetCorners = new ArrayList<>();
            sheetCorners.add(new Point(0,0));
            sheetCorners.add(new Point(0,image.getHeight()));
            sheetCorners.add(new Point(image.getWidth(),0));
            sheetCorners.add(new Point(image.getWidth(),image.getHeight()));
            /*ArrayList<Point> targetCorners = new ArrayList<>();
            targetCorners.add(new Point(0,0));
            targetCorners.add(new Point(0,height));
            targetCorners.add(new Point(width,0));
            targetCorners.add(new Point(width,height));*/
            ArrayList<Point> screenCorners = readScreenCorners();
            
            //ArrayList<Point> transformedCorners = ApplicationHomographie.transformPoints(CornerFinder.cache.getCorners(server.getUID()), screenCorners, sheetCorners);
            resultImage = ApplicationHomographie.partieEntiere(image, sheetCorners, screenCorners, 1200,1100);
            server.sendMessage(new FitToSheet(resultImage));
        }
    }
    private ArrayList<Point> readScreenCorners() {
        ArrayList<Point> screenCorners = new ArrayList<>();
        
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(CheckUser.usersFile.replaceFirst("users", "screenCorners"))); // open the file

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
}
