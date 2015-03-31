package apps;

import android.graphics.Bitmap;
import netzwerk.Connector;
import pact.smartpen.projection;
import remote.messages.StraightenAndSend;
import view.CloudServices;

/**
 * Created by arnaud on 07/03/15.
 */
public class Share extends Application {
    private Connector server;
    private CloudServices cloud;
    private projection activity;

    @Override
    protected void onLaunch() {
        server = ((Login)os.getApp("Login")).getServer();
        configureRemoteListeners(server);
        cloud = new CloudServices(server);

        if(server.getUID().equals("pact"))
            inputScreen.restart();
    }
    protected void onNewImage(Bitmap image) {
        Bitmap img = Bitmap.createScaledBitmap(image,image.getWidth()/2,image.getHeight()/2,false);
        outputScreen.display(img);
        cloud.straigthenAndSend(img,1200,800);
    }
    @Override
    protected void onConnectionClosure(final String distantUID){
        inputScreen.close();
        ((Login)os.getApp("Login")).resume();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.userDisconnected(distantUID);
            }
        });
    }
    protected void onCommandReceived(String command){

    }
    @Override
    protected void onImageReceived(final Bitmap image){
        outputScreen.display(Bitmap.createScaledBitmap(image, 1200, 800, false));
    }
    public void disconnectFromUser() {
        inputScreen.close();
        handler.post(new Runnable() {
            @Override
            public void run() {
                server.disconnectFromUser();
            }
        });
        ((Login)os.getApp("Login")).resume();
    }
    @Override
    protected void onClose() {

    }
    public void setActivity(projection acti) {
        activity = acti;
    }
}
