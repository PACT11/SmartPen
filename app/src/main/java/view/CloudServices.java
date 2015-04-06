package view;

import android.graphics.Bitmap;

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
    public void fitToSheet(Bitmap image) {
        if(width>0 && height>0)
            server.sendMessage(new FitToSheet(image, width,height));
    }
}
