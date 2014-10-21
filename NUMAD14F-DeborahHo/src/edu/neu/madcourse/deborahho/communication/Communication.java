package edu.neu.madcourse.deborahho.communication;

import edu.neu.madcourse.deborahho.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.mhealth.api.KeyValueAPI;

public class Communication extends Activity implements OnClickListener{
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
    static final String TAG_GLOBAL = "GCM_Globals";
    static final int SIMPLE_NOTIFICATION = 22;
	
	String GCM_API_KEY = "AIzaSyAJUK8ioj2HAOyDBKMTMJ80DDShn5UUDIQ";
	String SENDER_ID = "341143421214";
	String TEAM_NAME = "DeborahHo";
	String PASSWORD = "patricia";
	
	TextView mDisplay;
	EditText mUsername;
	EditText mMessage;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid;	
	String username;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication);
        
		mDisplay = (TextView) findViewById(R.id.communication_display);
		mUsername = (EditText) findViewById(R.id.communication_username);
		mMessage = (EditText) findViewById(R.id.communication_edit_message);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
        
        View registerButton = findViewById(R.id.communication_register_button);
        registerButton.setOnClickListener(this);
        View unregisterButton = findViewById(R.id.communication_unregister_button);
        unregisterButton.setOnClickListener(this);
        View findContactsButton = findViewById(R.id.find_contacts);
        findContactsButton.setOnClickListener(this);
        View sendDataButton = findViewById(R.id.communication_send);
        sendDataButton.setOnClickListener(this);
        View clearButton = findViewById(R.id.communication_clear);
        clearButton.setOnClickListener(this);
        View acknowledgementsButton = findViewById(R.id.com_acknowledgements_button);
        acknowledgementsButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this); 
	}	

	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		Log.i(TAG, "registrationId " + registrationId);
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

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				Log.d(TAG, "mDisplay: " + msg);
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "alertText",
							"Register Notification");
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "titleText",
							"Register");
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "contentText",
							"Registering Successful!");
					
					regid = gcm.register(SENDER_ID);
					
					int cnt = 0;
					if (KeyValueAPI.isServerAvailable()) {
						if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt")
								.contains("Error")) {
							Log.d("????", KeyValueAPI.get(TEAM_NAME, PASSWORD,
									"cnt"));
							cnt = Integer.parseInt(KeyValueAPI.get(TEAM_NAME,
									PASSWORD, "cnt"));
						}
						String getString;
						boolean flag = false;
						for (int i = 1; i <= cnt; i++) {
							getString = KeyValueAPI.get(TEAM_NAME, PASSWORD,
									"regid" + String.valueOf(i));
							Log.d(String.valueOf(i), getString);
							if (getString.equals(regid))
								flag = true;
						}
						if (!flag) {
							KeyValueAPI.put(TEAM_NAME, PASSWORD, "cnt",
									String.valueOf(cnt + 1));
							KeyValueAPI.put(TEAM_NAME, PASSWORD, "regid"
									+ String.valueOf(cnt + 1), regid);
						}
						msg = "Device registered, registration ID=" + regid + " ;Username=" + username;
					} else {
						msg = "Error :" + "Backup Server is not available";
						return msg;
					}
					sendRegistrationIdToBackend();
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				Log.d(TAG, "mDisplay: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {
		KeyValueAPI.put(TEAM_NAME, PASSWORD, "user1", username);
		KeyValueAPI.put(TEAM_NAME, PASSWORD, username, regid);
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

	private boolean checkPlayServices() {
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
	
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.communication_register_button:
    		username = ((EditText) findViewById(R.id.communication_username))
    		.getText().toString();
    		Log.d(TAG, "username: " + username);
    		if (username != "" && username != null) {
    			if (checkPlayServices()) {
    				regid = getRegistrationId(context);
    				if (TextUtils.isEmpty(regid)) {
    					registerInBackground();
    				}
    			}
			} else {
				Toast.makeText(context, "Input username!", Toast.LENGTH_LONG).show();
			}
    		break;
    	case R.id.communication_unregister_button:
    		unregister();
    		break;
    	case R.id.communication_send:
    		String message = ((EditText) findViewById(R.id.communication_edit_message))
			.getText().toString();
    		if (message != "") {
    			sendMessage(message);
    		} else {
    			Toast.makeText(context, "Sending Context Empty!",
				Toast.LENGTH_LONG).show();
    		}
    		break;
    	case R.id.communication_clear:
    		mMessage.setText("");
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.com_acknowledgements_button:
    		Intent i = new Intent(this, ComAcknowledgements.class);
    		startActivity(i);
    		break;
    	}
    }
    
    private void unregister() {
		Log.d(TAG_GLOBAL, "UNREGISTER USERID: " + regid);
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					msg = "Sent unregistration";
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "alertText",
							"Notification");
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "titleText",
							"Unregister");
					KeyValueAPI.put(TEAM_NAME, PASSWORD, "contentText",
							"Unregistering Successful!");
					gcm.unregister();
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
		Log.i(TAG_GLOBAL, "Removig regId on app version "
				+ appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PROPERTY_REG_ID);
		editor.commit();
		regid = null;
	}

	@SuppressLint("NewApi")
	private void sendMessage(final String message) {
		if (regid == null || regid.equals("")) {
			Toast.makeText(this, "You must register first", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (message.isEmpty()) {
			Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
			return;
		}

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				int cnt = 0;
				if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt").contains(
						"Error"))
					cnt = Integer.parseInt(KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt"));
				else {
					msg = KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt");
					return msg;
				}
				List<String> regIds = new ArrayList<String>();
				String reg_device = regid;
				//int nIcon = R.drawable.ic_stat_cloud;
				int nType = SIMPLE_NOTIFICATION;
				Map<String, String> msgParams;
				msgParams = new HashMap<String, String>();
				msgParams.put("data.alertText", "Notification");
				msgParams.put("data.titleText", "Notification Title");
				msgParams.put("data.contentText", message);
				//msgParams.put("data.nIcon", String.valueOf(nIcon));
				msgParams.put("data.nType", String.valueOf(nType));
				KeyValueAPI.put(TEAM_NAME, PASSWORD, "alertText",
						"Message Notification");
				KeyValueAPI.put(TEAM_NAME, PASSWORD, "titleText",
						"Sending Message");
				KeyValueAPI.put(TEAM_NAME, PASSWORD, "contentText", message);
				//KeyValueAPI.put("pbj1203", "1312789", "nIcon",
				//		String.valueOf(nIcon));
				KeyValueAPI.put(TEAM_NAME, PASSWORD, "nType",
						String.valueOf(nType));
//				GcmNotification gcmNotification = new GcmNotification();
//				for (int i = 1; i <= cnt; i++) {
//					regIds.clear();
//					reg_device = KeyValueAPI.get("pbj1203", "1312789", "regid"
//							+ String.valueOf(i));
//					Log.d(String.valueOf(i), reg_device);
//					regIds.add(reg_device);
//					gcmNotification.sendNotification(msgParams, regIds,
//							edu.neu.madcourse.deborahho.communication.Communication.this);
//					Log.d(String.valueOf(i), regIds.toString());
//				}
				msg = "sending information...";
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		}.execute(null, null, null);
	}

}
