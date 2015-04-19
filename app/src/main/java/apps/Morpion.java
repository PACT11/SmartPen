package apps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import netzwerk.Connector;
import pact.smartpen.projection;
import view.CloudServices;
import view.OutputScreen;

/*
 */
public class Morpion extends Share {

    private int decalageX  ;
    private int decalageY ;
    private int tailleMorpion;

    public Bitmap ajoutGrille(Bitmap feuillePaysage) {
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        int width =  feuille.getWidth();
        int height =  feuille.getHeight();
        tailleMorpion = width*4/21 ;


        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        Canvas canvas = new Canvas(feuille);

        int xCoinGauche = width /2 + decalageX;
        int yCoinGauche = height/2 + decalageY ;

        for (int k=0 ; k<4 ; k++){
            canvas.drawLine(xCoinGauche + k * tailleMorpion/3 ,yCoinGauche,xCoinGauche+ k * 4 * tailleMorpion/3 ,yCoinGauche + tailleMorpion, paint);
            canvas.drawLine(xCoinGauche ,yCoinGauche + k * tailleMorpion/3,xCoinGauche + tailleMorpion, yCoinGauche+ k * 4 * tailleMorpion/3 , paint );
        }
        return OutputScreen.rotateBitmap(feuille,90);
    }

    @Override
    protected void onLaunch() {
        configureRemoteListeners(server);

        menu.addItem("Nouvelle Partie");
        menu.addItem("Quitter");

        decalageX=200;
        decalageY=200;
    }

    @Override
    protected void onMenuClick(String menu) {
        if ( menu.equals("Nouvelle Partie") && tailleMorpion !=0) {
            decalageX = (decalageX + tailleMorpion)%(tailleMorpion*5);
            decalageY = (decalageY + tailleMorpion)%(tailleMorpion*5);
        }
        else if (menu.equals("Quitter")){
            os.startApp("Share");
        }
    }

    @Override
    protected void onImageReceived(final Bitmap image){
            outputScreen.fitAndDisplay(ajoutGrille(image));
    }
}

