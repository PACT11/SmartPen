package Morpion;

import android.graphics.Bitmap ;
import 	java.lang.Boolean ;
import android.graphics.Color ;

/**
 * Created by Nicolas on 17/03/2015.
 */
public class PositionnerGrille {
    public static int[] Position = new int[2] ;

    public static void TrouverPlaceLibre(Bitmap bitmap) {
        int[] T = new int[2] ;
        int length = bitmap.getWidth() ;
        boolean b = new Boolean(false) ;
        int i = 0 ;
        int j = 0 ;
        while (j != 5) {
            while (b != true) {
                b = AbsenceEcriture(bitmap,(length/4)+j*(5*length/21),(length/42)+i*(5*length/21)) ;
                i++ ;
                if (i == 4) {
                    j++;
                    i = 0 ;
                }
            }
        }
        if (b == false) j = 5 ;
        Position[0] = i ;
        Position[1] = j ;
    }

    static int seuilEcriture = 2 ;

    public static boolean AbsenceEcriture(Bitmap bitmap, int i, int j) {
        double compteur = Histogramme(bitmap, i, j) ;
        boolean b = new Boolean(false) ;
        if (compteur < seuilEcriture) b = true ;
        return b ;
    }

    public static double Histogramme(Bitmap bitmap, int i, int j) {
        Bitmap bitmapBinarise = Binariser(bitmap) ;
        double sum = 0 ;
        for (int k = i ; k < i+4*bitmap.getWidth()/21 ; k++) {
            for (int l = j ; l < j+4*bitmap.getWidth()/21 ; l++) {
                int color = bitmapBinarise.getPixel(l,k) ;
                sum = sum + 765 - Color.red(color) - Color.green(color) - Color.blue(color) ;
            }
        }
        sum = (double)sum / (3*16*bitmap.getWidth()^2/21^2) ;
        return sum ;
    }

    static int seuilBinarisation = 125 ;

    public static Bitmap Binariser(Bitmap bitmap) {

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

                int luminance = (int) (0.21 * red + 0.71 * green + 0.07 * blue) ;
                int colorTransformed = 0 ;

                if (luminance <= seuilBinarisation) colorTransformed = Color.BLACK ;
                else colorTransformed = Color.WHITE ;

                btm.setPixel(i, j, colorTransformed) ;
            }
        }

        return btm ;
    }
}

/* Il y a 2 utilisateurs A et B. A envoie une requête à B pour jouer au morpion.
   B l'accepte. Quand B a retiré sa main, on prend une photo de sa feuille.
   Celle-ci est recadrée (droite). Sur la feuille recadrée on applique la recherche d'espace libre.
   On trouve un espace libre et on le garde en mémoire. On peut alors tracer la grille sur la feuille.
   Puis on recale la feuille sur la feuille de l'utilisateur A. Sa feuille est prise en photo.
   On met une grille à l'endroit stocké et on peut commencer à échanger.
 */