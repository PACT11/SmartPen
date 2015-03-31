/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.tasks;

import image.transformation.ApplicationHomographie;
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
    
    public StraightenAndSendTask(ServerClient serverCallbackRef, BufferedImage image, int width, int height) {
        super(serverCallbackRef);
        
        this.width=width;
        this.height=height;
        this.image = image;
    }
    @Override
    public void run() {
        BufferedImage resultImage;
        ArrayList<Point> corners = new ArrayList<>();
        corners.add(new Point(0,0));
        corners.add(new Point(0,height));
        corners.add(new Point(width,0));
        corners.add(new Point(width,height));
        try {
            resultImage = ApplicationHomographie.redressementImage(image, corners, width, height,server.getUID());
            server.getConnection().sendMessage(new Image(resultImage), server);
        } catch (Exception ex) {
            System.err.println("StraightenAndSendTask : cannot finish task from " + server.getUID());
            System.err.println("        No sheet detected !");
            System.err.println(ex);
            server.sendMessage(new StraightenAndSend(null,width,height));
        }
        
    }
    
}
