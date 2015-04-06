package apps;

import android.graphics.Bitmap;
import netzwerk.Connector;
import pact.smartpen.projection;
import remote.messages.Image;
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
        server = Login.getServer();
        configureRemoteListeners(server);
        cloud = new CloudServices(server);
        menu.addItem("coucou");
        menu.addItem("SmartPen");

        if(server.getUID().equals("pact"))
            inputScreen.restart(activity);
    }
    protected void onNewImage(Bitmap image) {
        // Bitmap img = Bitmap.createScaledBitmap(image,3264/2,2448/2,false);
        //outputScreen.display(img);
        cloud.straigthenAndSend(image,1200,800);

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
    protected void onMenuClick(String menu) {

    }

    @Override
    protected void onImageReceived(final Bitmap image){
        if(server.getUID().equals("pact")) {

            outputScreen.fitAndDisplay(image);
        } else {
            outputScreen.display(Bitmap.createScaledBitmap(image, 1200, 800, false));
            server.sendMessage(new Image(image));
        }
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
