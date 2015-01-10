
package remote;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author arnaud
 */
public class IncomingDataCaller extends Thread {
    boolean running;
    BufferedReader in;
    DataListener listener;
    Runnable closeListener;
    public IncomingDataCaller(BufferedReader in, DataListener listener) {
        this.in = in;
        this.listener = listener;
        this.closeListener = closeListener;
    }
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                int data = in.read();
                if(data < 0)
                    throw new IOException();
                else if(listener!=null)
                    listener.dataReceived(data);
            } catch (IOException ex) {
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
    public void addDataListener(DataListener listener) {
        this.listener = listener;
    }
}
