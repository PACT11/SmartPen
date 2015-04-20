package apps;

import android.graphics.*;
import view.OutputScreen;

/**
 * Created by Chab on 20/04/15.
 */

public class ApprentissageEcriture extends Share {

    private boolean fini;
    private boolean demarre;
    private int decalageY;
    private int height ;
    private int width ;

    public Typeface policeApprentissageEcriture;
    public Typeface policeIndications;
    public Typeface policeApprentissageEcritureDifficile;
    public Typeface policeApprentissageEcritureSansLigne;

    private Bitmap imageCaractere;
    private Bitmap lastImage;
    private boolean special;

    public Bitmap ajoutScore(Bitmap feuillePaysage, double score){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Paint paint = new Paint(); paint.setColor(Color.BLUE);
        Canvas canvas = new Canvas(feuille);
        paint.setTextSize(23);

        String textScore = "Entraîne-toi en choisissant une difficulté";
        canvas.drawText(textScore, 20 , height/6 , paint);

        return OutputScreen.rotateBitmap(feuille,90);
    }

    public Bitmap dessinerCaractere(Bitmap feuillePaysage, Bitmap imageCaractereSimple){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Canvas canvas = new Canvas(feuille);
        canvas.drawBitmap(imageCaractereSimple,0,decalageY,null);

        return OutputScreen.rotateBitmap(feuille,90);
    }



    public Bitmap generateImageCaractere(int widthImageCaractere, int heightImageCaractere) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String pass = "";
        int i =(int)Math.floor(Math.random()*chars.length());
        for(int x=0;x<7;x++)   {
            pass += chars.charAt(i) +"  ";
        }
        pass += pass.toUpperCase();
        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        paint.setTextSize(22);

        if (special){
            int j = (int)Math.floor(Math.random()*3);
            if (j==0) paint.setTypeface(policeApprentissageEcritureDifficile);
            if (j==1) paint.setTypeface(policeApprentissageEcritureSansLigne);
            if (j==2) {
                String fullChars=chars+chars.toUpperCase();
                pass = "";
                for (int k=0;k<14;k++){
                    i =(int)Math.floor(Math.random()*fullChars.length());
                    pass += fullChars.charAt(i) + " ";
                }
            }
        } else {
            paint.setTypeface(policeApprentissageEcriture);
        }

        Bitmap bmp = Bitmap.createBitmap(widthImageCaractere, heightImageCaractere, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paintMenuBackground = new Paint();
        paintMenuBackground.setColor(Color.WHITE);
        Rect background = new Rect(0,0,widthImageCaractere,heightImageCaractere);
        canvas.drawRect(background, paintMenuBackground);
        canvas.drawText(pass,20,45,paint);
        return bmp;
    }

    @Override
    protected void onLaunch() {
        Pactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                policeApprentissageEcriture = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/Cursifl.TTF");
                policeIndications = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/Indications.ttf");
                policeApprentissageEcritureDifficile = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/ecolier dur.ttf");
                policeApprentissageEcritureSansLigne = Typeface.createFromAsset(Pactivity.getAssets(),"fonts/Cursif.TTF");

            }
        });

        demarre=  false;
        fini = false;
        decalageY=150;
        width = outputScreen.getImage().getHeight();
        height = outputScreen.getImage().getWidth();

        configureRemoteListeners(server);
        menu.addItem("Ecriture finie");
        menu.addItem("Facile");
        menu.addItem("Plus dur");
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
        if ( menu.equals("Ecriture finie")) {
            if (demarre) {
                fini = true;
                demarre = false;
                special = false ;
                outputScreen.fitAndDisplay(ajoutScore(lastImage,0));
            }
        }
        if (menu.equals("Facile")){
            if (fini) {
                special = false;
                decalageY+=60;
                imageCaractere = generateImageCaractere(width,100);
                outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
                demarre = true;
                fini = false;
            }
        }
        if (menu.equals("Plus dur")){
            if (fini) {
                decalageY+=60;
                special=true;
                imageCaractere = generateImageCaractere(width,100);
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
            imageCaractere = generateImageCaractere(width,100);
            demarre=true;
            outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
        } else if (fini) {
            outputScreen.fitAndDisplay(ajoutScore(lastImage,0));
        } else if (demarre) {
            outputScreen.fitAndDisplay(dessinerCaractere(lastImage, imageCaractere));
        }
    }
}
