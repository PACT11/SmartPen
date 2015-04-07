package pact.smartpen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import apps.Application;
import apps.Login;


public class SmartPen extends ActionBarActivity {
    Login login;
    // GUI
    private Button mPasserelle;
    private EditText mailView;
    private EditText pwdView;
    private CheckBox checkView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);

        // start SmartPen
        new MainProject().start();
        sleep();

        // #### DEBUG : if you want to bypass the login/connect activities, uncomment the following : ####
        //Intent secondeActivite = new Intent(SmartPen.this, projection.class);
        //startActivity(secondeActivite);
        // finish();

        // pass this for callbacks
        ((Login)Application.os.getApp("Login")).setSmartPenActivity(this);
        // start login mode (connects to the server) has to be done in the mainProject Thread because of networking
        Application.handler.post(new Runnable() {
            @Override
            public void run() {
                login = (Login) Application.os.startApp("Login");
            }
        });

        //GUI
            String loginDefault = prefs.getString("mail", null);
            String pwdDefault = prefs.getString("pwd", null);

        mPasserelle = (Button) findViewById(R.id.connection);
        mailView = (EditText) findViewById(R.id.editText);
        pwdView = (EditText) findViewById(R.id.editText2);
        checkView = (CheckBox) findViewById(R.id.checkBox);

        if ( checkView.isChecked() ){
            mailView.setText(loginDefault);
            pwdView.setText(pwdDefault);
        }

          // when tap on login button
        mPasserelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mailView.getText().toString();
                String pwd = pwdView.getText().toString();
                if ( checkView.isChecked() ){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("mail",mail);
                    editor.putString("pwd",pwd);
                    editor.commit();
                }
                login.checkUser(mail,pwd);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart_pen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void checkUserCallback(boolean isRegistered) {
        if(isRegistered){
            Intent secondeActivite = new Intent(SmartPen.this, list.class);
            sleep();
            startActivity(secondeActivite);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Erreur de Connexion");
            builder.setMessage("Erreur d'identifiant ou inscrivez-vous");
            builder.show();
        }
    }
    // called when a connection cannot be established with the server
    public void serverUnreachable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Serveur inaccessible");
        builder.setMessage("Vérifiez votre connection à internet ou réessayez dans quelques instants");
        builder.setNeutralButton("Réessayer", new DialogInterface.OnClickListener() {
            // retry : re-launch the login mode
            public void onClick(DialogInterface dialog, int id) {
                Application.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        login = (Login) Application.os.startApp("Login");
                    }
                });
            }
        });
        builder.show();
    }
    private void sleep() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
