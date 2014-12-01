package edu.neu.madcourse.deborahho.trickiestpart;
import edu.neu.madcourse.deborahho.R;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
 
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
  
@SuppressLint("NewApi") public class Upload extends Activity implements OnClickListener{
     
    private TextView messageText;
    private Button uploadButton, btnselectpic;
    private ImageView imageview;
    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;
        
    private String upLoadServerUri = null;
    private String imagepath=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trickiestpart_upload);
          
        uploadButton = (Button)findViewById(R.id.uploadButton);
        messageText  = (TextView)findViewById(R.id.messageText);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
         
        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        upLoadServerUri = "http://eighilaza.tk/UploadToServer.php";
    }
      
     
    @Override
    public void onClick(View arg0) {
        if(arg0==btnselectpic)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
        }
        else if (arg0==uploadButton) {
             
             dialog = ProgressDialog.show(Upload.this, "", "Uploading file...", true);
             messageText.setText("uploading started.....");
             new Thread(new Runnable() {
                 public void run() {
                                      
                      uploadFile(imagepath);
                                               
                 }
               }).start();     
        }
         
    } 
     
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getData().getPath(); 
           
            Uri selectedImageUri = data.getData();
            imagepath = getPath(selectedImageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
            imageview.setImageBitmap(bitmap);
            messageText.setText("Uploading file path:" +imagepath);
             
        }
    }
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi") public String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
         
    public int uploadFile(String sourceFileUri) {
           
           
          String fileName = sourceFileUri;
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
           
          if (!sourceFile.isFile()) {
               
               dialog.dismiss(); 
                
               Log.e("uploadFile", "Source File not exist :"+imagepath);
                
               runOnUiThread(new Runnable() {
                   public void run() {
                       messageText.setText("Source File not exist :"+ imagepath);
                   }
               }); 
                
               return 0;
            
          }
          else
          {
               try { 
                    
                     // open a URL connection to the Servlet
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   URL url = new URL(upLoadServerUri);
                    
                   // Open a HTTP  connection to  the URL
                   conn = (HttpURLConnection) url.openConnection(); 
                   conn.setDoInput(true); // Allow Inputs
                   conn.setDoOutput(true); // Allow Outputs
                   conn.setUseCaches(false); // Don't use a Cached Copy
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Connection", "Keep-Alive");
                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                   conn.setRequestProperty("uploaded_file", fileName); 
                    
                   dos = new DataOutputStream(conn.getOutputStream());
          
                   dos.writeBytes(twoHyphens + boundary + lineEnd);
                   dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                           + fileName + "\"" + lineEnd);
                    
                   dos.writeBytes(lineEnd);
          
                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available(); 
          
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
          
                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                      
                   while (bytesRead > 0) {
                        
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                      
                    }
          
                   // send multipart form data necesssary after file data...
                   dos.writeBytes(lineEnd);
                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
          
                   // Responses from the server (code and message)
                   serverResponseCode = conn.getResponseCode();
                   String serverResponseMessage = conn.getResponseMessage();
                     
                   Log.i("uploadFile", "HTTP Response is : "
                           + serverResponseMessage + ": " + serverResponseCode);
                    
                   if(serverResponseCode == 200){
                        
                       runOnUiThread(new Runnable() {
                            public void run() {
                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                      +" F:/wamp/wamp/www/uploads";
                                messageText.setText(msg);
                                Toast.makeText(Upload.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                            }
                        });                
                   }    
                    
                   //close the streams //
                   fileInputStream.close();
                   dos.flush();
                   dos.close();
                     
              } catch (MalformedURLException ex) {
                   
                  dialog.dismiss();  
                  ex.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("MalformedURLException Exception : check script url.");
                          Toast.makeText(Upload.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                      }
                  });
                   
                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
              } catch (Exception e) {
                   
                  dialog.dismiss();  
                  e.printStackTrace();
                   
                  runOnUiThread(new Runnable() {
                      public void run() {
                          messageText.setText("Got Exception : see logcat ");
                          Toast.makeText(Upload.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                      }
                  });
                  Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
              }
              dialog.dismiss();       
              return serverResponseCode; 
               
           } // End else block 
         }     
}