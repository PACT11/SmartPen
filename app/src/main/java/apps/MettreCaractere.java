package apps;

/**
 * Created by hu on 16/04/15.
 */
import android.graphics.Bitmap;
import android.graphics.Color;

public class MettreCaractere {



    //methode pour mettre le caractere imageCaractere sur une feuille prealablement redressee
    private static Bitmap mettreMenu(Bitmap feuille, Bitmap imageCaractere) {
        int width = feuille.getWidth();
        int height = feuille.getHeight();

        //cree une copie du Bitmap feuille dans imageAvecCaractere
        Bitmap imageAvecCaractere = Bitmap.createBitmap(feuille,0,0,width, height);

        //met le caractere dans imageAvecCaractere
        for(int i=0; i<imageCaractere.getWidth(); i++) {
            for(int j=0; j<imageCaractere.getHeight(); j++) {
                int colorOriginal = imageCaractere.getPixel(i, j);
                int red = Color.red(colorOriginal);
                int blue = Color.blue(colorOriginal);
                int green = Color.green(colorOriginal);
                int alpha = Color.alpha(colorOriginal);
                if (i<imageAvecCaractere.getWidth() && j<imageAvecCaractere.getHeight()) {
                    int nouveauPixel = Color.argb(alpha,red,green,blue);
                    // les +100 et +200 servent à placer le caractere ou on veut sur la feuille
                    imageAvecCaractere.setPixel(i+100, j+200, nouveauPixel);

                }


            }
        }

        return imageAvecCaractere;

    }


}
