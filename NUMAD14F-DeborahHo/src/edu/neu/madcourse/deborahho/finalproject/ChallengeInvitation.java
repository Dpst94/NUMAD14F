package edu.neu.madcourse.deborahho.finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.deborahho.R;
import edu.neu.mhealth.api.KeyValueAPI;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ChallengeInvitation extends Activity implements OnClickListener {
	
	static final String TAG = "GCM_Communication";
	
	TextView tx;
	String challenger = "";
	String username;

	String regid; 
	GoogleCloudMessaging gcm;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_challenge_invitation);
		
		context = getApplicationContext();

		SharedPreferences challengersName = getSharedPreferences("challenger",
				0);
		challenger = challengersName.getString("challenger", "UNKNOWN");
		
		SharedPreferences usersName = getGCMPreferences(context);
		username = usersName.getString("username", "UNKNOWN");		
		
		tx = (TextView) findViewById(R.id.challenge_invitation_text);
		tx.setText("Would you like to accept the invitation from " + challenger + "?");

		View yesButton = findViewById(R.id.yes_button);
		yesButton.setOnClickListener(this);
		View noButton = findViewById(R.id.no_button);
		noButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			acceptInvitation();
			finish();
			break;
		case R.id.no_button:
			finish();
			break;
		}
	}

	public void acceptInvitation() {
		if (!isOnline()) {
			Toast.makeText(context, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
						int cnt = 0;
						if (KeyValueAPI.isServerAvailable()) {
							if (!KeyValueAPI.get("eighilaza", "eighilaza",
									"friends_" + username + "_cnt").contains(
									"Error")) {
								Log.d("????", KeyValueAPI.get("eighilaza",
										"eighilaza", "cnt"));
								cnt = Integer.parseInt(KeyValueAPI.get(
										"eighilaza", "eighilaza", "friends_"
												+ username + "_cnt"));
							}
							int newCnt = cnt + 1;
							KeyValueAPI.put("eighilaza", "eighilaza",
									"friends_" + username + "_" + newCnt,
									challenger);
							KeyValueAPI.put("eighilaza", "eighilaza",
									"friends_" + username + "_cnt", ""+newCnt);
						} else {
							msg = "Error :" + "Backup Server is not available";
							return msg;
						}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				sendMessage(username+ " accepted you friend request!");
			}
		}.execute(null, null, null);
	}

	// Checks if connected to the Internet
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	
	@SuppressLint("NewApi")
	private void sendMessage(final String message) {
		regid = getRegistrationId(context);
		if (!isOnline()) {
			Toast.makeText(context, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				List<String> regIds = new ArrayList<String>();
				String reg_device = regid;
				int nIcon = R.drawable.ic_stat_cloud;
				int nType = CommunicationConstants.SIMPLE_NOTIFICATION;
				Map<String, String> msgParams;
				msgParams = new HashMap<String, String>();
				msgParams.put("data.alertText", "Notification");
				msgParams.put("data.titleText", "Notification Title");
				msgParams.put("data.contentText", message);
				msgParams.put("data.nIcon", String.valueOf(nIcon));
				msgParams.put("data.nType", String.valueOf(nType));
				KeyValueAPI.put("eighilaza", "eighilaza", "alertText",
						"Message Notification");
				KeyValueAPI.put("eighilaza", "eighilaza", "titleText",
						"Invitation Accepted");
				KeyValueAPI.put("eighilaza", "eighilaza", "contentText",
						message);
				KeyValueAPI.put("eighilaza", "eighilaza", "nIcon",
						String.valueOf(nIcon));
				KeyValueAPI.put("eighilaza", "eighilaza", "nType",
						String.valueOf(nType));
				GcmNotification gcmNotification = new GcmNotification();
				regIds.clear();
				reg_device = KeyValueAPI.get("eighilaza", "eighilaza", challenger);
				regIds.add(reg_device);
				gcmNotification
						.sendNotification(
								msgParams,
								regIds,context);
				msg = "replying to invitation...";
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
	}
	
	@SuppressLint("NewApi")
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(CommunicationConstants.PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		return registrationId;
	}
}
