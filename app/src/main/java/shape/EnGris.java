package shape;

/**
 * Created by hu on 06/03/15.
 */
import android.graphics.Bitmap;
import android.graphics.Color;

public class EnGris {


    // methode pour mettre l'image originale en niveau de gris avec la luminance
    public Bitmap mettreEnNiveauDeGris(Bitmap imageOriginale) {


        int width = imageOriginale.getWidth();
        int height = imageOriginale.getHeight();
        Bitmap.Config config = imageOriginale.getConfig();

        Bitmap imageGrise = Bitmap.createBitmap(width, height, config);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int colorOriginal = imageOriginale.getPixel(i, j);
                int red = Color.red(colorOriginal);
                int blue = Color.blue(colorOriginal);
                int green = Color.green(colorOriginal);
                int alpha = Color.alpha(colorOriginal);

                int gris = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                int colorTransformed = Color.argb(alpha, gris, gris, gris);

                imageGrise.setPixel(i, j, colorTransformed);

            }
        }

        return imageGrise;

    }

}