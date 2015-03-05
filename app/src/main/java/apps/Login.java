package apps;

import remote.RemotePen;
import remote.server.Server;

/**
 * Created by arnaud on 04/03/15.
 */
public class Login extends Application {
    private RemotePen server;
    private Object lock = new Object();
    private String UID = "";
    private String password;
    private boolean isRegistered= false;
    private String[] users;
    @Override
    protected void onLaunch() {
        server = new RemotePen("connectionAgent");
        server.connect(RemotePen.DEFAULTIP,2323);
        userChecker();
    }
    private void userChecker() {
        do {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!UID.equals("_close")) {
                isRegistered = server.isRegistered(UID,password);
                synchronized (lock) {
                    lock.notify();
                }
            }
        } while (!isRegistered);
        server.sendMessage(new remote.messages.UID(UID));
        users = server.getConnectedUsers();
    }
    private void listUpdater() {
        do {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!UID.equals("_close")) {
                isRegistered = server.isRegistered(UID,password);
                synchronized (lock) {
                    lock.notify();
                }
            }
        } while (!isRegistered);

    }
    public boolean checkUser(String user, String password) {
        this.UID = user;
        this.password = password;
        synchronized (lock) {
            lock.notify();
        }
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isRegistered;
    }
    @Override
    protected void onClose() {
        server.close();
    }
    public String[] getUsers() {
        return users;
    }
    public void close() {
        menu.debugClick("close");
    }
}
