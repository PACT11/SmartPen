package apps;

import pact.smartpen.list;
import remote.RemotePen;
import remote.messages.ConnectionAnswer;
import view.MyCamera;

public class Login extends Application {
    private RemotePen server;
    private Object lock = new Object();
    private String UID = "";
    private String password;
    private boolean isRegistered= false;
    private String[] users;
    private list activity;
    @Override
    protected void onLaunch() {
        server = new RemotePen("connectionAgent");
        server.connect(RemotePen.DEFAULTIP,2323);
    }
    public void resume() {
        System.out.println("pass");
        configureRemoteListeners(server);
        handler.post(new Runnable() {
            @Override
            public void run() {
                users = server.getConnectedUsers();

                synchronized (lock) {
                    lock.notify();
                }
            }
        });
        synchronized (lock)  { try {
            lock.wait(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } }
    }
    public boolean checkUser(String user, String pass) {
        this.UID = user;
        this.password = pass;
        handler.post(new Runnable() {
            @Override
            public void run() {
                isRegistered = server.isRegistered(UID,password);
                if(isRegistered) {
                    server.setUID(UID);
                    users = server.getConnectedUsers();
                    configureRemoteListeners(server);
                }
                synchronized (lock) {lock.notify();}
            }
        });
        synchronized (lock)  { try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } }
        return isRegistered;
    }
    @Override
    protected void onConnectionRequest(final String distantUID){
        if(activity==null) {
            server.acceptConnection(false);
        } else {
            //show GUI dialog
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.showConnectionRequestDialog(distantUID);
                }
            });
        }
    }
    @Override
    protected void onConnectionAnswer(final short answer){
        if(activity!=null) {
            //show GUI dialog
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.processConnectionAnswer(answer == ConnectionAnswer.ACCEPT);
                }
            });
        }
    }

    public void answerConnectionRequest(final boolean isAccepted) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                server.acceptConnection(isAccepted);
            }
        });
    }
    public void connectTo(final String targetUser) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                server.connectToUser(targetUser);
            }
        });
    }

    // getters and setters
    public String[] getUsers() {
        int cnt=0;
        for(String user : users) {
            if (!user.equals(UID))
                cnt++;
        }
        String[] filteredUsers= new String[cnt];
        cnt = 0;
        for(String user : users) {
            if (!user.equals(UID)) {
                filteredUsers[cnt] = user;
                cnt++;
            }
        }
        return filteredUsers;
    }
    public void setListActivity(list activity) {
        this.activity = activity;
    }
    public RemotePen getServer() {
        return server;
    }

    @Override
    protected void onClose() {
        //server.close();
    }
    public void close() {
        menu.debugClick("close");
    }
}
