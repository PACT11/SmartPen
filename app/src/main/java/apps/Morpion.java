package apps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import netzwerk.Connector;
import pact.smartpen.projection;
import view.CloudServices;
import view.OutputScreen;

/**
 * Created by arnaud on 07/03/15.
 */
public class Morpion extends Application {
    private Connector server;
    private CloudServices cloud;
    private projection activity;
    private int decalageX  ;
    private int decalageY ;
    private int yCoinGauche;
    private int xCoinGauche;
    private int tailleMorpion;

    public Bitmap ajoutGrille(Bitmap feuillePaysage) {
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        int width =  feuille.getWidth();
        int height =  feuille.getHeight();
        tailleMorpion = width*4/21 ;


        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        Canvas canvas = new Canvas(feuille);

            xCoinGauche = width /2 + decalageX;
            yCoinGauche = height/2 + decalageY ;

        for (int k=0 ; k<4 ; k++){
            canvas.drawLine(xCoinGauche + k * tailleMorpion/3 ,yCoinGauche,xCoinGauche+ k * 4 * tailleMorpion/3 ,yCoinGauche + tailleMorpion, paint);
            canvas.drawLine(xCoinGauche ,yCoinGauche + k * tailleMorpion/3,xCoinGauche + tailleMorpion, yCoinGauche+ k * 4 * tailleMorpion/3 , paint );
        }
        return OutputScreen.rotateBitmap(feuille,90);
    }

    @Override
    protected void onLaunch() {
        server = Login.getServer();
        cloud = new CloudServices(server);
        menu.addItem("Nouvelle Partie");
        menu.addItem("Quitter");
        decalageX=200;
        decalageY=200;
        if(server.getUID().equals("pact"))
            inputScreen.restart(activity);
    }
    protected void onNewImage(Bitmap image) {
        cloud.straigthenAndSend(image,1200,800);
    }

    protected void onCommandReceived(String command){

    }

    @Override
    protected void onMenuClick(String menu) {
        if ( menu.equals("Nouvelle Partie")) {
            decalageX = (decalageX + tailleMorpion)%(tailleMorpion*5) ;
            decalageY = (decalageY + tailleMorpion)%(tailleMorpion*5) ;
        }
        else if (menu.equals("Quitter")){

        }
    }

    @Override
    protected void onImageReceived(final Bitmap image){
            outputScreen.fitAndDisplay(ajoutGrille(image));
    }

    @Override
    protected void onClose() {
    }
}

