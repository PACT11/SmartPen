/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.messages;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.tasks.TransformAndSendTask;
import view.Bitmap;

/**
 *
 * @author arnaud
 */
public class TransformAndSend  extends Message {
    private int[] pointsx;
    private int[] pointsy;
    private int width;
    private int height;
    private byte[] img;
    
    public TransformAndSend(Bitmap img) {
        
    }
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            TransformAndSendTask task = new TransformAndSendTask(client,
                    ImageIO.read(in),pointsx,pointsy,width,height);
            task.start();
        } catch (IOException ex) {
            System.err.println("Transform and send : error while reading image");
            System.err.println(ex);
        }
 
    }

    @Override
    public void onClientReceive(Connector client) {
        
    }
    
}
