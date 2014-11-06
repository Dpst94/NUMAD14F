package edu.neu.madcourse.deborahho.twoplayerwordgame;

import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.deborahho.R;
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

public class TwoPlayerRegister extends Activity implements OnClickListener{
	
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
	EditText mUsername;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid;
	String username = "";
	String contact = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.twoplayer_register);
		mUsername = (EditText) findViewById(R.id.twoplayer_username);
		mDisplay = (TextView) findViewById(R.id.twoplayer_display);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
		
		View nextButton = findViewById(R.id.next_button);
		nextButton.setOnClickListener(this);
	    View exitButton = findViewById(R.id.exit_button);
	    exitButton.setOnClickListener(this); 
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == findViewById(R.id.twoplayer_register_button)) {
			username = mUsername.getText().toString();
    		Log.d(TAG, "username: " + username);
    		if (!username.equals("")) {
    			if (checkPlayServices()) {
    				regid = getRegistrationId(context);
    				if (TextUtils.isEmpty(regid)) {
    					registerInBackground();
    				} else {
    					Toast.makeText(context, "You are already registered!", Toast.LENGTH_LONG).show();
    				}
    					
    			}
			} else {
				Toast.makeText(context, "Input username!", Toast.LENGTH_LONG).show();
			}

		}else if (v == findViewById(R.id.twoplayer_unregister_button)) {
				unregister();
		}
		
		switch (v.getId()) {
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.next_button:
//    		regid = getRegistrationId(context);
//    		if (!TextUtils.isEmpty(regid)) {
//    			Intent l = new Intent(this, TwoPlayerChallengeUser.class);
//        		startActivity(l); 
//    		} else {
//    			Toast.makeText(context, "Please register!", Toast.LENGTH_LONG).show();	
//    		}   
    		Intent l = new Intent(this, TwoPlayerChallengeUser.class);
    		startActivity(l); 
    		break;
		}
		
	}
	
	@SuppressLint("NewApi")
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		Log.i(TAG, String.valueOf(registeredVersion));
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(TwoPlayerWordGame.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}	

	public void registerInBackground() {
		if (!isOnline()) {
			Toast.makeText(this, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				if (KeyValueAPI.isServerAvailable()) {
					try {
						if (gcm == null) {
							gcm = GoogleCloudMessaging.getInstance(context);
						}
						KeyValueAPI.put("DeborahHo", "patricia", "alertText",
								"Register Notification");
						KeyValueAPI.put("DeborahHo", "patricia", "titleText",
								"Register");
						KeyValueAPI.put("DeborahHo", "patricia",
								"contentText", "Registering Successful!");
						regid = gcm
								.register(CommunicationConstants.GCM_SENDER_ID);
						int cnt = 0;
						if (KeyValueAPI.isServerAvailable()) {
							if (!KeyValueAPI.get("DeborahHo", "patricia",
									"cnt").contains("Error")) {
								Log.d("????", KeyValueAPI.get("DeborahHo",
										"patricia", "cnt"));
								cnt = Integer.parseInt(KeyValueAPI.get(
										"DeborahHo", "patricia", "cnt"));
								Log.d(TAG, "cnt " + Integer.toString(cnt) );
							}
							String getString;
							boolean flag = false;
							for (int i = 1; i <= cnt; i++) {
								getString = KeyValueAPI.get("DeborahHo",
										"patricia",
										"regid" + String.valueOf(i));
								Log.d(String.valueOf(i), getString);
								if (getString.equals(regid))
									flag = true;
							}
							if (!flag) {
								KeyValueAPI.put("DeborahHo", "patricia",
										"cnt", String.valueOf(cnt + 1));
								Log.d(TAG, "!flag cnt " + Integer.toString(cnt) );
								
								KeyValueAPI.put("DeborahHo", "patricia",
										"regid" + String.valueOf(cnt + 1),
										regid);
							}
							KeyValueAPI.put("DeborahHo", "patricia", username,
									regid);
							KeyValueAPI.put("DeborahHo", "patricia", "user"
									+ String.valueOf(cnt + 1), username);

							msg = "Registered with the username "
									+ KeyValueAPI.get("DeborahHo", "patricia",
											"user" + String.valueOf(cnt + 1));
						} else {
							msg = "Error :" + "Backup Server is not available";
							return msg;
						}
						sendRegistrationIdToBackend();
						storeRegistrationId(context, regid);
					} catch (IOException ex) {
						msg = "Error :" + ex.getMessage();
					}
				} else {
					msg = "Error :" + "Backup Server is not available";
					return msg;
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
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
	
	private void unregister() {
		Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
		if (!isOnline()) {
			Toast.makeText(this, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Sent unregistration";
					KeyValueAPI.put("DeborahHo", "patricia", "alertText",
							"Notification");
					KeyValueAPI.put("DeborahHo", "patricia", "titleText",
							"Unregister");
					KeyValueAPI.put("DeborahHo", "patricia", "contentText",
							"Unregistering Successful!");
					gcm.unregister();
					
					int cnt = 0;
					if (!KeyValueAPI.get("DeborahHo", "patricia", "cnt").contains(
							"Error")){
						cnt = Integer.parseInt(KeyValueAPI.get("DeborahHo",
								"patricia", "cnt"));
						Log.d(TAG, "cnt " + Integer.toString(cnt) );
						for (int i = 1; i <= cnt; i++) {
							if(username.equals(KeyValueAPI.get("DeborahHo", "patricia", "user"+ String.valueOf(i)))){
								KeyValueAPI.clearKey("DeborahHo", "patricia", "user"+ String.valueOf(i));
								KeyValueAPI.clearKey("DeborahHo", "patricia", "regid"+ String.valueOf(i));
								KeyValueAPI.clearKey("DeborahHo", "patricia", username);
								KeyValueAPI.put("DeborahHo", "patricia","cnt", String.valueOf(cnt-1));
							}
						}
					}
					
					
					KeyValueAPI.clearKey("DeborahHo", "patricia", "username");
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				 KeyValueAPI.clear("DeborahHo", "patricia");
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				removeRegistrationId(getApplicationContext());
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				((TextView) findViewById(R.id.twoplayer_display))
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


}
