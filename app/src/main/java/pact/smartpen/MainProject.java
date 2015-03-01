package pact.smartpen;

import android.graphics.Bitmap;

import remote.RemotePen;


/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    public void run() {

        byte[] ip = {(byte)10,(byte)0,1,4};
        RemotePen server = new RemotePen("connectionAgent");
        server.connect(ip,2323);
        System.out.println("Test : " + server.isRegistered("pact","pact"));
        System.out.println("Test : " + server.isRegistered("pact","lol"));
        server.close();
    }
}
