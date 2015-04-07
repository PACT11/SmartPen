package Morpion;

import android.graphics.Bitmap;
import android.graphics.Color ;

/**
 * Created by Nicolas on 17/03/2015.
 */

public class Grille {
    public static int epaisseurTrait = 1 ;

    public Grille(Bitmap bitmap, int i, int j, int length) {
        // On trace les 4 traits verticaux
        for (int k = i ; k < i+length ; k++) {
            for (int e = 0 ; e < epaisseurTrait ; e++) {
                noir(bitmap,k,j+e) ;
                noir(bitmap,k,j+e+length/3) ;
                noir(bitmap,k,j+e+2*length/3) ;
                noir(bitmap,k,j+e+length) ;
            }
        }
        // On trace les 4 traits horizontaux
        for (int l = j ; l < j+length ; l++) {
            for (int e = 0 ; e < epaisseurTrait ; e++) {
                noir(bitmap,i+e,l) ;
                noir(bitmap,i+e+length/3,l) ;
                noir(bitmap,i+e+2*length/3,l) ;
                noir(bitmap,i+e+length,l) ;
            }
        }
    }
    public void noir(Bitmap bitmap, int i, int j) {
        bitmap.setPixel(i,j,Color.BLACK) ;
    }
}

