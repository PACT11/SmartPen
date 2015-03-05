package pact.smartpen;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import apps.Application;
import apps.Login;
import apps.OS;
import view.InputScreen;
import view.MyCamera;


public class SmartPen extends ActionBarActivity {
    Login login;

    private Button mPasserelle;
    private EditText mailView;
    private EditText pwdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_pen);
        // start SmartPen
        new MainProject().start();
        sleep();
        // start login mode (connects to the server)
        Application.os.startApp("Login");
        login = (Login) Application.os.getApp("Login");

        //GUI
        mPasserelle = (Button) findViewById(R.id.connection);
        mailView = (EditText) findViewById(R.id.editText);
        pwdView = (EditText) findViewById(R.id.editText2);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur de Connexion");
        builder.setMessage("Erreur d'identifiant ou inscrivez-vous");
        // when tap on login button
        mPasserelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.checkUser(mailView.getText().toString(),pwdView.getText().toString())){
                    Intent secondeActivite = new Intent(SmartPen.this, list.class);
                    sleep();
                    startActivity(secondeActivite);
                }
                else {
                    builder.show();
                }

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
    @Override
    protected void onPause() {
        super.onPause();
        //if(Application.inputScreen!=null)
            //Application.inputScreen.close();

    }
    protected void onResume() {
        super.onResume();
        //if(Application.inputScreen!=null)
            //Application.inputScreen.restart();
    }
    private void sleep() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
