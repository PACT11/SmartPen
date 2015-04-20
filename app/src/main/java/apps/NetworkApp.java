package apps;

import netzwerk.Connector;
import pact.smartpen.SmartPen;
import pact.smartpen.list;
import pact.smartpen.projection;
import remote.messages.UserListUpdate;
import view.CloudServices;


public class NetworkApp extends Application {
    // server's address
    //public static final byte[] serverIP = {(byte)10,(byte)0,(byte)1,(byte)4};
    //public static final byte[] serverIP = {(byte)137,(byte)194,(byte)16,(byte)226};
    public static final byte[] serverIP = {(byte)192,(byte)168,(byte)43,(byte)62};
    //public static final byte[] serverIP = {(byte)192,(byte)168,(byte)56,(byte)1};

    protected static Connector server;        // the server we connect to
    protected static CloudServices cloud;

    protected static String UID = "";         // the name of the user
    protected static String distantUID;
    protected String[] users;          // a list of all connected users

    // references to the activities for GUI callbacks
    protected static list Lactivity;
    protected static SmartPen Sactivity;
    protected static projection Pactivity;

    @Override
    protected void onLaunch() {
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
    // getters and setters
    public String[] getUsers() {
        int cnt=1;
        for(String user : users) {
            if (!user.equals(UID))
                cnt++;
        }
        String[] filteredUsers= new String[cnt];
        filteredUsers[0] = "Calibration de la projection";
        cnt = 1;
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
    public void setSmartPenActivity(SmartPen activity) {
        this.Sactivity = activity;
    }
    public void setProjectionActivity(projection activity) {
        this.Pactivity = activity;
    }
}