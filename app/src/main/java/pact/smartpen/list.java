package pact.smartpen;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import apps.Application;
import apps.Login;


public class list extends ListActivity {


    private String[] users;
    private Login login;
    private String targetUser;
    private AlertDialog alert;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login = (Login) Application.os.getApp("Login");
        users = login.getUsers();
        login.setListActivity(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        setListAdapter(adapter);
        if(alert!=null)
            alert.cancel();
    }

    public void showConnectionRequestDialog(String UID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Requete de connexion");

        builder.setMessage("Accepter la connexion avec " + UID +" ?");
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                login.answerConnectionRequest(false);
            }
        });
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                login.answerConnectionRequest(true);
                Intent project = new Intent(list.this, projection.class);
                startActivity(project);
            }
        });
        builder.show();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connexion");

        targetUser = users[(int) id];
        builder.setMessage("Se connecter avec : " + targetUser +" ?");
        builder.setNegativeButton("Non", null);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                login.connectTo(targetUser);
                showWaitingForAnswerDialog();
            }
        });

        builder.show();
    }
    private void showWaitingForAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Requete envoyee");
        builder.setMessage("En attente d'une reponse de " + targetUser + " ...");
        alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }
    public void processConnectionAnswer(boolean answer) {
        alert.cancel();
        alert.dismiss();
        if(answer) {
            Intent project = new Intent(list.this, projection.class);
            startActivity(project);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Requete refusee");
            builder.setMessage(targetUser + " a refuse la connexion.");
            builder.show();
        }
    }

}
