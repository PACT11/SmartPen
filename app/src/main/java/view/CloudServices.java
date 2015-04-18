package view;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import apps.Application;
import netzwerk.Connector;
import remote.listeners.ImageReceiveListener;
import remote.messages.FitToSheet;
import remote.messages.StraightenAndSend;

/**
 * Created by arnaud on 29/03/15.
 */
public class CloudServices {
    private Connector server;
    private int width=0;
    private int height=0;
    public CloudServices(Connector server) {
        this.server=server;

        StraightenAndSend.setErrorListener(new Runnable() {
            @Override
            public void run() {
                // TODO : display popup message
                System.out.println("Cloud Error : cannot find sheet in picture");
            }
        });
        StraightenAndSend.setCornerListener(new StraightenAndSend.CornerListener() {
            @Override
            public void cornerUpdate(ArrayList<shape.Point> corners) {
                if(Application.inputScreen!=null)
                    Application.inputScreen.setCorners(corners);
            }
        });
        StraightenAndSend.setImageListener(new ImageReceiveListener() {
            @Override
            public void imageReceived(Bitmap image) {
                if(Application.outputScreen!=null && Application.outputScreen.getImage()!=null) {
                    MyCamera.savePicture(fusion(Application.outputScreen.getImage(),image));
                }
            }
        });

        FitToSheet.setErrorListener(new Runnable() {
            @Override
            public void run() {
                // TODO : display popup message
                System.out.println("Cloud Error : cannot transform sheet");
            }
        });
        FitToSheet.setImageListener(new ImageReceiveListener() {
            @Override
            public void imageReceived(Bitmap image) {
                if (Application.outputScreen != null)
                    Application.outputScreen.display(image);
            }
        });

        if (Application.outputScreen != null)
            Application.outputScreen.setCloud(this);
    }
    public void straigthenAndSend(Bitmap image, int width, int height) {
        this.width=image.getWidth();
        this.height=image.getHeight();

        server.sendMessage(new StraightenAndSend(image,width,height,false));
    }
    public void straigthenAndSave(Bitmap image, int width, int height) {
        this.width=image.getWidth();
        this.height=image.getHeight();

        server.sendMessage(new StraightenAndSend(image,width,height,true));
    }

    public void fitToSheet(Bitmap image) {
        if(width>0 && height>0)
            server.sendMessage(new FitToSheet(image, width,height));
    }

    private static Bitmap fusion(Bitmap img1, Bitmap img2) {
        int width = Math.min(img1.getWidth(),img2.getWidth());
        int height = Math.min(img1.getHeight(),img2.getHeight());
        Bitmap result = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        int luminance;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                luminance = Math.min(luminance(img1.getPixel(i,j)),luminance(img2.getPixel(i,j)));
                result.setPixel(i,j,Color.argb(255,luminance,luminance,luminance));
            }
        }
        return result;
    }
    private static int luminance(int color) {
        return (int)(0.299*Color.red(color)+0.587*Color.green(color)+0.114*Color.blue(color));
    }
}
