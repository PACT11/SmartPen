package pact.smartpen;

/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    public void run() {
        remote.ipSync.IPsyncClient.main(null);
    }
}
