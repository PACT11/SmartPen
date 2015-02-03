import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Binariser {
	
	private static BufferedImage imageOriginale, imageNiveauDeGris, imageBinarisee;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_binarisee";
        imageOriginale = ImageIO.read(fichierOriginal);
        imageNiveauDeGris = mettreEnNiveauDeGris(imageOriginale);
        imageBinarisee = binariser(imageNiveauDeGris);
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageBinarisee, "jpg", file);
    }

	
	// methode pour mettre l'image originale en niveau de gris avec la luminance
    private static BufferedImage mettreEnNiveauDeGris(BufferedImage imageOriginale) {
 
        int rouge, vert, bleu, alpha;
        int nouveauPixel;
 
        BufferedImage imageGrise = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            for(int j=0; j<imageOriginale.getHeight(); j++) {
 
                // obtention des pixels par le rouge, le vert, le bleu et l'alpha
                rouge = new Color(imageOriginale.getRGB(i, j)).getRed();
                vert = new Color(imageOriginale.getRGB(i, j)).getGreen();
                bleu = new Color(imageOriginale.getRGB(i, j)).getBlue();
                alpha = new Color(imageOriginale.getRGB(i, j)).getAlpha();
 
                int gris = (int) (0.21 * rouge + 0.71 * vert + 0.07 * bleu);
                nouveauPixel = colorerPixel(alpha, gris, gris, gris);
 
                // mets les pixels dans l'image
                imageGrise.setRGB(i, j, nouveauPixel);
 
            }
        }
 
        return imageGrise;
 
    }
    
 // colore un pixel avec Rouge, Vert, Bleu, Alpha selon la mŽthode des 8 bits
    private static int colorerPixel(int alpha,int rouge, int vert, int bleu) {
 
        int nouveauPixel = 0;
        nouveauPixel += alpha;
        nouveauPixel = nouveauPixel << 8;
        nouveauPixel += rouge ;
        nouveauPixel = nouveauPixel << 8;
        nouveauPixel += vert;
        nouveauPixel = nouveauPixel << 8;
        nouveauPixel += bleu;
 
        return nouveauPixel;
 
    }
    
    // Renvoie l'histogramme de l'image en niveau de gris
    public static int[] imageHistogram(BufferedImage imageGrise) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<imageGrise.getWidth(); i++) {
            for(int j=0; j<imageGrise.getHeight(); j++) {
                int red = new Color(imageGrise.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
    
    // retourne le seuil pour binariser l'image
    private static int determinerSeuil(BufferedImage imageOriginale) {
 
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
    
    private static BufferedImage binariser(BufferedImage imageGrise) {
    	 
        int rouge;
        int nouveauPixel;
        
        int seuil = determinerSeuil(imageGrise);
 
        BufferedImage imageBinarisee = new BufferedImage(imageGrise.getWidth(), imageGrise.getHeight(), imageGrise.getType());
 
        for(int i=0; i<imageGrise.getWidth(); i++) {
            for(int j=0; j<imageGrise.getHeight(); j++) {
 
                // acquiere les pixels
                rouge = new Color(imageGrise.getRGB(i, j)).getRed();
                int alpha = new Color(imageGrise.getRGB(i, j)).getAlpha();
                if(rouge > seuil) {
                    nouveauPixel = 255;
                }
                else {
                    nouveauPixel = 0;
                }
                nouveauPixel = colorerPixel(alpha, nouveauPixel, nouveauPixel, nouveauPixel);
                imageBinarisee.setRGB(i, j, nouveauPixel); 
 
            }
        }
 
        return imageBinarisee;
 
    }
 

}
