import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class MettreMenu {
	
	private static BufferedImage imageOriginale, imageAvecMenu, menu;
	
	public static void main(String[] args) throws IOException{
		File fichierOriginal = new File("feuilledroite.jpg");
        String fichierSortie = "feuilledroite_avecmenu";
        imageOriginale = ImageIO.read(fichierOriginal);
        
        File fichierMenu = new File("Menu1.jpg");
        menu = ImageIO.read(fichierMenu);
        imageAvecMenu = mettreMenu(imageOriginale,tableauPixels(menu));
        creerImage(fichierSortie);      

	}
	
	private static void creerImage(String sortie) throws IOException {
        File file = new File(sortie+".jpg");
        ImageIO.write(imageAvecMenu, "jpg", file);
    }

	
	// methode pour mettre l'image originale en niveau de gris avec la luminance
    private static BufferedImage mettreMenu(BufferedImage imageOriginale,Color[][] menu) {
 
        int rouge, vert, bleu, alpha;
        int nouveauPixel;
 
        BufferedImage imageAvecMenu = new BufferedImage(imageOriginale.getWidth(), imageOriginale.getHeight(), imageOriginale.getType());
        
        //copie l'image originale dans imageAvecMenu
        Graphics g = imageAvecMenu.getGraphics();
        g.drawImage(imageOriginale, 0, 0, null);
        g.dispose();
        
        for(int i=0; i<menu.length; i++) {
            for(int j=0; j<menu[i].length; j++) {
            	rouge = menu[i][j].getRed();
                vert = menu[i][j].getGreen();
                bleu = menu[i][j].getBlue();
                alpha = menu[i][j].getAlpha();
                if (i<imageAvecMenu.getWidth() && j<imageAvecMenu.getHeight()) {
                	 nouveauPixel = colorerPixel(alpha, rouge,vert,bleu);
                     imageAvecMenu.setRGB(i, j, nouveauPixel);
                	
                }
            
 
            }
        }
 
        return imageAvecMenu;
 
    }
    
 // colore un pixel avec Rouge, Vert, Bleu, Alpha selon la m�thode des 8 bits
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
    
    private static Color[][] tableauPixels(BufferedImage menu) {
    	Color[][] result = new Color[menu.getWidth()][menu.getHeight()];
    	
    	for (int i =0 ; i<menu.getWidth() ; i++) {
    		for (int j =0;j<menu.getHeight();j++) {
    			result[i][j] = new Color(menu.getRGB(i,j));
    		}
    	}
    	
    	return result;
    	
    }

}
