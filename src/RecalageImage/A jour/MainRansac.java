import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class MainRansac {
  public static BufferedImage imageOriginale, imageGrise,imageRansac;
	
	
  public ArrayList<Point> donnerCoins(BufferedImage imageATransformer) throws Exception {
    EnGris engris = new EnGris();
   
    long tempsDebut1 = System.currentTimeMillis();
    //mise en niveau de gris de l'image couleur
    imageGrise = engris.mettreEnNiveauDeGris(imageATransformer);
    
    ArrayList<Point> result = ObtentionCoins(imageGrise,500,1200,1600,600,2000,2500,0.25);
    
    long tempsFin1 = System.currentTimeMillis();
    float seconds1 = (tempsFin1 - tempsDebut1) / 1000F;
    System.out.println("obtention coins effectuee en: "+ Float.toString(seconds1) + " secondes.");
    
    return result;

  }
  
  //methode pour redimensionner une image
  public static BufferedImage resizeImage(BufferedImage original, double factor) {
      int newWidth = (int) (factor * original.getWidth());
      int newHeight = (int) (factor * original.getHeight());
     
      BufferedImage newImage = new BufferedImage(newWidth, newHeight, original.getType());
     
      Graphics2D g = newImage.createGraphics();
     
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                      RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.setRenderingHint(RenderingHints.KEY_RENDERING,
                      RenderingHints.VALUE_RENDER_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                      RenderingHints.VALUE_ANTIALIAS_ON);
     
      g.drawImage(original, 0, 0, newWidth, newHeight, null);
      g.dispose();
     
      return newImage;
   }
  
  public static Point scalairePoint(Point point, int scalaire) {
	  return new Point(point.x*scalaire,point.y*scalaire);
  }
  
	//algorithme pour determiner les bords de la feuille
	private static ArrayList<Point> ObtentionCoins(BufferedImage image,int coin1,int coin2haut,int coin2bas,int coin3,int coin4gauche, int coin4droit,double scalaire) {
		
		BufferedImage imageGrise = resizeImage(image,scalaire);
        
        //on initialise les 4 bords
        Line verticale1, verticale2, horizontale1, horizontale2;
	 
        FittingInterface fitting = new SimpleFitting();
        
        //On applique l'algorithme de Ransac pour déterminer l'horizontale du haut
        //ArrayList<Point> data1 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),0,500);
        ArrayList<Point> data1 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),0,(int) (coin1*scalaire));
        Ransac ransac1 = new Ransac(fitting,data1,5);
        horizontale1 = appliquerRansac(ransac1,imageGrise); 
        
      //On applique l'algorithme de Ransac pour déterminer l'horizontale du bas
        //ArrayList<Point> data2 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),1200,1600);
        ArrayList<Point> data2 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),(int) (coin2haut*scalaire),(int) (coin2bas*scalaire));
        Ransac ransac2 = new Ransac(fitting,data2,5);
        horizontale2 = appliquerRansac(ransac2,imageGrise); 
        
      //On applique l'algorithme de Ransac pour déterminer la verticale du haut
        //ArrayList<Point> data3 = obtenirPointsVerticaux(imageGrise,0,600,0,imageGrise.getHeight());
        ArrayList<Point> data3 = obtenirPointsVerticaux(imageGrise,0,(int) (coin3*scalaire),0,imageGrise.getHeight());
        Ransac ransac3 = new Ransac(fitting,data3,5);
        verticale1 = appliquerRansac(ransac3,imageGrise); 
        
      //On applique l'algorithme de Ransac pour déterminer la verticale du bas
        //ArrayList<Point> data4 = obtenirPointsVerticaux(imageGrise,2000,2500,0,imageGrise.getHeight());
        ArrayList<Point> data4 = obtenirPointsVerticaux(imageGrise,(int)(coin4gauche*scalaire),(int)(coin4droit*scalaire),0,imageGrise.getHeight());
        Ransac ransac4 = new Ransac(fitting,data4,5);
        verticale2 = appliquerRansac(ransac4,imageGrise); 

        
        ArrayList<Point> Coins = new ArrayList<Point>(4);
        Point pointHG = getIntersectionPoint(verticale1,horizontale1,imageGrise);
        Point pointBG = getIntersectionPoint(verticale1,horizontale2,imageGrise);
        Point pointHD = getIntersectionPoint(verticale2,horizontale1,imageGrise);
        Point pointBD = getIntersectionPoint(verticale2,horizontale2,imageGrise);
       
        
        Coins.add(scalairePoint(pointHG,4));
        Coins.add(scalairePoint(pointBG,4));
        Coins.add(scalairePoint(pointHD,4));
        Coins.add(scalairePoint(pointBD,4));
        	
        System.out.println("coord x = " + pointHG.getX() + "coord y = " + pointHG.getY());
        System.out.println("coord x = " + pointBG.getX() + "coord y = " + pointBG.getY());
        System.out.println("coord x = " + pointHD.getX() + "coord y = " + pointHD.getY());
        System.out.println("coord x = " + pointBD.getX() + "coord y = " + pointBD.getY());
        
        
 
        return Coins;
 
    }

	//obtention du tableau de points necessaire a l'algorithme de Ransac a l'aide du seuillage gradient
    private static ArrayList<Point> obtenirPointsHorizontaux(BufferedImage imageGrise, int xmin, int xmax, int ymin, int ymax) {
		
		int seuilVertical = 35;
		
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
		
		int seuilHorizontal = 30;
		
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

    public static Line appliquerRansac(Ransac ransac,BufferedImage imageGrise) {
    	//On execute l'algorithme de Ransac
    	while (!ransac.isFinished()) {
        	ransac.computeNextStep();
        }

        Line line = ransac.getBestModel();
       
        Line result;
        
        if (line.isHorizontal())
        {
          double y = line.getY(0);
          Point p1 = new Point(0,(int)(y));
          Point p2 = new Point(imageGrise.getWidth(),(int)(y));
          result = new Line(p1,p2);
          System.out.println("Droite horizontale, y =" + y );
        }
        else if (line.isVertical())
        {
          double x = line.getX(0);
          Point p1 = new Point((int)(x),0);
          Point p2 = new Point((int)(x),imageGrise.getHeight());
          System.out.println("Droite verticale, x =" + x);
          result = new Line(p1,p2);
        }
        else
        {
        	int x1 = (int)(line.getX(0));
            int y1 = (int) line.getY(x1);
            Point p1 = new Point(x1,y1);
            int x2 = (int) line.getX(imageGrise.getHeight());
            int y2 = (int) line.getY(x2);
            Point p2 = new Point(x2,y2);
            result = new Line(p1,p2);
            System.out.println("Droite oblique : " + x1 + " " + y1 + " " + x2 + " " + y2);
        }
        
        	
 
        return result;
    }
    
    
    public static Point getIntersectionPoint(Line line1, Line line2,BufferedImage imageGrise) {
    	double x1 = line1.getX(0);
        double y1 = line1.getY(x1);
        double x2 = line1.getX(imageGrise.getHeight());
        double y2 = line1.getY(x2);
        double x3 = line2.getX(0);
        double y3 = line2.getY(x1);
        double x4 = line2.getX(imageGrise.getHeight());
        double y4 = line2.getY(x2);
        if (! linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4) ) return null;
          double px = x1,
                 py = y1,
                 rx = x2-px,
                 ry = y2-py;
          double qx = x3,
                 qy = y3,
                 sx = x4-qx,
                 sy = y4-qy;

          double det = sx*ry - sy*rx;
          if (det == 0) {
            return null;
          } else {
            double z = (sx*(qy-py)+sy*(px-qx))/det;
            if (z==0 ||  z==1) return null;  // intersection at end point!
            return new Point((int)(px+z*rx), (int)(py+z*ry));
          }
     }
    
    public static boolean linesIntersect(double x1, double y1,
    		                                          double x2, double y2,
    		                                          double x3, double y3,
    		                                          double x4, double y4)
    		     {
    		         return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
    		                  relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
    		                 && (relativeCCW(x3, y3, x4, y4, x1, y1) *
    		                     relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    		     }
    
    public static int relativeCCW(double x1, double y1,
    		                                   double x2, double y2,
    		                                   double px, double py)
    		     {
    		         x2 -= x1;
    		         y2 -= y1;
    		         px -= x1;
    		         py -= y1;
    		         double ccw = px * y2 - py * x2;
    		         if (ccw == 0.0) {
    		             // The point is colinear, classify based on which side of
    		             // the segment the point falls on.  We can calculate a
    		             // relative value using the projection of px,py onto the
    	             // segment - a negative value indicates the point projects
    		             // outside of the segment in the direction of the particular
    		             // endpoint used as the origin for the projection.
    		             ccw = px * x2 + py * y2;
    		             if (ccw > 0.0) {
    		                 // Reverse the projection to be relative to the original x2,y2
    		                 // x2 and y2 are simply negated.
    		                 // px and py need to have (x2 - x1) or (y2 - y1) subtracted
    		                 //    from them (based on the original values)
    		                 // Since we really want to get a positive answer when the
    		                 //    point is "beyond (x2,y2)", then we want to calculate
    		                 //    the inverse anyway - thus we leave x2 & y2 negated.
    		                 px -= x2;
    		                 py -= y2;
    	                 ccw = px * x2 + py * y2;
    		                 if (ccw < 0.0) {
    	                     ccw = 0.0;
    		                 }
    		             }
    		         }
    		         return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
    		     }

    
    
    
    /*methode pour determiner le point d'intersection de deux droites
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
     }
     
     */

    
  
}