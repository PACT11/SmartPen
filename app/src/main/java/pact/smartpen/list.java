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
    private ArrayAdapter<String> adapter;
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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        setListAdapter(adapter);

        if(alert!=null)
            alert.cancel();
    }


    public void showConnectionRequestDialog(String UID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Requête de connexion");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 23) {
            // Make sure the request was successful
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connexion");

        targetUser = users[(int) id];
        if (targetUser == "Mode Solitaire"){
            builder.setMessage("Démarrer le mode solitaire ?");
            builder.setNegativeButton("Non", null);
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    login.connectTo(targetUser);
                }
            });
        }
        else {

            builder.setMessage("Se connecter avec : " + targetUser + " ?");
            builder.setNegativeButton("Non", null);
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    login.connectTo(targetUser);
                    showWaitingForAnswerDialog();
                }
            });

            builder.show();
        }
    }


    private void showWaitingForAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Requête envoyée");
        builder.setMessage("En attente d'une réponse de " + targetUser + " ...");
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
            builder.setTitle("Requête refusée");
            builder.setMessage(targetUser + " a refusé la connexion.");
            builder.show();
        }
    }
    public void updateUsers(String[] users) {
        this.users = users;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        setListAdapter(adapter);
        ((ArrayAdapter<String>) getListAdapter()).notifyDataSetChanged();
    }
}
