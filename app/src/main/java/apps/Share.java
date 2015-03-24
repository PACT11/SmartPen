package apps;

import android.graphics.Bitmap;

import java.util.ArrayList;

import netzwerk.Connector;
import pact.smartpen.projection;
import remote.messages.Command;
import remote.messages.Image;
import shape.ApplicationHomographie;
import shape.MainRansac;

import shape.Point;

/**
 * Created by arnaud on 07/03/15.
 */
public class Share extends Application {
    private Connector server;
    private projection activity;
    private boolean working;
    private Bitmap img;
    ArrayList<Point> coinsDepart;
    ArrayList<Point> coinsArrive;
    @Override
    protected void onLaunch() {
        working = false;
        server = ((Login)os.getApp("Login")).getServer();
        configureRemoteListeners(server);
        coinsArrive = new ArrayList<>();
        coinsArrive.add(new Point(30, 0));
        coinsArrive.add(new Point(0, 390));
        coinsArrive.add(new Point(730, 0));
        coinsArrive.add(new Point(800, 390));
        if(server.getUID().equals("pact"))
            inputScreen.restart();
        else
            server.sendMessage(new Command("get"));
    }
    protected void onNewImage(Bitmap image) {
        img = Bitmap.createScaledBitmap(image,image.getWidth()/4,image.getHeight()/4,false);
        outputScreen.display(img);
        //if(!working) {
            server.sendMessage(new Image(img));
        //    working= true;
        //}
        /*if(!ransac) {
            ransac = true;
            img = Bitmap.createScaledBitmap(image, image.getWidth()/4, image.getHeight()/4, false);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    performRansac(img);
                    server.sendMessage(new Ransac(coinsDepart));
                    ransac = false;
                }
            });
        }*/
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
        working = false;
    }
    @Override
    protected void onImageReceived(final Bitmap image){
        if(!working) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    working = true;
                    System.out.println("Ransac ...");
                    performRansac(Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false));
                    System.out.println("done");
                    if(coinsDepart.get(0).isOrigin()||coinsDepart.get(1).isOrigin()||coinsDepart.get(2).isOrigin()||coinsDepart.get(3).isOrigin()) {
                        working=false;
                        return;
                    }
                    //Bitmap myBmp = ManipulationImage.Binariser(image);
                    System.out.println("homographie ...");
                    Bitmap myBmp = ApplicationHomographie.partieEntiere(image, coinsDepart, coinsArrive);
                    System.out.println("done");
                    outputScreen.display(myBmp);
                    server.sendMessage(new Command("get"));
                    working = false;
                }
            });
        }
        //}
        //outputScreen.display(Bitmap.createScaledBitmap(image, 800, 480, false));
        //server.sendCommand("get");
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
    public void performRansac(Bitmap image) {
        coinsDepart = MainRansac.obtentionCoins(image);
    }
}
