package view;

import android.graphics.Bitmap;

import netzwerk.Connector;
import remote.messages.StraightenAndSend;

/**
 * Created by arnaud on 29/03/15.
 */
public class CloudServices {
    private Connector server;
    public CloudServices(Connector server) {
        this.server=server;
        StraightenAndSend.setErrorListener(new Runnable() {
            @Override
            public void run() {
                // TODO : display popup message
                System.out.println("Cloud Error : cannot find sheet in picture");
            }
        });
    }
    public void straigthenAndSend(Bitmap image, int width, int height) {
        server.sendMessage(new StraightenAndSend(image,width,height));
    }
}
