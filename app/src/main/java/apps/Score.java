package apps;

/**
 * Created by hu on 16/04/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;


public class Score {

    public static double donnerScore(Bitmap imageDepart, Bitmap imageArrivee) {

        Bitmap imageDeReference = Bitmap.createBitmap(imageDepart,100,200,200,120);
        Bitmap imageAvecEcriture = Bitmap.createBitmap(imageArrivee,100,200,200,120);

        int[] histogramme1R = histogrammeRouge(imageDeReference);
        int [] histogramme2R = histogrammeRouge(imageAvecEcriture);
        double differenceR = 0;

        for (int i=0; i<256;i++)
            differenceR += Math.abs(histogramme1R[i]-histogramme2R[i]);

        int[] histogramme1V = histogrammeVert(imageDeReference);
        int [] histogramme2V = histogrammeVert(imageAvecEcriture);
        double differenceV = 0;

        for (int i=0; i<256;i++)
            differenceV += Math.abs(histogramme1V[i]-histogramme2V[i]);

        int[] histogramme1B = histogrammeBleu(imageDeReference);
        int [] histogramme2B = histogrammeBleu(imageAvecEcriture);
        double differenceB = 0;

        for (int i=0; i<256;i++)
            differenceB += Math.abs(histogramme1B[i]-histogramme2B[i]);

        double differenceTotale = (differenceR + differenceV + differenceB)/(3*256);

        return 100.0-differenceTotale;

    }



    public static int[] histogrammeRouge(Bitmap image) {

        int[] histogrammeRouge = new int[256];

        for(int i=0; i<histogrammeRouge.length; i++) histogrammeRouge[i] = 0;

        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int colorOriginal = image.getPixel(i,j) ;
                int red = Color.red(colorOriginal) ;
                histogrammeRouge[red]++;
            }
        }

        return histogrammeRouge;

    }

    public static int[] histogrammeVert(Bitmap image) {

        int[] histogrammeVert = new int[256];

        for(int i=0; i<histogrammeVert.length; i++) histogrammeVert[i] = 0;

        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int colorOriginal = image.getPixel(i,j) ;
                int green = Color.green(colorOriginal) ;
                histogrammeVert[green]++;
            }
        }

        return histogrammeVert;

    }

    public static int[] histogrammeBleu(Bitmap image) {

        int[] histogrammeBleu = new int[256];

        for(int i=0; i<histogrammeBleu.length; i++) histogrammeBleu[i] = 0;

        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int colorOriginal = image.getPixel(i,j) ;
                int blue = Color.blue(colorOriginal) ;
                histogrammeBleu[blue]++;
            }
        }

        return histogrammeBleu;

    }

}