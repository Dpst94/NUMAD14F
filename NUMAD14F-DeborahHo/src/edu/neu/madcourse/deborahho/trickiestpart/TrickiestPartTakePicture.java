package edu.neu.madcourse.deborahho.trickiestpart;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import edu.neu.madcourse.deborahho.R;

public class TrickiestPartTakePicture extends Activity {

   private Camera cameraObject;
   private ShowCamera showCamera;
   private ImageView pic;
   public static Camera isCameraAvailiable(){
      Camera object = null;
      try {
         object = Camera.open(); 
      }
      catch (Exception e){
      }
      return object; 
   }

   private PictureCallback capturedIt = new PictureCallback() {

      @Override
      public void onPictureTaken(byte[] data, Camera camera) {

      Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
      if(bitmap==null){
         Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
      }
      else
      {
         Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();    	
      }
      cameraObject.release();
   }
};

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.trickiestpart_take_picture);
      pic = (ImageView)findViewById(R.id.imageView1);
      cameraObject = isCameraAvailiable();
      showCamera = new ShowCamera(this, cameraObject);
      FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
      preview.addView(showCamera);
   }
   public void snapIt(View view){
      cameraObject.takePicture(null, null, capturedIt);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      //getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
}