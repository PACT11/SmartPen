package composantes_connexes ;
import java.awt.image.BufferedImage ;

/** ˆ importer sur Android Studio : import android.graphics.Bitmap
									import android.graphics.BitmapFactory */

public class ConversionBitmap {

	public Bitmap Conversion(BufferedImage image)
	{
            Bitmap bMap = BitmapFactory.decodeStream(image) ;
            return bMap ;
    }
	
}
