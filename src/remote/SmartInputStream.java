/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import remote.listeners.MessageListener;
import java.io.*;

/**
 *
 * @author arnaud
 */
public class SmartInputStream extends ObjectInputStream {
    IncomingDataCaller caller;
    
    public SmartInputStream(InputStream in) throws IOException {
        super(in);
        caller = new IncomingDataCaller(this);
        caller.start();
    }
    public void addMessageListener(MessageListener listener) {
        caller.addMessageListener(listener);
    }
    public void addCloseListener(Runnable listener) {
        caller.addCloseListener(listener); 
    }
    @Override
    public void close() {
        caller.close();
        try {
            super.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
