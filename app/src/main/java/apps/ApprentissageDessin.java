package apps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private int height ;
    private int width ;

    private Bitmap imageCaractere;
    private Bitmap lastImage;
    private boolean pokemon = true;
    private boolean animaux = false ;
    private  String[] pokemonListe = {
            "carapuce",
            "coloriage-pokemon-2", //pikachu
            "mew",
            "mewtwo",
            "salameche"
    };
    private  String[] animauxListe = {
            "ecureuil",
            "elephant"
    };

    public Bitmap ajoutScore(Bitmap feuillePaysage, double score){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Paint paint = new Paint(); paint.setColor(Color.BLUE);
        Canvas canvas = new Canvas(feuille);
        paint.setTextSize(19);

        String textScore = "Prends une nouvelle feuille et choisis une catégorie";
        canvas.drawText(textScore, 20 , height/6 , paint);

        return OutputScreen.rotateBitmap(feuille,90);
    }

    public Bitmap dessinerCaractere(Bitmap feuillePaysage, Bitmap imageDessinSimple){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Canvas canvas = new Canvas(feuille);
        canvas.drawBitmap(imageDessinSimple,
                new Rect(0,0,imageDessinSimple.getWidth()-1,imageDessinSimple.getHeight()-1),
                new Rect(0,height/12,width-1,height-1),null);

        return OutputScreen.rotateBitmap(feuille,90);
    }



    public Bitmap generateImageDessin() {
        if (pokemon){
            int i =(int)Math.floor(Math.random()*pokemonListe.length);
            return getBitmap("dessins/"+pokemonListe[i]+".jpg");
        }
        else if(animaux){
            int i =(int)Math.floor(Math.random()*animauxListe.length);
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
        demarre=  false;
        fini = false;
        width = outputScreen.getImage().getHeight();
        height = outputScreen.getImage().getWidth();

        configureRemoteListeners(server);
        menu.addItem("Dessin fini");
        menu.addItem("Pokemon");
        menu.addItem("Animaux");
        menu.addItem("Quitter");
        menu.setDistantUID(this.distantUID);

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
                pokemon = true;
                imageCaractere = generateImageDessin();
                outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
                demarre = true;
                fini = false;
            }
        }
        if (menu.equals("Animaux")){
            if (fini) {
                animaux = true;
                imageCaractere = generateImageDessin();
                outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
                demarre = true;
                fini = false;
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
