
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
public class StraightenAndSendTask extends CloudTask {
    private int width;
    private int height;
    private BufferedImage image;
    private boolean sendBack;
    
    public StraightenAndSendTask(ServerClient serverCallbackRef, BufferedImage image, int width, int height, boolean sendBack) {
        super(serverCallbackRef);
        
        this.sendBack=sendBack;
        this.width=width;
        this.height=height;
        this.image = image;
    }
    @Override
    public void run() {
        BufferedImage resultImage;
        ArrayList<Point> sheetCorners;
        ArrayList<Point> targetCorners = new ArrayList<>();
        targetCorners.add(new Point(0,0));
        targetCorners.add(new Point(0,height));
        targetCorners.add(new Point(width,0));
        targetCorners.add(new Point(width,height));
        
        try {
            // find corners
            long tempsDebut = System.currentTimeMillis();
            
            sheetCorners = CornerFinder.findCorners(image,false);
            CornerFinder.cache.update(server.getUID(), sheetCorners);
            
            
            long tempsFin = System.currentTimeMillis();
            //display Result
            System.out.println("coins trouvés en: "+ (tempsFin - tempsDebut) + " ms");
            for(Point point : sheetCorners) {
                System.out.println("x : " + point.x + ", y : " + point.y);
            }

            // transform image
            resultImage = ApplicationHomographie.partieEntiere(image,sheetCorners,targetCorners, width, height,true);
            System.out.println("image transformée en: "+ (System.currentTimeMillis() - tempsFin) + " ms");
            
            if(sendBack) {
                // send back to the user where the corners are and the straightened image
                server.sendMessage(new StraightenAndSend(sheetCorners,resultImage));
            } else {
                // send back to the user where the corners are
                server.sendMessage(new StraightenAndSend(sheetCorners,null));
            }
            
            // send it to all connected users
            server.getConnection().sendMessage(new Image(resultImage), server);
        } catch (Exception ex) {
            System.err.println("StraightenAndSendTask : cannot finish task from " + server.getUID());
            System.err.println("        No sheet detected !");
            System.err.println(ex);
            server.sendMessage(new StraightenAndSend(null,null));
        }
        
    }
    
}
