/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.tasks;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import netzwerk.ServerClient;
import remote.messages.Image;

/**
 *
 * @author arnaud
 */
public class TransformAndSendTask extends CloudTask {
    ArrayList<Point> corners = new ArrayList<>();
    private int width;
    private int height;
    private BufferedImage image;
    
    public TransformAndSendTask(ServerClient serverCallbackRef, BufferedImage image,int[] pointx, int[] pointy, int width, int height) {
        super(serverCallbackRef);
        for(int i=0;i<4;i++)
            corners.add(new Point(pointx[i],pointy[i]));
        this.width=width;
        this.height=height;
        this.image = image;
    }
    @Override
    public void run() {
        BufferedImage resultImage=null;
        // TODO
        server.sendMessage(new Image(resultImage));
    }
    
}
