
package remote.tasks;

import view.ApplicationHomographie;
import view.CornerFinder;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import netzwerk.ServerClient;
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
            resultImage = ApplicationHomographie.partieEntiere(image, sheetCorners, CornerFinder.cache.getCorners(server.getUID()), width,height);
            server.sendMessage(new FitToSheet(resultImage));
        }
        
    }
}
