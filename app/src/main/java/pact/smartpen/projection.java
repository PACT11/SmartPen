package pact.smartpen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import apps.Application;
import apps.Login;
import apps.Share;

/**
 * Created by Chab on 24/02/15.
 */
public class projection extends Activity {
    Share share;
    ImageView imageView;
    static boolean calibrate = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projection);
        // initilize projection
        imageView = (ImageView) findViewById(R.id.imageView1);
        Application.outputScreen.initScreen(this);

        if(calibrate) {
            Login login = (Login) Application.os.getApp("Login");
            login.calibrate(this);
            return;
        }

        // load share mode
        ((Share) Application.os.getApp("Share")).setProjectionActivity(this);
        Application.handler.post(new Runnable() {
            @Override
            public void run() {
                share = (Share) Application.os.startApp("Share");
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        if(!calibrate) {
            share.disconnectFromUser();
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( (keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME) ) {
            new AlertDialog.Builder(this).setMessage("Arreter le partage ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            share.disconnectFromUser();
                            finish();
                        }
                    }).setNegativeButton("Non", null).show();
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }


    public ImageView getImageView() {
        return imageView;
    }
    public void userDisconnected(String distantUID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deconnexion");
        builder.setMessage(distantUID + " s'est deconnecte");
        builder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
