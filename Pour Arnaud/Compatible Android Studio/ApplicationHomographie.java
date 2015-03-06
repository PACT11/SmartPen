package hu.recalage;

/**
 * Created by hu on 06/03/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;

public class ApplicationHomographie{

    private static Bitmap partieEntiere(Bitmap imageDepart,ArrayList<Point> coinsDepart, ArrayList<Point> coinsArrivee){

        EstimationHomographie phi = new EstimationHomographie();

        Point p1 = coinsDepart.get(0);
        Point p2 = coinsDepart.get(1);
        Point p3 = coinsDepart.get(2);
        Point p4 = coinsDepart.get(3);

        Point p5 = coinsArrivee.get(0);
        Point p6 = coinsArrivee.get(1);
        Point p7 = coinsArrivee.get(2);
        Point p8 = coinsArrivee.get(3);

        Matrix H = phi.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);

        //Creation de l'image resultat vierge
        int width = imageDepart.getWidth();
        int height = imageDepart.getHeight();
        Bitmap.Config config = imageDepart.getConfig();

        Bitmap imageRecalee = Bitmap.createBitmap(width, height, config);

        int u,v;

        for(int i=0; i<imageRecalee.getWidth(); i++) {
            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
                Point ij = new Point(i,j);
                Point p = phi.phiInverse(ij, H);
                u = (int) p.x;
                v = (int) p.x;

                if ((u>0) && (u<imageDepart.getWidth()) && (v>0) && (v<imageDepart.getHeight())) {

                    //Obtention des couleurs
                    int colorOriginal = imageDepart.getPixel(i,j) ;
                    int red = Color.red(colorOriginal) ;
                    int blue = Color.blue(colorOriginal) ;
                    int green = Color.green(colorOriginal) ;
                    int alpha = Color.alpha(colorOriginal) ;

                    int colorTransformed = Color.argb(alpha,red,green,blue) ;


                    imageRecalee.setPixel(i, j, colorTransformed) ;
                }
            }
        }

        return imageRecalee;

    }


}