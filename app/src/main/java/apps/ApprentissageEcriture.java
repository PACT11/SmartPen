package apps;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.InputStream;

import view.OutputScreen;

/**
 * Created by Chab on 16/04/15.
 */

public class ApprentissageEcriture extends Share {

    public ApprentissageEcriture(Context myContext) {
        AssetManager mngr = myContext.getAssets();
        try {
            InputStream is = mngr.open("fonts/textdb.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean fini = true ;
    private boolean demarre = false;
    private int decalageY;
    private int height ;
    private int width ;
    public Typeface policeApprentissageEcriture;
    private Bitmap feuilleAvecCaractere;

    public Bitmap ajoutScore(Bitmap feuillePaysage, double score){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);
        width =  feuille.getWidth();
        height =  feuille.getHeight();

        Paint paint = new Paint(); paint.setColor(Color.GREEN);
        Canvas canvas = new Canvas(feuille);
        paint.setTextSize(40);
        String textScore = "Entraîne-toi en cliquant sur Nouvel Entrainement";
        canvas.drawText(textScore, width/2 , height/4 , paint);

        return OutputScreen.rotateBitmap(feuille,90);
    }

    public Bitmap dessinerCaractère(Bitmap feuillePaysage, Bitmap imageCaractereSimple){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Canvas canvas = new Canvas(feuille);
        canvas.drawBitmap(imageCaractereSimple,100,decalageY,null);

        return OutputScreen.rotateBitmap(feuille,-90);
    }



    public Bitmap generateImageCaractère(int widthImageCaractere, int heightImageCaractere) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String pass = "";
        int i =(int)Math.floor(Math.random() * chars.length() -1);
        for(int x=0;x<7;x++)   {
            pass += chars.charAt(i) +"  ";
        }
        pass += pass.toUpperCase();
        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        paint.setTypeface(policeApprentissageEcriture);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(widthImageCaractere, heightImageCaractere, conf);
        Canvas canvas = new Canvas(bmp);
        Paint paintMenuBackground = new Paint();
        paintMenuBackground.setColor(Color.WHITE);
        Rect background = new Rect(0,0,widthImageCaractere,heightImageCaractere);
        canvas.drawRect(background, paintMenuBackground);
        canvas.drawText(pass,0,0,paint);
        return bmp;
    }

    @Override
    protected void onLaunch() {
        Pactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                policeApprentissageEcriture = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/Cursifl.ttf");
            }
        });

        demarre=  false;
        fini = false;
        configureRemoteListeners(server);
        menu.addItem("Ecriture completee");
        menu.addItem("Nouvel Entrainement");
        menu.addItem("Quitter");
    }


    protected void onNewImage(Bitmap image) {
        cloud.straigthenAndSend(image,1200,800);
    }

    @Override
    protected void onMenuClick(String menu) {
        if ( menu.equals("Ecriture completee")) {
            if (demarre) {
                fini = true;
                demarre = false;
            }
        }
        if (menu.equals("Nouvel Entrainement")){
            if (fini) {
                decalageY= (decalageY + 100)%height;
                demarre = false;
                fini = false;
            }
        }
        if (menu.equals("Quitter")){
            os.getApp("Share").run();
        }
    }

    @Override
    protected void onImageReceived(final Bitmap image){
        if (!fini && !demarre) {
            Bitmap imageCaractere = generateImageCaractère(600,100);
            feuilleAvecCaractere = dessinerCaractère(image, imageCaractere);
            outputScreen.fitAndDisplay(dessinerCaractère(image, imageCaractere));
            demarre=true;
        }

        if (fini) {
            outputScreen.fitAndDisplay(ajoutScore(image,0));
        }

        else if (demarre) {
            outputScreen.fitAndDisplay(feuilleAvecCaractere);
        }
    }

    @Override
    protected void onClose() {
    }



}
