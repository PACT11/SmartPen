package image.transformation;


import java.awt.Color;
import java.awt.image.BufferedImage;


public class EnGris {
	

	// methode pour mettre l'image originale en niveau de gris avec la luminance
    public BufferedImage mettreEnNiveauDeGris(BufferedImage imageOriginale) {
        Color color;
        int nouveauPixel;
 
        BufferedImage imageGrise = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            for(int j=0; j<imageOriginale.getHeight(); j++) {
                // obtention des pixels par le rouge, le vert, le bleu et l'alpha
                color = new Color(imageOriginale.getRGB(i, j));        

                int gris = (int) (0.21*color.getRed() + 0.71*color.getGreen() + 0.07*color.getBlue());
                nouveauPixel = colorerPixel(color.getAlpha(), gris, gris, gris);
 
                // mets les pixels dans l'image
                imageGrise.setRGB(i, j, nouveauPixel);
            }
        }
 
        return imageGrise;
 
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