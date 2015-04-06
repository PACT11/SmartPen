/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private boolean useCache;
    
    public StraightenAndSendTask(ServerClient serverCallbackRef, BufferedImage image, int width, int height, boolean useCache) {
        super(serverCallbackRef);
        
        this.useCache= useCache;
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
            if(useCache) {
                sheetCorners=CornerFinder.cache.getCorners(server.getUID());
            } else {
                sheetCorners = CornerFinder.findCorners(image);
                CornerFinder.cache.update(server.getUID(), sheetCorners);
                // send back to the user where the corners are
                server.sendMessage(new StraightenAndSend(sheetCorners));
            }
            long tempsFin = System.currentTimeMillis();
            //display Result
            System.out.println("coins trouvés en: "+ (tempsFin - tempsDebut) + " ms");
            for(Point point : sheetCorners) {
                System.out.println("x : " + point.x + ", y : " + point.y);
            }
            
            // transform image
            resultImage = ApplicationHomographie.partieEntiere(image,sheetCorners,targetCorners, width, height);
            System.out.println("image transformée en: "+ (System.currentTimeMillis() - tempsFin) + " ms");
            
            // send it to all connected users
            server.getConnection().sendMessage(new Image(resultImage), server);
        } catch (Exception ex) {
            System.err.println("StraightenAndSendTask : cannot finish task from " + server.getUID());
            System.err.println("        No sheet detected !");
            System.err.println(ex);
            server.sendMessage(new StraightenAndSend(null));
        }
        
    }
    
}
