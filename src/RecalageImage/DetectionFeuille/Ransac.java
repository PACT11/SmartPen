import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Ransac {
	
	private static BufferedImage imageGrise, imageRebords;
	int iterationsMax = 100;
	int seuilInliers = 10000;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_droites";
        imageGrise = ImageIO.read(fichierOriginal);
        imageRebords = mettreEnNiveauDeGris(imageOriginale);
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageRebords, "jpg", file);
    }

	
	public static Point[][] nuageDePoints(BufferedImage imageGrise) {
		Point[][] tableauPoints = new Point[imageGrise.getWidth()][imageGrise.getHeight()];
		
		int seuilVertical = 15;
		int seuilHorizontal = 10;
		
		int grisHaut, grisBas,grisGauche,grisDroite;
        Point nouveauPoint;
 
 
        for(int i=0; i<imageGrise.getWidth()-1; i++) {
            for(int j=0; j<imageGrise.getHeight()-1; j++) {
 
                     
            	grisHaut = new Color(imageGrise.getRGB(i+1, j)).getRed();
            	grisBas = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisGauche = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisDroite = new Color(imageGrise.getRGB(i,j+1)).getRed();
                
                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                	nouveauPoint = new Point(i,j);
                	tableauPoints[i][j] = nouveauPoint;
       
                }
                else if (Math.abs(grisGauche-grisDroite)>seuilHorizontal) {
                	nouveauPoint = new Point(i,j);
                	tableauPoints[i][j] = nouveauPoint;
                }
                
                else {
                	nouveauPoint = new Point(0,0);
                	tableauPoints[i][j] = nouveauPoint;
                }
            
            }
        }
		
		return tableauPoints;
		
	}
	
	public static BufferedImage tracerDroite(Point[][] tableauPointsDroite,BufferedImage imageGrise) {
		
		int nouveauPixel;
		BufferedImage imageRebords = new BufferedImage(imageGrise.getWidth(), imageGrise.getHeight(), imageGrise.getType());
		
		
		for(int i=0; i<imageRebords.getWidth(); i++) {
            for(int j=0; j<imageRebords.getHeight(); j++) {
 
                // obtention des pixels par le rouge, le vert, le bleu et l'alpha
                rouge = new Color(imageOriginale.getRGB(i, j)).getRed();
                vert = new Color(imageOriginale.getRGB(i, j)).getGreen();
                bleu = new Color(imageOriginale.getRGB(i, j)).getBlue();
                alpha = new Color(imageOriginale.getRGB(i, j)).getAlpha();

                nouveauPixel = colorerPixel(255,0,0,0);
 
                // mets les pixels dans l'image
                imageGrise.setRGB(i, j, nouveauPixel);
 
            }
        }
		
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
