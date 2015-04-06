package shape;
/**
 * Created by hu on 06/03/15.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

import apps.Application;
import view.*;

import static java.lang.Math.floor;

public class ShapeProcessor {
    private static int maxIndex;

    public static long[] heat(Bitmap image,int xmin,int xmax, int ymin, int ymax, int step) {
        long red=0, green=0, blue=0;
        int color;

        for(int i=xmin; i<=xmax; i+=step) {
            for(int j=ymin; j<=ymax; j+=step) {
                color = image.getPixel(i,j);
                red+=Color.red(color);
                green+=Color.green(color);
                blue+=Color.blue(color);
            }
        }

        long[] result={red-blue,(red+green+blue)/3};
        return result;
    }
    public static boolean zones(Bitmap image,int xZones,int yZones, double threshold) {
        int zoneWidth= (int) floor((float) (image.getWidth())/xZones);
        int zoneHeight= (int) floor((float) (image.getHeight()/yZones));
        int maxI=image.getWidth()-zoneWidth-1;
        int maxJ=image.getHeight()-zoneHeight-1;
        long sum=0,norm=0,max=0;
        long[] result;
        int i,j,k=0;

        System.out.println("Searching for hand ...");

        for(i=0;i<maxI;i+=zoneWidth) {
            for(j=0;j<maxJ;j+=zoneHeight) {
                k++;
                result=heat(image,i,i+zoneWidth,j,j+zoneHeight,8);
                norm+=result[1];
                sum+=result[0];
                if(result[0]>max) {
                    max = result[0];
                }
            }
        }

        if(norm==0)
            return false;
        System.out.println(max);
        System.out.println(norm);
        System.out.println(sum);
        System.out.println(" done. result = " + (max-(sum/k))*image.getHeight()*image.getWidth()/norm);
        return (max-(sum/k))*image.getHeight()*image.getWidth()/norm > threshold;
    }
    public static boolean hasHand(Bitmap image) {
        return zones(image, 16, 8, 3800);
    }
    public static void findFinger(Bitmap image,ArrayList<Point> corners) {
        if(corners==null)
            return;
        Point[] menuCorners = {corners.get(2),corners.get(3),
             getPointInLine(corners.get(2),corners.get(0),5),
             getPointInLine(corners.get(3),corners.get(1),5)};

        if(menuZones(image,50,menuCorners,2000)) {
            System.out.println("findFinger : found at " + maxIndex);
            Application.outputScreen.getMenu().click(100-maxIndex*2);
        } else {
            System.out.println("findFinger : not found");
            Application.outputScreen.getMenu().click(-1);
        }
    }
    private static long[] menuHeat(Bitmap image, Point[] corners,int xStep, int yStep) {
        long red=0, green=0, blue=0;
        int color;
        Point startPoint, endPoint, currentPoint;

        for(int i=0; i<100; i+=xStep) {
            startPoint= getPointInLine(corners[0],corners[1],i);
            endPoint= getPointInLine(corners[2],corners[3],i);
            for(int j=0; j<100; j+=yStep) {
                currentPoint=getPointInLine(startPoint,endPoint,j);
                color = image.getPixel((int)currentPoint.x,(int)currentPoint.y);
                red+=Color.red(color);
                green+=Color.green(color);
                blue+=Color.blue(color);
            }
        }

        long[] result={red-blue,(red+green+blue)/3};
        return result;
    }
    private static boolean menuZones(Bitmap image,int xZones,Point[] corners, double threshold) {
        int zoneWidth= (int) floor(100.0/xZones);
        long sum=0,norm=0,max=0;
        long[] result;
        int i,k=0;
        Point[] zoneCorners = {null, null,corners[1],corners[3]};
        System.out.println("Searching for finger in menu ...");

        for(i=1;i<=100;i+=zoneWidth) {
                zoneCorners[0]=zoneCorners[2];
                zoneCorners[1]=zoneCorners[3];
                zoneCorners[2]=getPointInLine(corners[1],corners[0],i);
                zoneCorners[3]=getPointInLine(corners[3],corners[2],i);

                result=menuHeat(image,zoneCorners,10,10);
                norm+=result[1];
                sum+=result[0];
                k++;
                if(result[0]>max) {
                    max = result[0];
                    maxIndex = k;
                }
        }

        if(norm==0)
            return false;
        System.out.println(max);
        System.out.println(norm);
        System.out.println(sum);

        System.out.println(" done. result = " + (max-(sum/k))*10000*k/norm);
        return (max-(sum/k))*10000*k/norm > threshold;
    }
    private static Point getPointInLine(Point startPoint, Point endPoint, float percent) {
        return new Point(startPoint.x+((endPoint.x-startPoint.x)*percent)/100,startPoint.y+((endPoint.y-startPoint.y)*percent)/100);
    }
}