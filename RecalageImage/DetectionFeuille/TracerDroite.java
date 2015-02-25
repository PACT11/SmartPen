import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TracerDroite {
	
private static BufferedImage imageOriginale, imageAvecDroite;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_droite";
        imageOriginale = ImageIO.read(fichierOriginal);
        imageAvecDroite = mettreDroite(imageOriginale);
        creerImage(fichierSortie);      
        
	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageAvecDroite, "jpg", file);
    }
	
	public static BufferedImage mettreDroite(BufferedImage imageOriginale) {
		int alpha;
        int nouveauPixel;
 
        BufferedImage imageAvecDroite = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
 
        for(int i=0; i<imageOriginale.getWidth(); i++) {
            	
            	int x = i;
            	int y = 3*x+4;
            	
            	if (y<imageOriginale.getHeight()) {
 
            		// obtention des pixels par le rouge, le vert, le bleu et l'alpha
            		alpha = new Color(imageOriginale.getRGB(x, y)).getAlpha();
 
            		nouveauPixel = colorerPixel(alpha, 255, 255, 255);
 
            		// mets les pixels dans l'image
            		imageAvecDroite.setRGB(x, y, nouveauPixel);
            	}
 
        }
 
        return imageAvecDroite;
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
