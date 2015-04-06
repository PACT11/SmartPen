/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.floor;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author arnaud
 */
public class CornerFinder {
    public static final float THRESHOLDFACTOR = 0.20F;
    public static final int STEPDIVIDER = 200;
    
    public static CornerCache cache;
    
    public static void main(String[] args) throws IOException {
        String[] files = {"a.jpg","b.jpg","c.jpg", "d.jpg","e.jpg","f.jpg","h.jpg"};
        
        for(String filename : files) {
            File fichierOriginal = new File("/Users/arnaud/NetBeansProjects/SmartPen/src/" + filename);
            BufferedImage image = ImageIO.read(fichierOriginal);
        
            long tempsDebut = System.currentTimeMillis();
            ArrayList<Point> corners = findCorners(image);      
            long tempsFin = System.currentTimeMillis();
        
            System.out.println("Opération totale effectuée en: "+ (tempsFin - tempsDebut) + " ms");
            System.out.println("width = " + image.getWidth());
            System.out.println("height = " + image.getHeight());
            for(Point point : corners) {
                System.out.println("x : " + point.x + ", y : " + point.y);
                image.getGraphics().setColor(Color.WHITE);
                image.getGraphics().fillRect(point.x-10, point.y-10, 20, 20);
            }
            File file = new File(filename);
            ImageIO.write(image, "jpg", file);
        }
    }
    
    public static ArrayList<Point> findCorners(BufferedImage image) {
        int[][] img = grayscale(image, (int) floor(image.getWidth()/STEPDIVIDER),(int) floor(image.getHeight()/STEPDIVIDER));
        int i,lastSum, min, max;
        int[] xSums = new int[img.length];
        int[] ySums = new int[img[0].length];
        int[] xs, ys;
        
        min=0; max=0;
        lastSum = colSum(img, 0);
        for(i=1;i<img.length;i++) {
            xSums[i] = colSum(img,i)-lastSum;
            lastSum = xSums[i]+lastSum;
            if(xSums[i]>max)
                max=xSums[i];
            if(xSums[i]<min)
                min=xSums[i];
        }
        xs = edges(xSums,max*THRESHOLDFACTOR,min*THRESHOLDFACTOR);
        
        min=0; max=0;
        lastSum = lineSum(img, 0);
        for(i=1;i<img.length;i++) {
            ySums[i] = lineSum(img,i)-lastSum;
            lastSum = ySums[i]+lastSum;
            if(ySums[i]>max && i<img.length/2)
                max=ySums[i];
            if(ySums[i]<min && i>img.length/2)
                min=ySums[i];
        }
        ys = edges(ySums,max*THRESHOLDFACTOR,min*THRESHOLDFACTOR);
        
        ArrayList<Point> corners = matchPoints(img, xs, ys);
        for(Point point : corners) {
            point.x = (int) (point.x*floor(image.getWidth()/STEPDIVIDER));
            point.y = (int) (point.y*(floor(image.getHeight()/STEPDIVIDER)));
        }  
        return corners;
    }
    
    private static ArrayList<Point> matchPoints(int[][] img, int[]xs, int[] ys) {
        Point upL= new Point(), upR = new Point(), downL = new Point(), downR = new Point();
        ArrayList<Point> corners = new ArrayList<>();
        
        upL.x = matchX(img, xs[0],xs[1], (int) floor((ys[0]+ys[1])/2),(int) floor((ys[2]+ys[3])/2))[0];
        downL.x=matchX(img, xs[0],xs[1], (int) floor((ys[0]+ys[1])/2),(int) floor((ys[2]+ys[3])/2))[1];
        
        downR.x=matchX(img, xs[3],xs[2], (int) floor((ys[0]+ys[1])/2),(int) floor((ys[2]+ys[3])/2))[0];
        upR.x = matchX(img, xs[3],xs[2], (int) floor((ys[0]+ys[1])/2),(int) floor((ys[2]+ys[3])/2))[1];
        
        upL.y=matchY(img, ys[0],ys[1], upL.x, upR.x)[0];
        upR.y=matchY(img, ys[0],ys[1], upL.x, upR.x)[1];
        
        downR.y=matchY(img, ys[3],ys[2], downL.x, downR.x)[0];
        downL.y=matchY(img, ys[3],ys[2], downL.x, downR.x)[1];
        
        corners.add(upL);
        corners.add(downL);
        corners.add(upR);
        corners.add(downR);

        return corners;
    }
    private static int[][] grayscale(BufferedImage img, int xStep,int yStep) {
        int imgWidth =img.getWidth();
        int imgHeight=img.getHeight();
        int width = (int) floor(imgWidth / xStep);
        int height= (int) floor(imgHeight/ yStep);
        int[][] grayImg = new int[width][height];
        Color color;
        int i,j;
        
        for(i=0; i<imgWidth; i+=xStep) {
            for(j=0; j<imgHeight; j+=yStep) {
                if(i/xStep<width && j/yStep<height) {
                    color = new Color(img.getRGB(i, j));      
                    grayImg[i/xStep][j/yStep] = (int) (0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue());
                }
            }
        }
        return grayImg;
    }
    
