package apps;

import android.graphics.Bitmap;

/**
 * Created by arnaud on 07/03/15.
 */
public class Share extends NetworkApp {
    boolean saving = false;
    @Override
    protected void onLaunch() {
        configureRemoteListeners(server);

        menu.addItem("Morpion");
        menu.addItem("Ecriture");
        menu.addItem("Enregistrer");
        menu.addItem("Déconnexion");
        menu.setAppName("Partage");

        inputScreen.restart(Pactivity);
    }

    protected void onNewImage(Bitmap image) {
        if(saving) {
            saving =false;
            cloud.straigthenAndSave(image, image.getWidth()/2,image.getHeight()/2);
        } else {
            cloud.straigthenAndSend(image,image.getWidth()/2,image.getHeight()/2);
        }
    }
    @Override
    protected void onImageReceived(Bitmap image){
        outputScreen.fitAndDisplay(image);
    }

    @Override
    protected void onMenuClick(String menu) {
        if(menu.equals("Déconnexion")) {
            disconnectFromUser();
            Pactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Pactivity.finish();
                }
            });
        } else if(menu.equals("Morpion")) {
            os.startApp("Morpion");
        } else if(menu.equals("Ecriture")){
            os.startApp("ApprentissageEcriture");
        }
        else if(menu.equals("Enregistrer")) {
            saving = true;
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
