package apps;

import android.graphics.*;
import view.OutputScreen;

/**
 * Created by Chab on 16/04/15.
 */

public class ApprentissageEcriture extends Share {

    private boolean fini;
    private boolean demarre;
    private int decalageY;
    private int height ;
    private int width ;
    public Typeface policeApprentissageEcriture;
    private Bitmap imageCaractere;
    private Bitmap lastImage;

    public Bitmap ajoutScore(Bitmap feuillePaysage, double score){
        Bitmap feuille = OutputScreen.rotateBitmap(feuillePaysage,-90);

        Paint paint = new Paint(); paint.setColor(Color.BLUE);
        Canvas canvas = new Canvas(feuille);
        paint.setTextSize(25);
        String textScore = "Entraîne-toi en cliquant sur Nouveau";
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
        int i =(int)Math.floor(Math.random() * chars.length() -1);
        for(int x=0;x<7;x++)   {
            pass += chars.charAt(i) +"  ";
        }
        pass += pass.toUpperCase();
        Paint paint = new Paint(); paint.setColor(Color.BLACK);
        paint.setTextSize(22);
        paint.setTypeface(policeApprentissageEcriture);

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
            }
        });

        demarre=  false;
        fini = false;
        decalageY=150;
        width = outputScreen.getImage().getHeight();
        height = outputScreen.getImage().getWidth();

        configureRemoteListeners(server);
        menu.addItem("Ecriture finie");
        menu.addItem("Nouveau");
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
        if ( menu.equals("Ecriture finie")) {
            if (demarre) {
                fini = true;
                demarre = false;
                outputScreen.fitAndDisplay(ajoutScore(lastImage,0));
            }
        }
        if (menu.equals("Nouveau")){
            if (fini) {
                decalageY+=80;
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
