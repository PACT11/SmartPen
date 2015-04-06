package apps;

import java.net.UnknownHostException;

import netzwerk.Connector;
import netzwerk.messages.*;
import pact.smartpen.*;
import remote.messages.CheckUser;
import remote.messages.UserListUpdate;


public class Login extends Application {
    // server's address
    public static final byte[] serverIP = {(byte)10,(byte)0,(byte)1,(byte)4};
    //public static final byte[] serverIP = {(byte)137,(byte)194,(byte)17,(byte)15};
    //public static final byte[] serverIP = {(byte)192,(byte)168,(byte)42,(byte)192};
    //public static final byte[] serverIP = {(byte)192,(byte)168,(byte)56,(byte)1};

    private static Connector server;        // the server we log in
    private String UID = "";         // the name of the user
    private String[] users;          // a list of all connected users
    // references to the cativities for GUI callbacks
    private list Lactivity;
    private pact.smartpen.SmartPen Sactivity;

    @Override
    protected void onLaunch() {
        // start an anonymous connection to the server
        server = new Connector("connectionAgent");

        try {
            server.connect(serverIP,2323);
        } catch (UnknownHostException e) {  // if we cannot connect to the server
            if(Sactivity!=null) {
                Sactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Sactivity.serverUnreachable();
                    }
                });
            }
        }
        // handle connections list updates
        UserListUpdate.setListener( new UserListUpdate.UserListListener() {
            @Override
            public void onUpdate(String[] users) {
                onUserListUpdate(users);
            }
        });
    }
    public void resume() {
        // reconfigure listeners for login app
        configureRemoteListeners(server);
    }
    public void checkUser(String user, final String password) {
        this.UID = user;
        handler.post(new Runnable() {
            @Override
            public void run() {
                final boolean isRegistered = CheckUser.check(UID, password,server);
                if(isRegistered) {
                    // modified ID to match user's
                    server.setUID(UID);
                    // get the user list (blocking to be sure to have something to display on list activity launch)
                    users = UserList.get(server);
                    // send a notification to all connected users to udpate their connection list
                    server.sendMessage(new UserListUpdate());
                    // from now on we'll respond to connection requests/answers
                    configureRemoteListeners(server);
                }
                // GUI callback
                Sactivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Sactivity.checkUserCallback(isRegistered);
                    }
                });
            }
        });
    }
    @Override
    protected void onConnectionRequest(final String distantUID){
        if(Lactivity==null) {               // if the list activity has not been loaded yet, refuse connection
            server.acceptConnection(false);
        } else {
            //show GUI dialog
            Lactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Lactivity.showConnectionRequestDialog(distantUID);
                }
            });
        }
    }
    @Override
    protected void onConnectionAnswer(final short answer){
        if(Lactivity!=null) {
            //show GUI dialog
            Lactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Lactivity.processConnectionAnswer(answer == ConnectionAnswer.ACCEPT);
                }
            });
        }
    }
    private void onUserListUpdate(final String[] users) {
        this.users = users;
        if(Lactivity!=null) {
            Lactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Lactivity != null)
                        Lactivity.updateUsers(getUsers());
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
        this.Lactivity = activity;
    }
    public void setSmartPenActivity(pact.smartpen.SmartPen activity) {
        this.Sactivity = activity;
    }
    public static Connector getServer() {
        return server;
    }

    @Override
    protected void onClose() {
        //server.close();
    }
}