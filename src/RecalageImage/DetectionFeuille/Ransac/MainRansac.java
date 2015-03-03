import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MainRansac {
  public static BufferedImage imageOriginale, imageGrise, imageRansac;
  
  public static void main(String[] argv) throws Exception {
    EnGris engris = new EnGris();
   
    //on ouvre le fichier de l'image en couleur
    File fichierOriginal = new File("nuage.jpg");
    String fichierSortie = "nuage-ransac";
    imageOriginale = ImageIO.read(fichierOriginal);
    
    //mise en niveau de gris de l'image couleur
    imageGrise = engris.mettreEnNiveauDeGris(imageOriginale);
    
    //tracage des droites ˆ l'aide de l'algorithme de Ransac
    imageRansac = mettreDroite(imageGrise);
    
    //creation de l'image de sortie
    creerImage(fichierSortie);   

  }
  
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageRansac, "jpg", file);
    }
	
	//algorithme pour determiner les bords de la feuille
	private static BufferedImage mettreDroite(BufferedImage imageOriginale) {
	
 
        BufferedImage imageRansac = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());

        
        FittingInterface fitting = new SimpleFitting();
        Ransac ransac = new Ransac(fitting,obtenirPoints(imageGrise),5);
        
        //on initialise les 4 bords
        
       // Line2D verticale1, verticale2, horizontale1, horizontale2;
        
        //k represente le nombre d'occurrences de l'algorithme de Ransac
        for (int k = 0; k<2;k++) {
        	
        	
        	ransac.resetIterationNb();
        	
        	//On execute l'algorithme de Ransac
        	while (!ransac.isFinished()) {
            	ransac.computeNextStep();
            }

            Line line = ransac.getBestModel();
           
            Line2D lines;
            
            if (line.isHorizontal())
            {
              double y = line.getY(0);
              lines = new Line2D.Double(0, y, imageOriginale.getWidth(), y);
              System.out.println("Droite horizontale, y =" + y );
            }
            else if (line.isVertical())
            {
              double x = line.getX(0);
              System.out.println("Droite verticale, x =" + x);
              lines = new Line2D.Double(x, 0,x, imageOriginale.getHeight());
            }
            else
            {
            	double x1 = line.getX(0);
                double y1 = line.getY(x1);
                double x2 = line.getX(imageOriginale.getHeight());
                double y2 = line.getY(x2);
                lines = new Line2D.Double(x1, y1, x2, y2);
                System.out.println("Droite oblique : " + x1 + " " + y1 + " " + x2 + " " + y2);
            }
            
            Graphics2D g2d = (Graphics2D)imageRansac.getGraphics();
            
            g2d.draw(lines);
            	
     
            for (Point point : ransac.getInliers()) {
            	ransac.removePoint(point);
            }
        	
            
        }
        
 
        return imageRansac;
 
    }
	
	//obtention du tableau de points necessaire a l'algorithme de Ransac a l'aide du seuillage gradient
    private static ArrayList<Point> obtenirPoints(BufferedImage imageGrise) {
		
		int seuilVertical = 15;
		int seuilHorizontal = 10;
		
		int grisHaut, grisBas,grisGauche,grisDroite;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=0; i<imageGrise.getWidth()-1; i++) {
            for(int j=0; j<imageGrise.getHeight()-1; j++) {
 
                     
            	grisHaut = new Color(imageGrise.getRGB(i+1, j)).getRed();
            	grisBas = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisGauche = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisDroite = new Color(imageGrise.getRGB(i,j+1)).getRed();
                
                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                	data.add(new Point(i,j));
       
                }
                else if (Math.abs(grisGauche-grisDroite)>seuilHorizontal) {
                	data.add(new Point(i,j));
                	
                }
            
            }
        }
 
        return data;
		
	}
  
}