import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class DetectionMain {
	
	private static BufferedImage image1,image2;
	
	public static void main(String[] args) throws IOException{
		File fichier1 = new File(args[0]+".jpg");
		File fichier2 = new File(args[1]+".jpg");
        image1 = ImageIO.read(fichier1);
        image2 = ImageIO.read(fichier2);
        System.out.println(differenceHistogramme(image1,image2));    

	}
	
	/* vrai seuil feuille2 et feuille3 : 720 000 
	 * 
	 */
	
	public static int differenceHistogramme(BufferedImage image1, BufferedImage image2) {
		int seuilDifference = 850000;
		int[] histogramme1 = histogrammeRouge(image1);
		int [] histogramme2 = histogrammeRouge(image2);
		int difference = 0;
		
		for (int i=0; i<256;i++)
			difference += Math.abs(histogramme1[i]-histogramme2[i]);
		
		if (difference>seuilDifference)
			return 1;
		else 
			return 0;
	}
	
    public static int[] histogrammeRouge(BufferedImage imageOriginale) {
 
        int[] histogrammeRouge = new int[256];
 
        for(int i=0; i<histogrammeRouge.length; i++) histogrammeRouge[i] = 0;
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            for(int j=0; j<imageOriginale.getHeight(); j++) {
                int red = new Color(imageOriginale.getRGB (i, j)).getRed();
                histogrammeRouge[red]++;
            }
        }
 
        return histogrammeRouge;
 
    }
    
    public static int[] histogrammeVert(BufferedImage imageOriginale) {
    	 
        int[] histogrammeVert = new int[256];
 
        for(int i=0; i<histogrammeVert.length; i++) histogrammeVert[i] = 0;
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            for(int j=0; j<imageOriginale.getHeight(); j++) {
                int red = new Color(imageOriginale.getRGB (i, j)).getRed();
                histogrammeVert[red]++;
            }
        }
 
        return histogrammeVert;
 
    }
    
    public static int[] histogrammeBleu(BufferedImage imageOriginale) {
   	 
        int[] histogrammeBleu = new int[256];
 
        for(int i=0; i<histogrammeBleu.length; i++) histogrammeBleu[i] = 0;
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            for(int j=0; j<imageOriginale.getHeight(); j++) {
                int red = new Color(imageOriginale.getRGB (i, j)).getRed();
                histogrammeBleu[red]++;
            }
        }
 
        return histogrammeBleu;
 
    }

}
