package pact.smartpen;

import android.graphics.Bitmap;

import apps.*;
import apps.SmartPen;
import remote.RemotePen;
import view.InputScreen;
import view.MyCamera;


/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    public void run() {
        // launch OS
        SmartPen.main(null);
    }
}
