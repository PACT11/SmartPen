package apps;

import android.graphics.Bitmap;

import netzwerk.Connector;
import pact.smartpen.projection;
import remote.messages.Image;
import view.CloudServices;

/**
 * Created by arnaud on 07/03/15.
 */
public class Share extends NetworkApp {
    @Override
    protected void onLaunch() {
        configureRemoteListeners(server);

        menu.addItem("Morpion");
        menu.addItem("Déconnection");
        menu.setAppName("Partage");

        inputScreen.restart(Pactivity);
    }

    protected void onNewImage(Bitmap image) {
        cloud.straigthenAndSend(image,image.getWidth()/2,image.getHeight()/2);
    }
    @Override
    protected void onImageReceived(Bitmap image){
        outputScreen.fitAndDisplay(image);
    }

    @Override
    protected void onMenuClick(String menu) {
        if(menu.equals("Quitter")) {
            disconnectFromUser();
            Pactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Pactivity.finish();
                }
            });
        } else if(menu.equals("Morpion")) {
            os.startApp("Morpion");
        }
    }

    @Override
    protected void onConnectionClosure(final String distantUID){
        inputScreen.close();
        ((Login)os.getApp("Login")).resume();
        Pactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Pactivity.userDisconnected(distantUID);
            }
        });
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
}