    private static int[] edges(int[] sums, double positiveThreshold, double negativeThreshold) {
        int i, rising=0;
        int[] edges = {0,0,sums.length-1,0};
        int mass=0,maxMass=0;
        for(i=0;i<sums.length-1;i++) {
            if(sums[i]<positiveThreshold && sums[i+1]>positiveThreshold) {
                rising=i+1;
                mass=0;
            }
            if(sums[i]>positiveThreshold)
                mass+=sums[i];
            if(sums[i]>positiveThreshold && sums[i+1]<positiveThreshold) {
                if(mass>maxMass) {
                    maxMass=mass;
                    edges[0]=rising;
                    edges[1]=i;
                }
            }
        }
        
        mass=0;maxMass=0;rising=sums.length-1;
        for(i=sums.length-1;i>0;i--) {
            if(sums[i]>negativeThreshold && sums[i-1]<negativeThreshold) {
                rising=i-1;
                mass=0;
            }
            if(sums[i]<negativeThreshold)
                mass+=sums[i];
            if(sums[i]<negativeThreshold && sums[i-1]>negativeThreshold) {
                if(mass<maxMass) {
                    maxMass=mass;
                    edges[2]=rising;
                    edges[3]=i;
                }
            }
            
        }
        return edges;
    }
    
    private static int[] matchX(int[][] img, int x1, int x2,int yUp,int yDown) {
        int[] xs = {0,0};
        int yMiddle=(int) floor((yUp+yDown)/2);
        int xMiddle=(int) floor((x1+x2)/2);
        int moyUp=sum(img,xMiddle,xMiddle,yUp,yMiddle);
        int moyDown=sum(img,xMiddle,xMiddle,yMiddle,yDown);
        
        if(moyUp>moyDown) {
            xs[0]=x1;
            xs[1]=x2;
        } else {
            xs[0]=x2;
            xs[1]=x1;
        }
        return xs;
    }
    private static int[] matchY(int[][] img, int y1, int y2,int xL,int xR) {
        int[] ys = {0,0};
        int xMiddle=(int) floor((xL+xR)/2);
        int yMiddle=(int) floor((y1+y2)/2);
        int moyUp = sum(img,xL,xMiddle,yMiddle,yMiddle);
        int moyDown=sum(img,xMiddle,xR,yMiddle,yMiddle);
        
        if(moyUp>moyDown) {
            ys[0]=y1;
            ys[1]=y2;
        } else {
            ys[0]=y2;
            ys[1]=y1;
        }
        return ys;
    }
    
    private static int lineSum(int[][] img,int y) {
        int i;
        int result=0;
        for(i=0;i<img.length;i++)
            result+=img[i][y];
        return result;
    }
    private static int colSum(int[][] img,int x) {
        return sum(img,x,x,0,img[x].length-1);
    }
    private static int sum(int[][] table, int xmin, int xmax, int ymin, int ymax) {
        int i,j;
        int result=0;
        for(i=xmin; i<=xmax; i++) {
            for(j=ymin; j<=ymax; j++) {
                result+=table[i][j];
            }
        }
        return result;
    }
}