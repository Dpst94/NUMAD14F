package edu.neu.madcourse.deborahho.twoplayerwordgame;

import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.communication.Communication;
import edu.neu.madcourse.deborahho.communication.CommunicationConstants;
import edu.neu.mhealth.api.KeyValueAPI;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TwoPlayerUnregister extends Activity implements OnClickListener{
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";
	
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "TwoPlayerWordGame";

	TextView mDisplay;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid;
	String username = "";
	String contact = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.twoplayer_unregister);
		mDisplay = (TextView) findViewById(R.id.communication_display);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
		
		
		
		View challengeUserButton = findViewById(R.id.challenge_user_button);
		challengeUserButton.setOnClickListener(this);
	    View exitButton = findViewById(R.id.exit_button);
	    exitButton.setOnClickListener(this); 
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == findViewById(R.id.twoplayer_unregister_button)) {
			unregister();
		}
		
		switch (v.getId()) {
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.challenge_user_button:
//    		Intent l = new Intent(this, TwoPlayerUnregister.class);
//    		startActivity(l);    
    		break;
		}
		
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Communication.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	private void unregister() {
		Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
		if (!isOnline()) {
			Toast.makeText(this, "Phone is not connected to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Sent unregistration";
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "alertText",
							"Notification");
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "titleText",
							"Unregister");
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "contentText",
							"Unregistering Successful!");
					KeyValueAPI.clear(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD);
					int cnt = 0;
					if (!KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "cnt").contains(
							"Error")){
						cnt = Integer.parseInt(KeyValueAPI.get(CommunicationConstants.TEAM_NAME,
								CommunicationConstants.PASSWORD, "cnt"));
						for (int i = 1; i <= cnt; i++) {
							if(username.equals(KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "user"+ String.valueOf(i)))){
								KeyValueAPI.clearKey(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "user"+ String.valueOf(i));
								KeyValueAPI.clearKey(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "regid"+ String.valueOf(i));
								KeyValueAPI.clearKey(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, username);
								KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD,"cnt", String.valueOf(cnt-1));
							}
						}
					}
					
					gcm.unregister();
					KeyValueAPI.clearKey(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "username");
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				removeRegistrationId(getApplicationContext());
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				((TextView) findViewById(R.id.communication_display))
						.setText(regid);
			}
		}.execute();
	}
	
	private void removeRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(CommunicationConstants.TAG, "Removig regId on app version "
				+ appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PROPERTY_REG_ID);
		editor.commit();
		regid = null;
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	


}
