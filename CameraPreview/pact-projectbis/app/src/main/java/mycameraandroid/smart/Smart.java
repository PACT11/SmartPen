package mycameraandroid.smart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Smart extends Activity {
    private Camera cameraObject;
    private ShowCamera2 showCamera;
    private Bitmap bitmap;
    private ImageView welcom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart);
        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera2(this, cameraObject);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
            welcom =  (ImageView) findViewById(R.id.imageView1);
            welcom.setImageBitmap(bitmap);

            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }
            cameraObject.startPreview();
        }
    };

    public static Camera isCameraAvailiable() {
        Camera object = null;
        try {
            object = Camera.open();
            object.startPreview();
        } catch (Exception e) {
        }
        return object;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public static void savePicture(Bitmap bitmap) {
        FileOutputStream fos = null;
        Bitmap bmp = bitmap;
        Date date = new Date();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss");
        try {
            String path_file = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SmartPenImage";
            System.out.println(path_file);
            File file = new File(path_file, "Smartpen-" + timeStampFormat.format(date) + ".png");
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

    public void takePicture(View view) {
        cameraObject.takePicture(null, null, capturedIt);
    }
}
