package shape;
/**
 * Created by hu on 06/03/15.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class ManipulationImage {
    private Bitmap mergeBitmap(Bitmap bitmap1, Bitmap bitmap2){
        Bitmap mergedBitmap = null;

        int width;
        if(bitmap1.getWidth() >= bitmap2.getWidth()){
            width = bitmap1.getWidth();
        }else{
            width = bitmap2.getWidth();
        }

        int height;
        if(bitmap1.getHeight() >= bitmap2.getHeight()){
            height = bitmap1.getHeight();
        }else{
            height = bitmap2.getHeight();
        }

        Bitmap.Config conf = bitmap1.getConfig();
        if(conf== null){
            conf = Bitmap.Config.ARGB_8888;
        }

        mergedBitmap = Bitmap.createBitmap(width, height, conf);
        Canvas newCanvas = new Canvas(mergedBitmap);

        newCanvas.drawBitmap(bitmap1, 0, 0, null);
        Paint paint = new Paint();
        paint.setAlpha(128);

        newCanvas.drawBitmap(bitmap2, 0, 0, paint);
        return mergedBitmap;
    }


    public Bitmap changedPixel(Bitmap bitmap){

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width = " + width);
        System.out.println("height = "+ height);
        Bitmap.Config config = bitmap.getConfig();
        boolean isMutable = true;
        Bitmap btm = bitmap.copy(config, isMutable);
        Color color = new Color();
        int colour;
        colour= color.GREEN;
        for( int i=150; i<987 ;i++){
            for (int j=50; j<753; j++){
                btm.setPixel( i, j, colour);
            }
        }

        return btm;
    }

    public static Bitmap mettreEnNiveauDeGris(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap.Config config = bitmap.getConfig();
        Bitmap btm = Bitmap.createBitmap(width,height,config);

        for (int i = 0 ; i<width ; i++) {
            for (int j = 0 ; j < height ; j++) {
                int colorOriginal = bitmap.getPixel(i,j) ;
                int red = Color.red(colorOriginal) ;
                int blue = Color.blue(colorOriginal) ;
                int green = Color.green(colorOriginal) ;
                int alpha = Color.alpha(colorOriginal) ;

                int gris = (int) (0.21 * red + 0.71 * green + 0.07 * blue) ;
                int colorTransformed = Color.argb(alpha,gris,gris,gris) ;

                btm.setPixel(i, j, colorTransformed) ;
            }
        }

        return btm ;
    }

    // Renvoie l'histogramme de l'image en niveau de gris
    public static int[] imageHistogram(Bitmap imageGrise) {

        int[] histogram = new int[256];

        for(int i=0; i<histogram.length; i++) histogram[i] = 0;

        for(int i=0; i<imageGrise.getWidth(); i++) {
            for(int j=0; j<imageGrise.getHeight(); j++) {
                int colorOriginal = imageGrise.getPixel(i,j) ;
                int red = Color.red(colorOriginal) ;
                histogram[red]++;
            }
        }

        return histogram;

    }

    // retourne le seuil pour binariser l'image
    private static int determinerSeuil(Bitmap imageOriginale) {

        int[] histogram = imageHistogram(imageOriginale);
        int total = imageOriginale.getHeight() * imageOriginale.getWidth();

        float somme = 0;
        for(int i=0; i<256; i++) somme += i * histogram[i];

        float somme2 = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int seuil= 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            somme2 += (float) (i * histogram[i]);
            float mB = somme2/ wB;
            float mF = (somme - somme2) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                seuil = i;
            }
        }

        return seuil;

    }

    public static Bitmap Binariser(Bitmap bitmap) {
        int seuil = determinerSeuil(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap.Config config = bitmap.getConfig();
        Bitmap btm = Bitmap.createBitmap(width,height,config);

        for (int i = 0 ; i<width ; i++) {
            for (int j = 0 ; j<height ; j++) {
                int colorOriginal = bitmap.getPixel(i,j);
                int red = Color.red(colorOriginal) ;
                int blue = Color.blue(colorOriginal) ;
                int green = Color.green(colorOriginal) ;

                int luminance = (int) (0.21 * red + 0.71 * green + 0.07 * blue) ;
                int colorTransformed = 0 ;

                if (luminance <= seuil) colorTransformed = Color.argb(255,5,100,117) ;
                else colorTransformed = Color.WHITE ;

                btm.setPixel(i, j, colorTransformed) ;
            }
        }

        return btm ;
    }
}
