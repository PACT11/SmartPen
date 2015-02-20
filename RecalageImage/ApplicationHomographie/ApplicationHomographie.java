import java.math.*;

public class ApplicationHomographie{
	
	
	public static BufferedImage partieEntiere(BufferedImage imageBrute){
		
		int u, v, rouge, vert, bleu, alpha, nouveauPixel;
		
		//Definition de la nouvelle image comme une image vierge
		int imageRecaleeWidth, imageRecaleeHeight, imageRecaleeType;
        BufferedImage imageRecalee = new BufferedImage(imageRecaleeWidth, imageRecaleeHeight, imageRecaleeType);
		
		for(int i=0; i<imagerecaleeWidth; i++) {
	            for(int j=0; j<imagerecaleeHeight; j++) {  //On parcourt toute l'image
	            	(u,v) = floor(phiInverse(i,j)+(1/2,1/2)); //Ce sont les coordonnées du pixel dans l'ancienne image
	                //Il manque l'implentation de la fonction phi
	            	
	            	
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
	     
		return imageRecalee
		
	}
	
	public static BufferedImage ponderation(BufferedImage imageBrute){
		
		int x1, y1, x2, y2, x3, y3, x4, y4, rouge, rouge1, rouge2, rouge3 rouge4, vert, vert1, vert2, vert3, vert4, bleu, bleu1, bleu2, bleu3, bleu4, alpha, alpha1, alpha2, alpha3, alpha4, gris, nouveauPixel;
		double u, v, dx, dy;
		
		//Definition de la nouvelle image comme une image vierge
		int imageRecaleeWidth, imageRecaleeHeight, imageRecaleeType
        BufferedImage imageRecalee = new BufferedImage(imageRecaleeWidth, imageRecaleeHeight, imageRecaleeType);
		
		for(int i=0; i<imagerecaleeWidth; i++) {
	            for(int j=0; j<imagerecaleeHeight; j++) {  //On parcourt toute l'image
	            	
	            	//Définition de tous les points nécessaires
	            	(u,v) = phiInverse(i,j);
	            	(x1,x2) = (floor(u), floor(v));
	            	x2 = (x1+1);
	            	y2 = x2;
	            	x3 = x1;
	            	y3 = (y1+1);
	            	x4 = (x1+1);
	            	y4 = (y1+1);
	            	dx = (u-x1);
	            	dy = (v-x2);
	            	
	            	//Et de leur couleur
	            	rouge1 = new Color(imageBrute.getRGB(x2, y2)).getRed();
	                vert1 = new Color(imageBrute.getRGB(x2, y2)).getGreen();
	                bleu1 = new Color(imageBrute.getRGB(x2, y2)).getBlue();
	                alpha1 = new Color(imageBrute.getRGB(x2, y2)).getAlpha();
	                
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
	                
	                rouge = floor(dx*dy*rouge1 + (1-dx)*dy*rouge2 + dx*(1-dy)*rouge3 + (1-dx)*(1-dy)*rouge4);
	                vert = floor(dx*dy*vert1 + (1-dx)*dy*vert2 + dx*(1-dy)*vert3 + (1-dx)*(1-dy)*vert4);
	                bleu = floor(dx*dy*bleu1 + (1-dx)*dy*bleu2 + dx*(1-dy)*bleu3 + (1-dx)*(1-dy)*bleu4);
	                alpha = floor(dx*dy*alpha1 + (1-dx)*dy*alpha2 + dx*(1-dy)*alpha3 + (1-dx)*(1-dy)*alpha4);
	                gris = (0.21 * rouge + 0.71 * vert + 0.07 * bleu);

	                //Coloration de l'image
	                nouveauPixel = colorerPixel(alpha, gris, gris, gris);
	                imageRecalee.setRGB(i, j, nouveauPixel);
	                
	            }
		 }       
	     
		return imageRecalee
	}
	
}