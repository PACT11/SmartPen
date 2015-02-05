import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Seuillage {
	
private static BufferedImage imageGrise, imageSeuillee;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_seuil";
        imageGrise = ImageIO.read(fichierOriginal);
        imageSeuillee = mettreSeuil(imageGrise);
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageSeuillee, "jpg", file);
    }
	
	private static BufferedImage mettreSeuil(BufferedImage imageGrise) {
		
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
