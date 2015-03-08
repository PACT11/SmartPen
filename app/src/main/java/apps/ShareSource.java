package apps;

import android.graphics.Bitmap;

/**
 * Created by arnaud on 04/03/15.
 */
public class ShareSource extends Application {
    @Override
    protected void onLaunch() {
        inputScreen.restart();
    }
    protected void onNewImage(Bitmap image) {

    }

    @Override
    protected void onClose() {

    }
}
