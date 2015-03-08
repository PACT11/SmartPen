package pact.smartpen;

import android.os.Looper;
import android.os.Handler;

import apps.Application;
import apps.SmartPen;


/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    public void run() {
        Looper.prepare();
        Application.handler = new Handler();
        // launch OS
        SmartPen.main(null);
        Looper.loop();
    }
}
