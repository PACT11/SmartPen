import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ApplicationHomographie2{
	public static BufferedImage imageOriginale, imageSortie;
	
	
	public static void main(String[] args) throws Exception{
		/*
		long tempsDebut = System.currentTimeMillis();
		File fichierOriginal = new File("imageblanche.jpg");
        String fichierSortie = "imageblanche_redressee";
        BufferedImage imageOriginale = ImageIO.read(fichierOriginal);
        Point p5 = new Point(0,0);
        Point p6 = new Point(0,1200);
        Point p7 = new Point(800,0);
        Point p8 = new Point(800,1200);
        ArrayList<Point> coinsArrivee = new ArrayList<Point>(4);
        coinsArrivee.add(p5);
        coinsArrivee.add(p6);
        coinsArrivee.add(p7);
        coinsArrivee.add(p8);
        imageSortie = enImage(redressementImage(tableauPixels(imageOriginale), coinsArrivee, 800,1200));
        creerImage(fichierSortie);      
        
        long tempsFin = System.currentTimeMillis();
        float seconds = (tempsFin - tempsDebut) / 1000F;
        System.out.println("Opération totale effectuée en: "+ Float.toString(seconds) + " secondes.");
*/
		File fichierOriginal = new File("imagebrute.jpg");
        BufferedImage imageOriginale = ImageIO.read(fichierOriginal);
        
        tableauPixels(imageOriginale);
	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageSortie, "jpg", file);
    }
	
	
	public static Color[][] redressementImage(Color[][] imageATransformer,ArrayList<Point> coinsArrivee, int width, int height) throws Exception {
		MainRansac mainRansac = new MainRansac();
        BufferedImage image = enImage(imageATransformer);
        ArrayList<Point> coinsDepart = mainRansac.donnerCoins(image);

        BufferedImage result = partieEntiere(image,coinsDepart,coinsArrivee, width, height);
		
		return tableauPixels(result);
	}
	

	private static BufferedImage partieEntiere(BufferedImage imageDepart,ArrayList<Point> coinsDepart, ArrayList<Point> coinsArrivee, int width, int height){
		
		int u,v, rouge, vert, bleu, alpha, nouveauPixel;
		
		EstimationHomographie phi = new EstimationHomographie();

        Point p1 = coinsDepart.get(0);
        Point p2 = coinsDepart.get(1);
        Point p3 = coinsDepart.get(2);
        Point p4 = coinsDepart.get(3);

        Point p5 = coinsArrivee.get(0);
        Point p6 = coinsArrivee.get(1);
        Point p7 = coinsArrivee.get(2);
        Point p8 = coinsArrivee.get(3);
		
		/*Point p1 = new Point(490,60);
		Point p2 = new Point(51,500);
		Point p3 = new Point(860,280);
		Point p4 = new Point(420,720);
		
		Point p5 = new Point(0,0);
		Point p6 = new Point(0,500);
		Point p7 = new Point(600,0);
		Point p8 = new Point(600,500);
		
		*/
        
		Matrix H = phi.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);
       
		
		//Definition de la nouvelle image comme une image vierge
        BufferedImage imageRecalee = new BufferedImage(width,height, imageDepart.getType());
        
		for(int i=0; i<imageRecalee.getWidth(); i++) {
	            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
	            	Point ij = new Point(i,j);
	            	double[] p = phi.phiInverse(ij, H);
	            	 u = (int)(p[0]);
	            	 v = (int) (p[1]);
	            	
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
	
    private static Color[][] tableauPixels(BufferedImage image) {
    	Color[][] result = new Color[image.getWidth()][image.getHeight()];
    	
    	for (int i =0 ; i<image.getWidth() ; i++) {
    		for (int j =0;j<image.getHeight();j++) {
    			result[i][j] = new Color(image.getRGB(i,j));
    		}
    	}
    	
    	return result;
    	
    }
    
    private static BufferedImage enImage(Color[][] image) {
    	BufferedImage result = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_4BYTE_ABGR);
    	
    	int rouge,vert,bleu,alpha,nouveauPixel;
    	 //met le menu dans imageAvecMenu
        for(int i=0; i<image.length; i++) {
            for(int j=0; j<image[i].length; j++) {
            	rouge = image[i][j].getRed();
                vert = image[i][j].getGreen();
                bleu = image[i][j].getBlue();
                alpha = image[i][j].getAlpha();
                nouveauPixel = colorerPixel(alpha, rouge,vert,bleu);
                result.setRGB(i, j, nouveauPixel);
                	
            
 
            }
        }
    	return result;
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