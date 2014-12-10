package edu.neu.madcourse.deborahho.finalproject;

import java.util.Calendar;

import edu.neu.madcourse.deborahho.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.neu.mhealth.api.KeyValueAPI;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	static final String TAG = "GCM_Communication";

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String alertText = "";
		String titleText = "";
		String contentText = "";

		Bundle extras = intent.getExtras();
		Log.d(String.valueOf(extras.size()), extras.toString());
		if (!extras.isEmpty()) {
			alertText = KeyValueAPI.get("eighilaza", "eighilaza", "alertText");
			titleText = KeyValueAPI.get("eighilaza", "eighilaza", "titleText");
			contentText = KeyValueAPI.get("eighilaza", "eighilaza", "contentText");
			
			SharedPreferences challengersName = getSharedPreferences(
					"challenger", 0);
			SharedPreferences.Editor editor = challengersName.edit();
			editor.putString("challenger", contentText);
			editor.commit();
			
			sendNotification(alertText, titleText, contentText);
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public void sendNotification(String alertText, String titleText,
			String contentText) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent;
		notificationIntent = new Intent(this,
				ChallengeInvitation.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.putExtra("show_response", "show_response");
		PendingIntent intent;
		Log.d("TITLETEXT", titleText);
		if(titleText.equals("Received Invitation From")){
			intent = PendingIntent.getActivity(this, 0, new Intent(
					this, ChallengeInvitation.class),
					PendingIntent.FLAG_UPDATE_CURRENT);
		}
		else{
			intent = PendingIntent.getActivity(this, 0, new Intent(
					this, CrunchyMenu.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
					PendingIntent.FLAG_UPDATE_CURRENT);
		}		
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_stat_cloud)
				.setContentTitle(titleText)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(contentText))
				.setContentText(contentText).setTicker(alertText)
				.setAutoCancel(true);
		mBuilder.setContentIntent(intent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}