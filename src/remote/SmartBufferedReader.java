/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author arnaud
 */
public class SmartBufferedReader extends BufferedReader {
    IncomingDataCaller caller;
    public SmartBufferedReader(Reader in, int sz) {
        super(in, sz);
    }
    public SmartBufferedReader(Reader in) {
        super(in);
    }
    public void addDataListener(DataListener listener) {
        if(caller!=null) {
            caller.addDataListener(listener);
        } else {
            caller = new IncomingDataCaller(this, listener);
            caller.start();
        }
    }
    public void addCloseListener(Runnable listener) {
        if(caller!=null) {
            caller.addCloseListener(listener);
        } else {
            caller = new IncomingDataCaller(this, null);
            caller.addCloseListener(listener);
            caller.start();
        }
            
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
