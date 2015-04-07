import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class MainRansac2 {
  public static BufferedImage imageOriginale, imageGrise,imageRansac;
  
  public static void main(String[] argv) throws Exception {
    EnGris engris = new EnGris();
    
    String fileName = new String("imagebleue");
   
    //on ouvre le fichier de l'image en couleur
    File fichierOriginal = new File(fileName + ".jpg");
    String fichierSortie = fileName + "-ransac";
    imageOriginale = ImageIO.read(fichierOriginal);
    
    //mise en niveau de gris de l'image couleur
    //imageGrise = engris.mettreEnNiveauDeGris(imageOriginale);
    
    //tracage des droites a l'aide de l'algorithme de Ransac
    imageRansac = mettreDroite(imageOriginale);
    
    //creation de l'image de sortie
    creerImage(fichierSortie);   

  }
  
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageRansac, "jpg", file);
    }
	
	//algorithme pour determiner les bords de la feuille
	private static BufferedImage mettreDroite(BufferedImage imageGrise) {
		
		//on initialise une image vide
		BufferedImage imageRansac = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
        Graphics2D g2d = (Graphics2D)imageRansac.getGraphics();
        
        //on initialise les 4 bords
        Line2D verticale1, verticale2, horizontale1, horizontale2;
	 
        FittingInterface fitting = new SimpleFitting();
        

        ArrayList<Point> data1 = obtenirPointsBleusHorizontaux(imageGrise,0,imageGrise.getWidth(),0,500);
        Ransac ransac1 = new Ransac(fitting,data1,1);
        horizontale1 = appliquerRansac(ransac1); 
        g2d.draw(horizontale1);
        
        ArrayList<Point> data2 = obtenirPointsBleusHorizontaux(imageGrise,0,imageGrise.getWidth(),1200,1600);
        Ransac ransac2 = new Ransac(fitting,data2,1);
        horizontale2 = appliquerRansac(ransac2); 
        g2d.draw(horizontale2);
        
        ArrayList<Point> data3 = obtenirPointsBleusVerticaux(imageGrise,0,600,0,imageGrise.getHeight());
        Ransac ransac3 = new Ransac(fitting,data3,1);
        verticale1 = appliquerRansac(ransac3); 
        g2d.draw(verticale1);
        
        ArrayList<Point> data4 = obtenirPointsBleusVerticaux(imageGrise,2000,2500,0,imageGrise.getHeight());
        Ransac ransac4 = new Ransac(fitting,data4,1);
        verticale2 = appliquerRansac(ransac4); 
        g2d.draw(verticale2);
        
        
        ArrayList<Point> Coins = new ArrayList<Point>(4);
        Point pointHG = getIntersectionPoint(verticale1,horizontale1);
        Point pointBG = getIntersectionPoint(verticale1,horizontale2);
        Point pointHD = getIntersectionPoint(verticale2,horizontale1);
        Point pointBD = getIntersectionPoint(verticale2,horizontale2);
        
        Coins.add(pointHG);
        Coins.add(pointBG);
        Coins.add(pointHD);
        Coins.add(pointBD);
        	
        System.out.println("coord x = " + pointHG.getX() + "coord y = " + pointHG.getY());
        System.out.println("coord x = " + pointBG.getX() + "coord y = " + pointBG.getY());
        System.out.println("coord x = " + pointHD.getX() + "coord y = " + pointHD.getY());
        System.out.println("coord x = " + pointBD.getX() + "coord y = " + pointBD.getY());
        
        
 
        return imageRansac;
 
    }
	
	//obtention du tableau de points necessaire a l'algorithme de Ransac a l'aide du seuillage gradient
    private static ArrayList<Point> obtenirPointsHorizontaux(BufferedImage imageGrise, int xmin, int xmax, int ymin, int ymax) {
		
		int seuilVertical = 15;
		
		int grisHaut, grisBas;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=xmin; i<xmax-1; i++) {
            for(int j=ymin; j<ymax-1; j++) {
 
                     
            	grisHaut = new Color(imageGrise.getRGB(i,j)).getRed();
            	grisBas = new Color(imageGrise.getRGB(i,j+1)).getRed();
                
                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                	data.add(new Point(i,j));
       
                }
            
            }
        }
        
        System.out.println(data.size());
 
        return data;
		
	}
    
    private static ArrayList<Point> obtenirPointsVerticaux(BufferedImage imageGrise, int xmin, int xmax, int ymin, int ymax) {
		
		int seuilHorizontal = 10;
		
		int grisGauche,grisDroite;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=xmin; i<xmax-1; i++) {
            for(int j=ymin; j<ymax-1; j++) {
 
                     
            	grisGauche = new Color(imageGrise.getRGB(i+1, j)).getRed();
            	grisDroite = new Color(imageGrise.getRGB(i,j)).getRed();
            	
            	if (Math.abs(grisGauche-grisDroite)>seuilHorizontal) {
                	data.add(new Point(i,j));
                	
                }
            
            }
        }
        System.out.println(data.size());
        return data;
		
	}
    
private static ArrayList<Point> obtenirPointsBleusHorizontaux(BufferedImage imageOriginale, int xmin, int xmax, int ymin, int ymax) {
		
		int seuilVertical = 7;
		
		int grisHaut, grisBas;
 
        ArrayList<Point> data = new ArrayList<Point>();
 
        for(int i=xmin; i<xmax-1; i++) {
            for(int j=ymin; j<ymax-1; j++) {
 

                grisHaut = new Color(imageOriginale.getRGB(i,j)).getBlue();
            	grisBas = new Color(imageOriginale.getRGB(i,j+1)).getBlue();
                
                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                	data.add(new Point(i,j));
       
                }

            
            }
        }
        
        System.out.println(data.size());
        
        return data;
		
	}

private static ArrayList<Point> obtenirPointsBleusVerticaux(BufferedImage imageOriginale, int xmin, int xmax, int ymin, int ymax) {

	int seuilHorizontal = 7;
	
	int grisGauche,grisDroite;

    ArrayList<Point> data = new ArrayList<Point>();

    for(int i=xmin; i<xmax-1; i++) {
        for(int j=ymin; j<ymax-1; j++) {

                 
        	grisGauche = new Color(imageOriginale.getRGB(i+1, j)).getBlue();
        	grisDroite = new Color(imageOriginale.getRGB(i,j)).getBlue();

            if (Math.abs(grisGauche-grisDroite)>seuilHorizontal) {
            	data.add(new Point(i,j));
            	
            }
        
        }
    }
    
    System.out.println(data.size());
    
    return data;
	
}
    
    public static Line2D appliquerRansac(Ransac ransac) {
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
        
        	
 
        return lines;
    }
    
    //methode pour determiner le point d'intersection de deux droites
    public static Point getIntersectionPoint(Line2D line1, Line2D line2) {
        if (! line1.intersectsLine(line2) ) return null;
          double px = line1.getX1(),
                py = line1.getY1(),
                rx = line1.getX2()-px,
                ry = line1.getY2()-py;
          double qx = line2.getX1(),
                qy = line2.getY1(),
                sx = line2.getX2()-qx,
                sy = line2.getY2()-qy;

          double det = sx*ry - sy*rx;
          if (det == 0) {
            return null;
          } else {
            double z = (sx*(qy-py)+sy*(px-qx))/det;
            if (z==0 ||  z==1) return null;  // intersection at end point!
            return new Point((int)(px+z*rx), (int)(py+z*ry));
          }
     } // end intersection line-line
    
  
}