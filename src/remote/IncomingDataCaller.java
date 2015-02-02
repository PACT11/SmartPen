
package remote;

import remote.messages.Message;
import remote.listeners.MessageListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arnaud
 */
public class IncomingDataCaller extends Thread {
    boolean running;
    ObjectInputStream in;
    MessageListener listener;
    Runnable closeListener;
    
    public IncomingDataCaller(ObjectInputStream in) {
        this.in = in;
    }
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                Message message = (Message) in.readObject();
                if(listener!=null)
                    listener.messageReceived(message);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Server : closed input stream");
                running = false;
                if(closeListener != null)
                    closeListener.run();
            }
        }
    }
    public void close() {
        running = false;
    }
    public void addCloseListener(Runnable listener) {
        closeListener = listener;
    }
    public void addMessageListener(MessageListener listener) {
        this.listener = listener;
    }
}
