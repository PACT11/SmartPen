import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ApplicationHomographie{
	
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
	
	private static BufferedImage partieEntiere(BufferedImage imageBrute){
		
		int u, v, rouge, vert, bleu, alpha, nouveauPixel;
		
		EstimationHomographie phi = new EstimationHomographie();

		Point p1 = new Point(463,757);
		Point p2 = new Point(75,2121);
		Point p3 = new Point(1591,700);
		Point p4 = new Point(1854,2158);
		
		Point p5 = new Point(0,0);
		Point p6 = new Point(0,800);
		Point p7 = new Point(600,0);
		Point p8 = new Point(600,800);
		
		Matrix H = phi.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);
		
		//Definition de la nouvelle image comme une image vierge
        BufferedImage imageRecalee = new BufferedImage(600,800, imageBrute.getType());
		
		for(int i=0; i<imageRecalee.getWidth(); i++) {
	            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
	            	Point ij = new Point(i,j);
	            	Point p = phi.phiInverse(ij, H);
	            	u = (int) p.getX();
	            	v = (int) p.getY();
	            	
	            	if ((u>0) && (u<imageBrute.getWidth()) && (v>0) && (v<imageBrute.getHeight())) {
	            	
	            		//Obtention des couleurs
	            		rouge = new Color(imageBrute.getRGB(u,v)).getRed();
	            		vert = new Color(imageBrute.getRGB(u,v)).getGreen();
	            		bleu = new Color(imageBrute.getRGB(u,v)).getBlue();
	            		alpha = new Color(imageBrute.getRGB(u,v)).getAlpha();
	            	
	                
	            		//Coloration de la nouvelle image
	            		nouveauPixel = colorerPixel(alpha, rouge, vert, bleu);
	            		imageRecalee.setRGB(i, j, nouveauPixel);
	            	}
	            }
		 }       
	     
		return imageRecalee;
		
	}

	public static BufferedImage ponderation(BufferedImage imageBrute){
		
		int x1, y1, x2, y2, x3, y3, x4, y4, rouge, rouge1, rouge2, rouge3, rouge4, vert, vert1, vert2, vert3, vert4, bleu, bleu1, bleu2, bleu3, bleu4, alpha, alpha1, alpha2, alpha3, alpha4, gris, nouveauPixel;
		double u, v, dx, dy;
		
		//Definition de la nouvelle image comme une image vierge
		BufferedImage imageRecalee = new BufferedImage(imageBrute.getWidth(), imageBrute.getHeight(), imageBrute.getType());
		
		for(int i=0; i<imageRecalee.getWidth(); i++) {
	            for(int j=0; j<imageRecalee.getHeight(); j++) {  //On parcourt toute l'image
	            	
	            	//Definition de tous les points necessaires
	            	u = (int)(i*Math.cos(0) - j*Math.sin(0));
	            	v = (int)(i*Math.cos(0) + j*Math.sin(0));
	            	x1=(int)(u);
	            	y1= (int)(v);
	            	x2 = x1+1;
	            	y2 = y1;
	            	x3 = x1;
	            	y3 = (y1+1);
	            	x4 = x2;
	            	y4 = y3;
	            	dx = (u-x1);
	            	dy = (v-y1);
	            	
	            	//Et de leur couleur
	            	rouge1 = new Color(imageBrute.getRGB(x1, y1)).getRed();
	                vert1 = new Color(imageBrute.getRGB(x1, y1)).getGreen();
	                bleu1 = new Color(imageBrute.getRGB(x1, y1)).getBlue();
	                alpha1 = new Color(imageBrute.getRGB(x1, y1)).getAlpha();
	                
	                rouge2 = new Color(imageBrute.getRGB(x2, y2)).getRed();
	                vert2 = new Color(imageBrute.getRGB(x2, y2)).getGreen();
	                bleu2 = new Color(imageBrute.getRGB(x2, y2)).getBlue();
	                alpha2 = new Color(imageBrute.getRGB(x2, y2)).getAlpha();
	                
	                rouge3 = new Color(imageBrute.getRGB(x3, y3)).getRed();
	                vert3 = new Color(imageBrute.getRGB(x3, y3)).getGreen();
	                bleu3 = new Color(imageBrute.getRGB(x3, y3)).getBlue();
	                alpha3 = new Color(imageBrute.getRGB(x3, y3)).getAlpha();
	                
	                rouge4 = new Color(imageBrute.getRGB(x4, y4)).getRed();
	                vert4 = new Color(imageBrute.getRGB(x4, y4)).getGreen();
	                bleu4 = new Color(imageBrute.getRGB(x4, y4)).getBlue();
	                alpha4 = new Color(imageBrute.getRGB(x4, y4)).getAlpha();
	                
	                rouge = (int)(dx*dy*rouge1 + (1-dx)*dy*rouge2 + dx*(1-dy)*rouge3 + (1-dx)*(1-dy)*rouge4);
	                vert = (int)(dx*dy*vert1 + (1-dx)*dy*vert2 + dx*(1-dy)*vert3 + (1-dx)*(1-dy)*vert4);
	                bleu = (int)(dx*dy*bleu1 + (1-dx)*dy*bleu2 + dx*(1-dy)*bleu3 + (1-dx)*(1-dy)*bleu4);
	                alpha = (int)(dx*dy*alpha1 + (1-dx)*dy*alpha2 + dx*(1-dy)*alpha3 + (1-dx)*(1-dy)*alpha4);
	                gris = (int) (0.21 * rouge + 0.71 * vert + 0.07 * bleu);

	                //Coloration de l'image
	                nouveauPixel = colorerPixel(alpha, gris, gris, gris);
	                imageRecalee.setRGB(i, j, nouveauPixel);
	                
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