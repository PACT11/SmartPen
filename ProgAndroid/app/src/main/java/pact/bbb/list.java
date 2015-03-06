package pact.bbb;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class list extends ListActivity {


    private String[] mStrings = {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mStrings=null; // Todo : Arnaud ;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);

        setListAdapter(adapter);
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connexion");

        builder.setMessage("Se connecter avec : " + mStrings[(int) id] +" ?");
        builder.setNegativeButton("Non", null);
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent project = new Intent(list.this, projection.class);
                startActivity(project);
            }
        });

        builder.show(); }
}
