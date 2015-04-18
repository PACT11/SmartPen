
package view;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import apps.Application;
import pact.smartpen.projection;

public class MyCamera{
    public static final int updatePeriod = 1000;     // the picture update period in ms

    private InputScreen.ImageListener imageListener; // a listener called when a picture is taken by the timer
    private Timer pictureUpdater;                    // a timer taking picture periodically
    private Camera camera;
    private Bitmap bitmap;                           // the captured image
    private Object synchronizer = new Object();      // used to wait for the picture to be captured (with .wait & .notify)
    private SurfaceTexture texture;                  // the surface needed for the camera to preview (not displayed)
    private  PictureCallback onCapture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            onPictureCaptured(data);
        }
    };
    private Camera.ShutterCallback onShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            Application.outputScreen.restore();
        }
    };
    private boolean opened;
    private projection activity;

    public MyCamera() {
        try {
            camera = Camera.open();
            texture = new SurfaceTexture(23);
            camera.setPreviewTexture(texture);
            camera.startPreview();

            opened = true;
        }
        catch (Exception e) {
            System.err.println("Camera : error while loading camera");
            e.printStackTrace();
        }
        // create the timer
        pictureUpdater = new Timer(false);
        pictureUpdater.schedule(new TimerTask() {
            @Override
            public void run() {
                ontakingPicture();
            }
        }, 100, updatePeriod);

        System.out.println("Camera : new camera created : starting taking pictures");
    }

    // add a listener called each time a picture is taken (EXCEPT when taken with takePicture())
    public void addNewImageListener(InputScreen.ImageListener listener) {
        imageListener = listener;
    }
    // take and return a picture
    public Bitmap takePicture() {
        if(!opened)
            return null;
        //AudioManager manager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        //manager.setStreamMute(AudioManager.STREAM_SYSTEM,true);

        camera.takePicture(onShutter, null, onCapture);
        System.out.print("Camera : taking a picture ... ");
        bitmap = null;
        // waiting for the pictured to be captured
        synchronized (synchronizer) {
            try {
                synchronizer.wait(updatePeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(bitmap!=null) {
            System.out.println("done");
        } else {
            System.out.println("fail");
        }

        return bitmap;
    }
    // called when the picture has been captured
    void onPictureCaptured(byte[] data) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        camera.startPreview();
        synchronized (synchronizer) {
            synchronizer.notify();
        }
        AudioManager manager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamMute(AudioManager.STREAM_SYSTEM,false);
    }
    // called periodically by a timer to take a picture of the sheet
    private void ontakingPicture() {
        Bitmap picture;
        if(imageListener!=null && (picture=takePicture())!=null)
            imageListener.newImage(picture);
    }
    // close the camera
    public void close() {
        if(opened) {
            synchronized (synchronizer) {
                synchronizer.notify();
            }
            opened = false;
            pictureUpdater.cancel();
            camera.release();
        }
    }

    public static void savePicture(Bitmap bitmap){
        FileOutputStream fos = null;
        Bitmap bmp;
        bmp = bitmap;
        Date date=new Date();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
        try {
            String path_file  = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ "SmartPenImage";
            //System.out.println(path_file);
            File file = new File(path_file, "Smartpen-"+timeStampFormat.format(date)+".png");
            File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "SmartPenImage"); //pour créer le repertoire dans lequel on va mettre notre image
            if (!myDir.exists()) {
                myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
            }
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setActivity(projection activity) {
        this.activity = activity;
    }
}