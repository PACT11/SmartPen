/*import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MainFinal{
	
	private static BufferedImage image1, image2;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File("sansmain.jpg");
        String fichierSortie = "sansmain_homographie";
        image1 = ImageIO.read(fichierOriginal);
        image2 = partieEntiere(image1);
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(image2, "jpg", file);
    }
	
	private static BufferedImage partieEntiere(BufferedImage imageDepart,ArrayList<Point> pointsDepart,ArrayList<Point> pointsArrivee){
		
		int u, v, rouge, vert, bleu, alpha, nouveauPixel;
		
		EstimationHomographie phi = new EstimationHomographie();

		Point p1 = pointsDepart.get(0);
		Point p2 = pointsDepart.get(1);
		Point p3 = pointsDepart.get(2);
		Point p4 = pointsDepart.get(3);
		
		Point p5 = pointsArrivee.get(0);
		Point p6 = pointsArrivee.get(1);
		Point p7 = pointsArrivee.get(2);
		Point p8 = pointsArrivee.get(3);
		
		Matrix H = phi.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);
		
		//Definition de la nouvelle image comme une image vierge
        BufferedImage imageRecalee = new BufferedImage(700,1000, imageDepart.getType());
		
		for(int i=0; i<imageRecalee.getWidth(); i++) {
	            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
	            	Point ij = new Point(i,j);
	            	Point p = phi.phiInverse(ij, H);
	            	u = (int) p.getX();
	            	v = (int) p.getY();
	            	
	            	if ((u>0) && (u<imageDepart.getWidth()) && (v>0) && (v<imageDepart.getHeight())) {
	            	
	            		//Obtention des couleurs
	            		rouge = new Color(imageDepart.getRGB(u,v)).getRed();
	            		vert = new Color(imageDepart.getRGB(u,v)).getGreen();
	            		bleu = new Color(imageDepart.getRGB(u,v)).getBlue();
	            		alpha = new Color(imageDepart.getRGB(u,v)).getAlpha();
	            	
	                
	            		//Coloration de la nouvelle image
	            		nouveauPixel = colorerPixel(alpha, rouge, vert, bleu);
	            		imageRecalee.setRGB(i, j, nouveauPixel);
	            	}
	            }
		 }       
	     
		return imageRecalee;
		
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

*/