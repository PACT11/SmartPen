package apps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import view.OutputScreen;

/*
 */
public class Morpion extends Share {

    private int decalageX  ;
    private int decalageY ;
    private int tailleMorpion;
    private int margin;
    private int height;
    private int width;
    private int xCoinGauche;
    private int yCoinGauche;
    private Paint paint = new Paint();

    public Bitmap ajoutGrille(Bitmap feuillePaysage) {
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        width =  feuille.getWidth();
        height =  feuille.getHeight();
        tailleMorpion = width*4/21 ;
        margin = width*1/21 ;

        paint.setColor(Color.BLACK);
        Canvas canvas = new Canvas(feuille);

        xCoinGauche =  decalageX;
        yCoinGauche =  decalageY ;

        for (int k=0 ; k<4 ; k++){
            canvas.drawLine(xCoinGauche + k*tailleMorpion/3 ,yCoinGauche,xCoinGauche+ k*tailleMorpion/3 ,yCoinGauche + tailleMorpion, paint);
            canvas.drawLine(xCoinGauche ,yCoinGauche + k * tailleMorpion/3,xCoinGauche + tailleMorpion, yCoinGauche+ k*tailleMorpion/3 , paint );
        }

        return OutputScreen.rotateBitmap(feuille,90);
    }

    @Override
    protected void onLaunch() {
        configureRemoteListeners(server);

        menu.addItem("Nouvelle Partie");
        menu.addItem("Quitter");

        decalageX=50;
        decalageY=200;

        outputScreen.fitAndDisplay(ajoutGrille(outputScreen.getImage()));
    }

    @Override
    protected void onMenuClick(String menu) {

        if ( menu.equals("Nouvelle Partie")) {
            decalageX = decalageX + tailleMorpion+margin;
            if (decalageX > (width-tailleMorpion)) {
                decalageY = (decalageY + tailleMorpion);
                decalageX = 50 ;
            }
            outputScreen.fitAndDisplay(ajoutGrille(outputScreen.getImage()));
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

