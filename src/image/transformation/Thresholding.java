package image.transformation;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Thresholding {
	

	
	// methode pour mettre l'image originale en niveau de gris avec la luminance
    public static BufferedImage mettreEnNiveauDeGris(BufferedImage imageOriginale) {
 
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
    
    //technique du gradient pour seuiller l'image (obtention de points autour des contours de la feuille)
    public static BufferedImage mettreSeuil(BufferedImage imageGrise) {
		
		int seuilVertical = 15;
		int seuilHorizontal = 10;
		
		int grisHaut, grisBas,grisGauche,grisDroite,alpha;
        int nouveauPixel;
 
        BufferedImage imageSeuillee = new BufferedImage(imageGrise.getWidth(), imageGrise.getHeight(), imageGrise.getType());
 
        for(int i=0; i<imageGrise.getWidth()-1; i++) {
            for(int j=0; j<imageGrise.getHeight()-1; j++) {
 
                     
            	grisHaut = new Color(imageGrise.getRGB(i+1, j)).getRed();
            	grisBas = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisGauche = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisDroite = new Color(imageGrise.getRGB(i,j+1)).getRed();
                alpha = new Color(imageGrise.getRGB(i, j)).getAlpha();
                
                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                	nouveauPixel = colorerPixel(alpha,255,255,255);
                    imageSeuillee.setRGB(i,j,nouveauPixel);
       
                }
                else if (Math.abs(grisGauche-grisDroite)>seuilHorizontal) {
                	nouveauPixel = colorerPixel(alpha,255,255,255);
                    imageSeuillee.setRGB(i,j,nouveauPixel);
                	
                }
                
                else {
                	nouveauPixel = colorerPixel(alpha,0,0,0);
                	imageSeuillee.setRGB(i,j,nouveauPixel);
                }
            
            }
        }
 
        return imageSeuillee;
		
	}
    
 // colore un pixel avec Rouge, Vert, Bleu, Alpha avec les 8 bits
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

}
