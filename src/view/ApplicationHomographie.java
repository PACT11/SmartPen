package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ApplicationHomographie{
	public static BufferedImage imageOriginale, imageSortie;
	
	
	public static void main(String[] args) throws Exception{
		/*        
            File fichierOriginal = new File("imageblanche.jpg");
            String fichierSortie = "imageblanche_projetee";
            image1 = ImageIO.read(fichierOriginal);
            MainRansac mainRansac = new MainRansac();

    //      long tempsDebut = System.currentTimeMillis();
            ArrayList<Point> coinsDepart = mainRansac.donnerCoins();
            long tempsFin = System.currentTimeMillis();
            float seconds = (tempsFin - tempsDebut) / 1000F;
            System.out.println("Opération effectuée en: "+ Float.toString(seconds) + " secondes.");
            Point p5 = new Point(0,0);
            Point p6 = new Point(0,1200);
            Point p7 = new Point(800,0);
            Point p8 = new Point(800,1200);
            ArrayList<Point> coinsArrivee = new ArrayList<Point>(4);
            coinsArrivee.add(p5);
            coinsArrivee.add(p6);
            coinsArrivee.add(p7);
            coinsArrivee.add(p8);



            //ArrayList<Point> coinsArrivee = mainRansacBleu.donnerCoinsBleus();
            image2 = partieEntiere(image1,coinsDepart,coinsArrivee);
            creerImage(fichierSortie);   
        

        
            long tempsFin = System.currentTimeMillis();
            float seconds = (tempsFin - tempsDebut) / 1000F;
            System.out.println("Opération totale effectuée en: "+ Float.toString(seconds) + " secondes.");
            */
	}
	
	private static void creerImage(String sortie) throws IOException {
            File file = new File(sortie+".jpg");
            ImageIO.write(imageSortie, "jpg", file);
        }
	
	public static ArrayList<Point> transformPoints(ArrayList<Point> sourcePoints, ArrayList<Point> sourceCorners, ArrayList<Point> targetCorners) {
            ArrayList<Point> resultPoints = new ArrayList<>();
            double[] pt = {0,0}; 
            
            Point p5 = sourceCorners.get(0);
            Point p6 = sourceCorners.get(1);
            Point p7 = sourceCorners.get(2);
            Point p8 = sourceCorners.get(3);

            Point p1 = targetCorners.get(0);
            Point p2 = targetCorners.get(1);
            Point p3 = targetCorners.get(2);
            Point p4 = targetCorners.get(3);
            
            Matrix H = EstimationHomographie.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);
            
            for(Point point : sourcePoints) {
                pt = EstimationHomographie.phiInverse(point, H);
                resultPoints.add(new Point((int) pt[0],(int) pt[1]));
            }
            return resultPoints;
        }
	public static BufferedImage redressementImage(BufferedImage imageATransformer,ArrayList<Point> coinsArrivee, int width, int height, String UID) throws Exception {
		MainRansac mainRansac = new MainRansac();

                ArrayList<Point> coinsDepart = mainRansac.donnerCoins(imageATransformer, UID);

                //BufferedImage result = partieEntiere(imageATransformer,coinsDepart,coinsArrivee, width, height);
		
		return null;
	}
	

	public static BufferedImage partieEntiere(BufferedImage imageDepart,ArrayList<Point> coinsDepart, ArrayList<Point> coinsArrivee, int width, int height,boolean greyscale){
            int u,v, nouveauPixel,grey;
            Color color;
            int sourceWidth= imageDepart.getWidth(),sourceHeight = imageDepart.getHeight();
            double[] p;
            
            Point p1 = coinsDepart.get(0);
            Point p2 = coinsDepart.get(1);
            Point p3 = coinsDepart.get(2);
            Point p4 = coinsDepart.get(3);

            Point p5 = coinsArrivee.get(0);
            Point p6 = coinsArrivee.get(1);
            Point p7 = coinsArrivee.get(2);
            Point p8 = coinsArrivee.get(3);
            Matrix H = EstimationHomographie.getHomographyCoefficients(p1,p2,p3,p4,p5,p6,p7,p8);

            //Definition de la nouvelle image comme une image vierge
            BufferedImage imageRecalee = new BufferedImage(width,height, imageDepart.getType());

            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {  //On parcourt toute l'image
                    p = EstimationHomographie.phiInverse(new Point(i,j), H);
                    u = (int)(p[0]);
                    v = (int) (p[1]);

                    if ((u>0) && (u<sourceWidth) && (v>0) && (v<sourceHeight)) {
                            //Obtention des couleurs
                            color = new Color(imageDepart.getRGB(u,v));
                            if(greyscale) {
                                grey = (int) (0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue());
                            
                                //Coloration de la nouvelle image
                                nouveauPixel = colorerPixel(color.getAlpha(), grey, grey, grey);
                            } else {
                                //Coloration de la nouvelle image
                                nouveauPixel = colorerPixel(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
                            }
                            imageRecalee.setRGB(i, j, nouveauPixel);
                    }
                }
             }       

            return imageRecalee;
	}
         /*
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
        
        */

	
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
