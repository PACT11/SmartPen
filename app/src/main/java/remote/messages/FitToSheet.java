/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.messages;

import netzwerk.Connector;
import netzwerk.ServerClient;
import netzwerk.messages.Message;
import remote.listeners.ImageReceiveListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import view.BufferedImage;

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
    
    public FitToSheet(Bitmap straightenedSheet, int width, int height) {
        img = Image.getBytesFromBitmap(straightenedSheet);
        this.height=height;
        this.width=width;
    }
    // a constructor for the server, sending back the transformer image
    public FitToSheet(BufferedImage image) {
        // ...
    }

    @Override
    public void onServerReceive(ServerClient client) {
        // ...
    }

    
    @Override
    public void onClientReceive(Connector client) {
        if(img!=null && imageListener!=null) {
            Bitmap imgrecue = BitmapFactory.decodeByteArray(img, 0, img.length);
            imageListener.imageReceived(imgrecue);
        } else if(img==null && errorListener!=null) {
            errorListener.run();
        }
    }
    
    public static void setErrorListener(Runnable listener) {
        errorListener = listener;
    }
    public static void setImageListener(ImageReceiveListener listener) {
        imageListener = listener;
    }
}
