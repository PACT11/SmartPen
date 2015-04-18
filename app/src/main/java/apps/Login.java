package apps;

import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;
import java.net.UnknownHostException;

import netzwerk.Connector;
import netzwerk.messages.*;
import pact.smartpen.projection;
import remote.messages.*;
import view.CloudServices;

/**
 * Created by arnaud on 18/04/15.
 */
public class Login extends NetworkApp {

    @Override
    protected void onLaunch() {
        super.onLaunch();

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

        // initialize cloud services
        cloud = new CloudServices(server);
    }

    public void checkUser(String user, final String password) {
        this.UID = user;
        handler.post(new Runnable() {
            @Override
            public void run() {
                final boolean isRegistered = CheckUser.check(UID, password, server);
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
            this.distantUID = distantUID;
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

    public void answerConnectionRequest(final boolean isAccepted) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                server.acceptConnection(isAccepted);
            }
        });
    }
    public void connectTo(final String targetUser) {
        this.distantUID = targetUser;

        handler.post(new Runnable() {
            @Override
            public void run() {
                server.connectToUser(targetUser);
            }
        });
    }
    public void calibrate(final projection proj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    android.hardware.Camera camera = Camera.open();
                    SurfaceTexture texture = new SurfaceTexture(23);
                    camera.setPreviewTexture(texture);
                    camera.startPreview();
                    outputScreen.greenScreen();
                    Thread.sleep(700);
                    camera.takePicture(null,null,new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            server.sendMessage(new CalibrateScreen( BitmapFactory.decodeByteArray(data, 0, data.length)));
                            proj.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    proj.finish();
                                }
                            });
                        }
                    });
                } catch (IOException | InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        });
    }
}
