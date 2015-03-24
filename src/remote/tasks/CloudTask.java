
package remote.tasks;

import netzwerk.ServerClient;

/**
 *
 * @author arnaud
 */
public abstract class CloudTask extends Thread {
    protected ServerClient server;
    public CloudTask(ServerClient serverCallbackRef) {
        server = serverCallbackRef;
    }
    @Override
    public void run() {
        
    }
    
}
