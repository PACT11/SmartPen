package apps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.InputStream;

import view.OutputScreen;

/**
 * Created by Chab on 20/04/15.
 */

public class ApprentissageDessin extends Share {

    private boolean fini;
    private boolean demarre;
    private int decalageY= 150 ;
    private int height ;
    private int width ;
    public Typeface policeIndications;
    private Bitmap imageCaractere;
    private Bitmap lastImage;
    private boolean pokemon = false;
    private boolean animaux = false ;
    private  String[] pokemonListe = {
            "carapuce",
            "coloriage-pokemon-2", //pikachu
            "mew",
            "mewto",
            "salameche"
    };
    private  String[] animauxListe = {
            "bambi",
            "biche",
            "ecureuil",
            "elephant",
            "sanglier"
    };

    public Bitmap ajoutScore(Bitmap feuillePaysage, double score){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Paint paint = new Paint(); paint.setColor(Color.BLUE);
        Canvas canvas = new Canvas(feuille);
        paint.setTextSize(25);
        paint.setTypeface(policeIndications);
        String textScore = "Prends une nouvelle feuille et clique sur la catégorie souhaitée";
        canvas.drawText(textScore, 20 , height/6 , paint);

        return OutputScreen.rotateBitmap(feuille,90);
    }

    public Bitmap dessinerCaractere(Bitmap feuillePaysage, Bitmap imageDessinSimple){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Canvas canvas = new Canvas(feuille);
        canvas.drawBitmap(imageDessinSimple,0,decalageY,null);

        return OutputScreen.rotateBitmap(feuille,90);
    }



    public Bitmap generateImageDessin() {
        if (pokemon){
            int i =(int)Math.floor(Math.random() * pokemonListe.length -1);
            return getBitmap("dessins/"+pokemonListe[i]+".jpg");
        }
        else if(animaux){
            int i =(int)Math.floor(Math.random() * animauxListe.length -1);
            return getBitmap("dessins/"+animauxListe[i]+".jpg");
        }
        else return null;
    }

    public Bitmap getBitmap(String pathNameRelativeToAssetsFolder) {
        InputStream bitmapIs = null;
        Bitmap bmp = null;
        try {
            bitmapIs = Pactivity.getAssets().open(pathNameRelativeToAssetsFolder);
            bmp = BitmapFactory.decodeStream(bitmapIs);
        } catch (IOException e) {
            // Error reading the file
            e.printStackTrace();

            if(bmp != null) {
                bmp.recycle();
                bmp = null;
            }
        } finally {
            if(bitmapIs != null) {
                try {
                    bitmapIs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bmp;
    }

    @Override
    protected void onLaunch() {
        Pactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                policeIndications = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/Indications.TTF");
            }
        });

        demarre=  false;
        fini = false;
        width = outputScreen.getImage().getHeight();
        height = outputScreen.getImage().getWidth();

        configureRemoteListeners(server);
        menu.addItem("Dessin fini");
        menu.addItem("Pokemon");
        menu.addItem("Animaux");
        menu.addItem("Quitter");

        if(distantUID.equals("mode solitaire")) {
            lastImage = Bitmap.createBitmap(height,width, Bitmap.Config.ARGB_8888);
            for(int i=0;i<height; i++) {
                for(int j=0;j<width;j++)
                    lastImage.setPixel(i,j, Color.WHITE);
            }
        }

    }

    @Override
    protected void onMenuClick(String menu) {
        if ( menu.equals("Dessin fini")) {
            if (demarre) {
                fini = true;
                demarre = false;
                outputScreen.fitAndDisplay(ajoutScore(lastImage,0));
                pokemon = false ;
                animaux = false ;
            }
        }
        if (menu.equals("Pokemon")){
            if (fini) {
                imageCaractere = generateImageDessin();
                outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
                demarre = true;
                fini = false;
                pokemon = true ;
            }
        }
        if (menu.equals("Animaux")){
            if (fini) {
                imageCaractere = generateImageDessin();
                outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
                demarre = true;
                fini = false;
                animaux = true;
            }
        }
        if (menu.equals("Quitter")){
            os.getApp("Share").run();
        }
    }

    @Override
    protected void onImageReceived(Bitmap image){
        if(!this.distantUID.equals("mode solitaire"))
            lastImage = image;

        if(!demarre && !fini) {
            imageCaractere = generateImageDessin();
            demarre=true;
            outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
        } else if (fini) {
            outputScreen.fitAndDisplay(ajoutScore(lastImage,0));
        } else if (demarre) {
            outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
        }
    }
}
