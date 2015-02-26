
package view;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;

import android.view.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import android.widget.*;

import java.io.*;



public class MyCamera{
    private InputScreen.ImageListener imageListener;
    private Camera cameraObject;
    private ShowCamera showCamera;
    private Bitmap bitmap;

    public MyCamera() {
        // TODO : create a timer that take a picture on a regular basis and call imageListener for processing
        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(this, cameraObject);
        System.out.println("Camera : new camera created : starting taking pictures");
        Timer timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ontakingPicture();
            }
        }, 100, 500);
    }

    public Bitmap getBitmap()
    {
        return this.bitmap;
    }

    // add a listener called each time a picture is taken (EXCEPT when taken with takePicture())
    public void addNewImageListener(InputScreen.ImageListener listener) {
        imageListener = listener;
    }
    // take and return a picture
    public Bitmap takePicture() {
        cameraObject.takePicture(null, null, capturedIt);
        System.out.println("Camera : taking a picture");
        imageListener.newImage(null);
        return bitmap;
    }
    private void ontakingPicture() {
        Bitmap bmp = takePicture();
        imageListener.newImage(bmp);
    }
    // close the camera
    public void close() {
        cameraObject.
    }
    // debug
    public void simulateNewImage(Bitmap image) {
        imageListener.newImage(image);
    }

    public static Camera isCameraAvailiable(){
        Camera object = null;
        try {
            object = Camera.open();
            object.startPreview();
        }
        catch (Exception e){
        }
        return object;
    }

    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap btm = BitmapFactory.decodeByteArray(data , 0, data .length);
            bitmap = btm;
            savePicture(bitmap);
            if(bitmap==null){
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }
            cameraObject.startPreview();
        }
    };

    public void savePicture(Bitmap bitmap){
        FileOutputStream fos = null;
        Bitmap bmp;
        bmp = bitmap;
        Date date=new Date();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss");
        try {
            String path_file  = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ "SmartPenImage";
            System.out.println(path_file);
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
}

