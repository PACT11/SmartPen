package mycameraandroid.smartpen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class ManipulationImage {
    private Bitmap mergeBitmap(Bitmap bitmap1, Bitmap bitmap2){
        Bitmap mergedBitmap = null;

        int width;
        if(bitmap1.getWidth() >= bitmap2.getWidth()){
            width = bitmap1.getWidth();
        }else{
            width = bitmap2.getWidth();
        }

        int height;
        if(bitmap1.getHeight() >= bitmap2.getHeight()){
            height = bitmap1.getHeight();
        }else{
            height = bitmap2.getHeight();
        }

        Bitmap.Config conf = bitmap1.getConfig();
        if(conf== null){
             conf = Bitmap.Config.ARGB_8888;
        }

        mergedBitmap = Bitmap.createBitmap(width, height, conf);
        Canvas newCanvas = new Canvas(mergedBitmap);

        newCanvas.drawBitmap(bitmap1, 0, 0, null);
        Paint paint = new Paint();
        paint.setAlpha(128);

        newCanvas.drawBitmap(bitmap2, 0, 0, paint);
        return mergedBitmap;
    }


    public Bitmap changedPixel(Bitmap bitmap){

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        System.out.println("width = " + width);
        System.out.println("height = "+ height);
        Bitmap.Config config = bitmap.getConfig();
        boolean isMutable = true;
        Bitmap btm = bitmap.copy(config, isMutable);
        Color color = new Color();
        int colour;
        colour= color.GREEN;
        for( int i=150; i<987 ;i++){
            for (int j=50; j<753; j++){
                btm.setPixel( i, j, colour);
            }
        }

        return btm;
    }
}
