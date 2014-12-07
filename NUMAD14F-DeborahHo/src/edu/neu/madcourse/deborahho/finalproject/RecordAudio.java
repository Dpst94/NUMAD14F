package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.trickiestpart.Uploader;

import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecordAudio extends Activity{

   private MediaRecorder myRecorder;
   private MediaPlayer myPlayer;
   private String outputFile = null;
   private Button recordBtn;
   private Button listenBtn;
   private Button sendBtn;
   private Button backBtn;
   private TextView receiverAudio;
   
   SharedPreferences name;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.finalproject_record_audio);
      
      receiverAudio = (TextView) findViewById(R.id.receiver_audio_snippet);      
      
       name = getSharedPreferences("audio_receiver", 0);     
      
      receiverAudio.setText("Send Audio To "+name.getString("receiver", "UNKNOWN"));
      // store it to sd card
      outputFile = Environment.getExternalStorageDirectory().
    		  getAbsolutePath() + "/javacodegeeksRecording.3gpp";

      
      
      backBtn = (Button) findViewById(R.id.finalproject_back_button);
      backBtn.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();	
		}
    	  
      });
      
      recordBtn = (Button)findViewById(R.id.start);
      recordBtn.setOnTouchListener(new View.OnTouchListener() {

          @Override
          public boolean onTouch(View v, MotionEvent event) {
              switch(event.getAction()){
               case MotionEvent.ACTION_DOWN:
                   Log.d("record", "Start Recording");
                   start(v);
                   break;
               case MotionEvent.ACTION_UP:
                   Log.d("record", "stop Recording");
                   stop(v);
                   break;
              }
              return false;
          }

      });
      
      listenBtn = (Button)findViewById(R.id.listen_recording);
      
      listenBtn.setOnTouchListener(new View.OnTouchListener() {

          @Override
          public boolean onTouch(View v, MotionEvent event) {
              // TODO Auto-generated method stub
              switch(event.getAction()){
               case MotionEvent.ACTION_DOWN:
                   Log.d("listen", "play Recording");
                   play(v);
                   break;
               case MotionEvent.ACTION_UP:
                   Log.d("listen", "stop Recording");
                   stopPlay(v);
                   break;
              }
              return false;
          }

      });
      
      sendBtn = (Button) findViewById(R.id.send_recording);
      sendBtn.setOnClickListener(new OnClickListener() {
    	  
    	@Override
		public void onClick(View v) {
    		new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
				      Uploader.uploadFile(outputFile);
				      return "uploaded";
				}
				@Override
				protected void onPostExecute(String msg) {
				}
			}.execute(null, null, null);	
			
			Toast.makeText(getApplicationContext(), "Send recording...", 
		    		   Toast.LENGTH_SHORT).show();
		}
    	  
      });
      
   }

   public void start(View view){
	   try {
		   myRecorder = new MediaRecorder();
		      myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		      myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		      myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		      myRecorder.setOutputFile(outputFile);
          myRecorder.prepare();
          myRecorder.start();
       } catch (IllegalStateException e) {
          // start:it is called before prepare()
    	  // prepare: it is called after start() or before setOutputFormat() 
          e.printStackTrace();
       } catch (IOException e) {
           // prepare() fails
           e.printStackTrace();
        }
       
       Toast.makeText(getApplicationContext(), "Start recording...", 
    		   Toast.LENGTH_SHORT).show();
   }

   public void stop(View view){
	   try {
	      myRecorder.stop();
	      myRecorder.release();
	      myRecorder  = null;
	      
	      listenBtn.setEnabled(true);
	      sendBtn.setEnabled(true);
	      
	      Toast.makeText(getApplicationContext(), "Stop recording...",
	    		  Toast.LENGTH_SHORT).show();
	   } catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
	   } catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
	   }
   }
  
   public void play(View view) {
	   try{
		   myPlayer = new MediaPlayer();
		   myPlayer.setDataSource(outputFile);
		   myPlayer.prepare();
		   myPlayer.start();		   
		   Toast.makeText(getApplicationContext(), "Start playing the recording...", 
				   Toast.LENGTH_SHORT).show();
	   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
   public void stopPlay(View view) {
	   try {
	       if (myPlayer != null) {
	    	   myPlayer.stop();
	           myPlayer.release();
	           myPlayer = null;
	           listenBtn.setEnabled(true);
	           
	           Toast.makeText(getApplicationContext(), "Stop playing the recording...", 
					   Toast.LENGTH_SHORT).show();
	       }
	   } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }

}
