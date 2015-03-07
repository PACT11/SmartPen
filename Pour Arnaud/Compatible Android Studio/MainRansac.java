package hu.recalage;

/**
 * Created by hu on 05/03/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainRansac {

    //algorithme pour determiner les bords de la feuille
    public ArrayList<Point> obtentionCoins(Bitmap imageGrise) {


        //on initialise les 4 bords
        Line verticale1, verticale2, horizontale1, horizontale2;

        FittingInterface fitting = new SimpleFitting();

        ArrayList<Point> data1 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),0,500);
        Ransac ransac1 = new Ransac(fitting,data1,1);
        horizontale1 = appliquerRansac(ransac1,imageGrise);

        ArrayList<Point> data2 = obtenirPointsHorizontaux(imageGrise,0,imageGrise.getWidth(),1200,1600);
        Ransac ransac2 = new Ransac(fitting,data2,1);
        horizontale2 = appliquerRansac(ransac2,imageGrise);


        ArrayList<Point> data3 = obtenirPointsVerticaux(imageGrise,0,600,0,imageGrise.getHeight());
        Ransac ransac3 = new Ransac(fitting,data3,1);
        verticale1 = appliquerRansac(ransac3,imageGrise);

        ArrayList<Point> data4 = obtenirPointsVerticaux(imageGrise,2000,2500,0,imageGrise.getHeight());
        Ransac ransac4 = new Ransac(fitting,data4,1);
        verticale2 = appliquerRansac(ransac4,imageGrise);


        ArrayList<Point> Coins = new ArrayList<Point>(4);
        Point pointHG = getIntersectionPoint(verticale1,horizontale1,imageGrise);
        Point pointBG = getIntersectionPoint(verticale1,horizontale2,imageGrise);
        Point pointHD = getIntersectionPoint(verticale2,horizontale1,imageGrise);
        Point pointBD = getIntersectionPoint(verticale2,horizontale2,imageGrise);

        Coins.add(pointHG);
        Coins.add(pointBG);
        Coins.add(pointHD);
        Coins.add(pointBD);

        System.out.println("coord x = " + pointHG.x + "coord y = " + pointHG.y);
        System.out.println("coord x = " + pointBG.x + "coord y = " + pointBG.y);
        System.out.println("coord x = " + pointHD.x + "coord y = " + pointHD.y);
        System.out.println("coord x = " + pointBD.x + "coord y = " + pointBD.y);



        return Coins;

    }

    //obtention du tableau de points necessaire a l'algorithme de Ransac a l'aide du seuillage gradient
    private static ArrayList<Point> obtenirPointsHorizontaux(Bitmap imageGrise, int xmin, int xmax, int ymin, int ymax) {

        int seuilVertical = 15;

        int grisHaut, grisBas;

        ArrayList<Point> data = new ArrayList<Point>();

        for(int i=xmin; i<xmax-1; i++) {
            for(int j=ymin; j<ymax-1; j++) {

                int colorOriginal1 = imageGrise.getPixel(i,j) ;
                grisHaut = Color.red(colorOriginal1) ;
                int colorOriginal2 = imageGrise.getPixel(i,j+1) ;
                grisBas = Color.red(colorOriginal2) ;


                if (Math.abs(grisHaut-grisBas) > seuilVertical) {
                    data.add(new Point(i,j));

                }

            }
        }

        return data;

    }

    //obtention du tableau de points necessaire a l'algorithme de Ransac a l'aide du seuillage gradient
    private static ArrayList<Point> obtenirPointsVerticaux(Bitmap imageGrise, int xmin, int xmax, int ymin, int ymax) {

        int seuilHorizontal = 10;

        int grisGauche, grisDroite;

        ArrayList<Point> data = new ArrayList<Point>();

        for(int i=xmin; i<xmax-1; i++) {
            for(int j=ymin; j<ymax-1; j++) {

                int colorOriginal1 = imageGrise.getPixel(i+1,j) ;
                grisGauche = Color.red(colorOriginal1) ;
                int colorOriginal2 = imageGrise.getPixel(i,j) ;
                grisDroite = Color.red(colorOriginal2) ;


                if (Math.abs(grisGauche-grisDroite) > seuilHorizontal) {
                    data.add(new Point(i,j));

                }

            }
        }

        return data;

    }

    public static Line appliquerRansac(Ransac ransac,Bitmap imageGrise) {
        //On execute l'algorithme de Ransac
        while (!ransac.isFinished()) {
            ransac.computeNextStep();
        }

        Line line = ransac.getBestModel();

        Line lines;

        if (line.isHorizontal())
        {
            double y = line.getY(0);
            Point p1 = new Point(0,(int)(y));
            Point p2 = new Point(imageGrise.getWidth(),(int)(y));

            lines = new Line(p1,p2);
            System.out.println("Droite horizontale, y =" + y );
        }
        else if (line.isVertical())
        {
            double x = line.getX(0);
            System.out.println("Droite verticale, x =" + x);
            Point p1 = new Point((int)(x),0);
            Point p2 = new Point((int)(x),imageGrise.getHeight());
            lines = new Line(p1,p2);
        }
        else
        {
            int x1 = (int)(line.getX(0));
            int y1 = (int)(line.getY(x1));
            int x2 = (int)(line.getX(imageGrise.getHeight()));
            int y2 = (int)(line.getY(x2));
            Point p1 = new Point(x1,y1);
            Point p2 = new Point(x2,y2);

            lines = new Line(p1,p2);
            System.out.println("Droite oblique : " + x1 + " " + y1 + " " + x2 + " " + y2);
        }



        return lines;
    }

    //donne le point d'intersection entre deux droites
    public static Point getIntersectionPoint(Line line1, Line line2,Bitmap imageGrise) {
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

    /* return true if the line1 between (x1,y1) and (x2,y2) intersects the line2 between
       (x3,y3) and (x4,y4)
     */
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





}
