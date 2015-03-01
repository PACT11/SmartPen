import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TracerDroite {
	
private static BufferedImage imageOriginale, imageAvecDroite;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File(args[0]+".jpg");
        String fichierSortie = args[0]+"_droite";
        imageOriginale = ImageIO.read(fichierOriginal);
        imageAvecDroite = mettreDroite(imageOriginale);
        creerImage(fichierSortie);      
        
	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageAvecDroite, "jpg", file);
    }
	
	public static BufferedImage mettreDroite(BufferedImage imageOriginale) {
        Point point1 = new Point(0,0);
        Point point2 = new Point(500,800);
        
        
        Line2D.Double line = new Line2D.Double(point1,point2);
 
        BufferedImage imageAvecDroite = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
 
        Graphics2D g2d = (Graphics2D)imageAvecDroite.getGraphics();

        g2d.draw(line);
 
 
        return imageAvecDroite;
	}

}
