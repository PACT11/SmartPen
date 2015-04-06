/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.messages;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.listeners.ImageReceiveListener;
import remote.tasks.FitToSheetTask;
import view.Bitmap;

/**
 *
 * @author arnaud
 */
public class FitToSheet extends Message{
    
    static final long serialVersionUID= 4298568912358L;
    private static Runnable errorListener;
    private static ImageReceiveListener imageListener;
    
    private byte[] img;
    private int width;
    private int height;
    
    public FitToSheet(Bitmap StraightenedSheet, int width, int height) {
        img = Image.getBytesFromBitmap(StraightenedSheet);
        this.height=height;
        this.width=width;
    }
    
    @Override
    public void onServerReceive(ServerClient client) {
        try {
            InputStream in = new ByteArrayInputStream(img);
            FitToSheetTask task = new FitToSheetTask(client,
                    ImageIO.read(in),width,height);
            task.start();
        } catch (IOException ex) {
            System.err.println("Transform and send : error while reading image");
            System.err.println(ex);
        }
    }
    // a constructor for the server, sending back the transformer image
    public FitToSheet(BufferedImage image) {
        if(image!=null) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", out);
                out.flush();
                this.img = out.toByteArray();
                out.close();
            } catch (IOException ex) {
                System.err.println("FitToSheet : error while converting image");
                System.err.println(ex);
            }
        }
    }
    
    @Override
    public void onClientReceive(Connector client) {
        if(img!=null && imageListener!=null) {
            //Bitmap imgrecue = BitmapFactory.decodeByteArray(image, 0, image.length);
            //imageListener.imageReceived(imgrecue);
        } else if(img==null && errorListener!=null) {
            errorListener.run();
        }
    }
    
    public void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
    public void setImageListener(ImageReceiveListener listener) {
        imageListener = listener;
    }
}
