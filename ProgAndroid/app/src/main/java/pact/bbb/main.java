package pact.bbb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class main extends Activity {

    private Button mPasserelle = null;
    private MainProject mProject =null;
    private EditText mailView;
    private EditText pwdView;
    private String mail;
    private String pwd;

    private boolean isRegistered;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProject = new MainProject();
        mProject.start();

        mPasserelle = (Button) findViewById(R.id.connection);
        mailView = (EditText) findViewById(R.id.editText);
        pwdView = (EditText) findViewById(R.id.editText2);
        mail= mailView.getText().toString();
        pwd= pwdView.getText().toString();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur de Connexion");
        builder.setMessage("Erreur d'identifiant ou inscrivez-vous");

        mPasserelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRegistered = mProject.checkUser(mail,pwd);
                if(isRegistered){
                    Intent secondeActivite = new Intent(main.this, list.class);
                    startActivity(secondeActivite);
                }
                else {
                    builder.show();
                }

            }
        });
    }
}