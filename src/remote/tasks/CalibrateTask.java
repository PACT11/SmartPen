
package remote.tasks;

import view.ApplicationHomographie;
import view.CornerFinder;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import netzwerk.ServerClient;
import remote.messages.Image;
import remote.messages.StraightenAndSend;

/**
 *
 * @author arnaud
 */
public class CalibrateTask extends CloudTask {
    private BufferedImage image;
    
    public CalibrateTask(ServerClient serverCallbackRef, BufferedImage image) {
        super(serverCallbackRef);

        this.image = image;
    }
    @Override
    public void run() {
        ArrayList<Point> sheetCorners;
        
        try {
            // find corners
            long tempsDebut = System.currentTimeMillis();
            
            sheetCorners = CornerFinder.findCorners(image,true);
            CornerFinder.cache.updateCalibration(server.getUID(), sheetCorners);
                
            long tempsFin = System.currentTimeMillis();
            //display Result
            System.out.println(" >> Screen corners found in: "+ (tempsFin - tempsDebut) + " ms");
            for(Point point : sheetCorners) {
                System.out.println("x : " + point.x + ", y : " + point.y);
            }
        } catch (Exception ex) {
            System.err.println("CalibrateTask : cannot finish task from " + server.getUID());
            System.err.println("        Cannot detect screen limits !");
            System.err.println(ex);
        }
        
    }
    
}

