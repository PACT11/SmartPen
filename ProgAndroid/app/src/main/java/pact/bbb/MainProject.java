package pact.bbb;

import remote.RemotePen;


/**
 * Created by arnaud on 18/02/15.
 */
public class MainProject extends Thread {
    private String password;
    private String UID;
    private boolean close = false;
    private boolean check = false;
    private Object lock = new Object();
    private boolean isRegistered = false;

    public void run() {
        byte[] ip = {(byte)127,(byte)0,(byte)0,(byte)1};
        RemotePen server = new RemotePen("connectionAgent");
        server.connect(ip,2323);

        while(!close) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(check) {
                isRegistered= server.isRegistered(UID, password);
                synchronized (lock) {
                    lock.notify();
                }
                check = false;
            }
        }

        server.close();
    }

    public boolean checkUser(String UID, String password) {
        this.password = password;
        this.UID = UID;
        synchronized (lock) {
            lock.notify();
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isRegistered;
    }
    public void close() {
        close = true;
        synchronized (lock) {
            lock.notify();
        }
    }
}
