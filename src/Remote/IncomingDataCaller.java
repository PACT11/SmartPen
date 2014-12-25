
package Remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arnaud
 */
public class IncomingDataCaller extends Thread {
    boolean running;
    BufferedReader in;
    DataListener listener;
    public IncomingDataCaller(BufferedReader in, DataListener listener) {
        this.in = in;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                int data = in.read();
                if(data < 0)
                    throw new IOException();
                else 
                    listener.dataReceived((char) data);
            } catch (IOException ex) {
                System.out.println("Server : closed input stream");
            }
        }
    }
    public void close() {
        running = false;
    }
}
