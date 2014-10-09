package edu.neu.madcourse.deborahho.bananagrams;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
   private static MediaPlayer mp = null;
   private static MediaPlayer mpOnce = null;

   /** Stop old song and start new one */
   
   public static void play(Context context, int resource) {
	 stop(context);
         mp = MediaPlayer.create(context, resource);
         mp.setLooping(true);
         mp.start();    
   }
   
   public static void playOnce(Context context, int resource) {
	   stop(context);
       mpOnce = MediaPlayer.create(context, resource);
       mpOnce.setLooping(false);
       mpOnce.start();
	   
   }
   
   /** Stop the music */
   public static void stop(Context context) { 
      if (mp != null) {
         mp.stop();
         mp.release();
         mp = null;
      }
   }
}