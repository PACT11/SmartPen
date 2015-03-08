package shape;

/**
 * Created by hu on 06/03/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;

public class ApplicationHomographie{

    /*cree l'image à projeter à partir de la photo a projeter, et de l'image bleue qui contient les coins sur lesquels
    l'homographie va s'appuyer pour trouver la transformation*/

    public Bitmap appliquerTransformation(Bitmap imageDepart,Bitmap imageBleue) {
        MainRansac mainRansac = new MainRansac();
        MainRansacBleu mainRansacBleu = new MainRansacBleu();

        ArrayList<Point> coinsDepart = mainRansac.obtentionCoins(imageDepart);
        ArrayList<Point> coinsArrivee = mainRansacBleu.obtentionCoinsBleus(imageBleue);
        return partieEntiere(imageDepart,coinsDepart,coinsArrivee);
    }

    public static Bitmap partieEntiere(Bitmap imageDepart,ArrayList<Point> coinsDepart, ArrayList<Point> coinsArrivee){

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
       // int width = imageDepart.getWidth();
        //int height = imageDepart.getHeight();
        Bitmap.Config config = imageDepart.getConfig();

        Bitmap imageRecalee = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
        System.out.println(imageDepart.getWidth());
        System.out.println(imageDepart.getHeight());
        int u,v;

        for(int i=0; i<imageRecalee.getWidth(); i++) {
            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
                Point ij = new Point(i,j);
                double[] p = phi.phiInverse(ij, H);
                u = (int) p[0];
                v = (int) p[1];

                if ((u>0) && (u<imageDepart.getWidth()) && (v>0) && (v<imageDepart.getHeight())) {
                    imageRecalee.setPixel(i, j, imageDepart.getPixel(u,v)) ;
                } else {
                    imageRecalee.setPixel(i, j, Color.BLACK);
                }
            }
        }

        return imageRecalee;

    }


}