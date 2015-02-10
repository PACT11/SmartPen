import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class EnGris {
	
private static BufferedImage imageOriginale, imageNiveauDeGris;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_gris";
        imageOriginale = ImageIO.read(fichierOriginal);
        imageNiveauDeGris = mettreEnNiveauDeGris(imageOriginale);
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageNiveauDeGris, "jpg", file);
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
