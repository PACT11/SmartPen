package pact.smartpen;

import android.os.Looper;
import android.os.Handler;

import apps.*;
import static apps.Application.applications;

/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    public void run() {
        Looper.prepare();
        Application.handler = new Handler();


        // each app has to do so (except the OS) to allow the OS to display all the available apps
        applications.add(new Login());
        applications.add(new Share());
        applications.add(new Morpion());
        applications.add(new ApprentissageEcriture());
        applications.add(new ApprentissageDessin());
        // launch OS
        new OS().run();

        Looper.loop();
    }
}
