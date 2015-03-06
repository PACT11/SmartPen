package hu.recalage;

/**
 * Created by hu on 06/03/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

public class DetectionMain {

    public static boolean differenceHistogramme(Bitmap image1, Bitmap image2) {
        int seuilDifference = 4600000;

        int[] histogramme1R = histogrammeRouge(image1);
        int [] histogramme2R = histogrammeRouge(image2);
        int differenceR = 0;

        for (int i=0; i<256;i++)
            differenceR += Math.abs(histogramme1R[i]-histogramme2R[i]);

        int[] histogramme1V = histogrammeVert(image1);
        int [] histogramme2V = histogrammeVert(image2);
        int differenceV = 0;

        for (int i=0; i<256;i++)
            differenceV += Math.abs(histogramme1V[i]-histogramme2V[i]);

        int[] histogramme1B = histogrammeBleu(image1);
        int [] histogramme2B = histogrammeBleu(image2);
        int differenceB = 0;

        for (int i=0; i<256;i++)
            differenceB += Math.abs(histogramme1B[i]-histogramme2B[i]);

        int differenceTotale = (differenceR + differenceV + differenceB)/3;

        if (differenceTotale>seuilDifference)
            return true; //la main n'est plus là
        else
            return false; //la main est toujours là
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