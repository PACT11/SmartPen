package mycameraandroid.smartpen;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class ShowCamera2 extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holdMe;
    private Camera theCamera;

    public ShowCamera2(Context context,Camera camera) {
        super(context);
        theCamera = camera;
        holdMe = getHolder();
        holdMe.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try   {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (IOException e) {
            //System.out.println(e.setStackTrace());
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Camera.Parameters parameters = theCamera.getParameters();
        // parameters.setPreviewSize(width, height);
        //  theCamera.setParameters(parameters);
        //  theCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        theCamera.stopPreview();
        theCamera = null;
    }
}
