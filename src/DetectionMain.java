import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class DetectionMain {
	
	private static BufferedImage image1,image2;
	
	public static void main(String[] args) throws IOException{
		File fichier1 = new File("main4.jpg");
		File fichier2 = new File("main5.jpg");
        image1 = ImageIO.read(fichier1);
        image2 = ImageIO.read(fichier2);
        System.out.println(differenceHistogramme(image1,image2));    

	}
	
	
	public static String differenceHistogramme(BufferedImage image1, BufferedImage image2) {
		double seuilDifference = 0.35;
		
		int[] histogramme1R = histogrammeRouge(image1);
		int [] histogramme2R = histogrammeRouge(image2);
		double differenceR = 0;
		
		for (int i=0; i<256;i++)
			differenceR += Math.abs(histogramme1R[i]-histogramme2R[i]);
		
		int[] histogramme1V = histogrammeVert(image1);
		int [] histogramme2V = histogrammeVert(image2);
		double differenceV = 0;
		
		for (int i=0; i<256;i++)
			differenceV += Math.abs(histogramme1V[i]-histogramme2V[i]);
		
		int[] histogramme1B = histogrammeBleu(image1);
		int [] histogramme2B = histogrammeBleu(image2);
		double differenceB = 0;
		
		for (int i=0; i<256;i++)
			differenceB += Math.abs(histogramme1B[i]-histogramme2B[i]);
		
		double differenceTotale = (differenceR + differenceV + differenceB)/(3*256*256*256);
		
		if (differenceTotale>seuilDifference)
			return "La main n'est plus presente";
		else 
			return "La main est la";
	}
	
    public static int[] histogrammeRouge(BufferedImage image) {
 
        int[] histogrammeRouge = new int[256];
 
        for(int i=0; i<histogrammeRouge.length; i++) histogrammeRouge[i] = 0;
 
        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int red = new Color(image.getRGB (i, j)).getRed();
                histogrammeRouge[red]++;
            }
        }
 
        return histogrammeRouge;
 
    }
    
    public static int[] histogrammeVert(BufferedImage image) {
    	 
        int[] histogrammeVert = new int[256];
 
        for(int i=0; i<histogrammeVert.length; i++) histogrammeVert[i] = 0;
 
        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int vert = new Color(image.getRGB (i, j)).getGreen();
                histogrammeVert[vert]++;
            }
        }
 
        return histogrammeVert;
 
    }
    
    public static int[] histogrammeBleu(BufferedImage image) {
   	 
        int[] histogrammeBleu = new int[256];
 
        for(int i=0; i<histogrammeBleu.length; i++) histogrammeBleu[i] = 0;
 
        for(int i=0; i<image.getWidth(); i++) {
            for(int j=0; j<image.getHeight(); j++) {
                int blue = new Color(image.getRGB (i, j)).getBlue();
                histogrammeBleu[blue]++;
            }
        }
 
        return histogrammeBleu;
 
    }
}