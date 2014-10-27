package edu.neu.madcourse.deborahho.communication;

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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.deborahho.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class Communication extends Activity implements OnClickListener {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	
	TextView mDisplay;
	EditText mMessage;
	EditText mUsername;
	EditText mContact;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid;
	String username = "";
	String contact = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.communication);
		mDisplay = (TextView) findViewById(R.id.communication_display);
		mUsername = (EditText) findViewById(R.id.communication_username);
		mMessage = (EditText) findViewById(R.id.communication_edit_message);
		mContact = (EditText) findViewById(R.id.communication_contact);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
		
		View findContactsButton = findViewById(R.id.find_contacts);
        findContactsButton.setOnClickListener(this);
	    View acknowledgementsButton = findViewById(R.id.com_acknowledgements_button);
	    acknowledgementsButton.setOnClickListener(this);
	    View exitButton = findViewById(R.id.exit_button);
	    exitButton.setOnClickListener(this); 
		
	}

	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
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
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "alertText",
							"Register Notification");
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "titleText",
							"Register");
					KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "contentText",
							"Registering Successful!");
					regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);
					int cnt = 0;
					if (KeyValueAPI.isServerAvailable()) {
						if (!KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "cnt")
								.contains("Error")) {
							Log.d("????", KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD,
									"cnt"));
							cnt = Integer.parseInt(KeyValueAPI.get(CommunicationConstants.TEAM_NAME,
									CommunicationConstants.PASSWORD, "cnt"));
						}
						String getString;
						boolean flag = false;
						for (int i = 1; i <= cnt; i++) {
							getString = KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD,
									"regid" + String.valueOf(i));
							Log.d(String.valueOf(i), getString);
							if (getString.equals(regid))
								flag = true;
						}
						if (!flag) {
							KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "cnt",
									String.valueOf(cnt + 1));
							KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "regid"
									+ String.valueOf(cnt + 1), regid);
						}
						KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "user" + String.valueOf(cnt+1), username);
						KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, username, regid);
						
						msg = "Device registered, username = " + username;
					} else {
						msg = "Error :" + "Backup Server is not available";
						return msg;
					}
					sendRegistrationIdToBackend();
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				Log.d(TAG, "regid: " + regid);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
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
	public void onClick(final View view) {
		if (view == findViewById(R.id.communication_send)) {
			String message = ((EditText) findViewById(R.id.communication_edit_message))
					.getText().toString();
			if (message != "") {
				sendMessage(message);
			} else {
				Toast.makeText(context, "Sending Context Empty!",
						Toast.LENGTH_LONG).show();
			}
		} else if (view == findViewById(R.id.communication_clear)) {
			mMessage.setText("");
			mContact.setText("");
		} else if (view == findViewById(R.id.communication_unregister_button)) {
			unregister();
		} else if (view == findViewById(R.id.communication_register_button)) {
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
		}
//		} else if (view == findViewById(R.id.com_acknowledgements_button)) {
//			Intent i = new Intent(this, ComAcknowledgements.class);
//			startActivity(i);
//		} else if (view == findViewById(R.id.find_contacts)) {
//			Intent j = new Intent(this, ComFindContacts.class);
//			startActivity(j);
//		} else if (view == findViewById (R.id.exit_button)) {
//			finish();
//		}
		
		switch (view.getId()) {
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.com_acknowledgements_button:
    		Intent i = new Intent(this, ComAcknowledgements.class);
    		startActivity(i);
    		break;
    	case R.id.find_contacts:
    		if (!isOnline()) {
    			Toast.makeText(this, "Failed to connect to the Internet",
    					Toast.LENGTH_LONG).show();
    			return;
    		} else {
    			Intent j = new Intent(this, ComFindContacts.class);
    			startActivity(j);
    			break;	
    		}
		
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

	@SuppressLint("NewApi")
	private void sendMessage(final String message) {
		if (regid == null || regid.equals("")) {
			Toast.makeText(this, "You must register first", Toast.LENGTH_LONG)
					.show();
			Log.d(TAG, "Not registered");
			return;
		}
		if (message.isEmpty()) {
			Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Empty message");
			return;
		}
		contact = mContact.getText().toString();
		if (contact.isEmpty()) {
			Toast.makeText(this, "Input Contact User", Toast.LENGTH_LONG).show();
			Log.d(TAG, "No contact selected");
			return;
		}
		if (!isOnline()) {
			Toast.makeText(this, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			Log.d(TAG, "No internet");
			return;
		}

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				int cnt = 0;
				if (!KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "cnt").contains(
						"Error")) {
					cnt = Integer.parseInt(KeyValueAPI.get(CommunicationConstants.TEAM_NAME,
							CommunicationConstants.PASSWORD, "cnt"));
					Log.d(TAG, "Get count: " + cnt);
				}
				else {
					msg = KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "cnt");
					Log.d(TAG, "message: " + msg);
					return msg;
				}
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
				KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "alertText",
						"Message Notification");
				KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "titleText",
						"Sending Message");
				KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "contentText", message);
				KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "nIcon",
						String.valueOf(nIcon));
				KeyValueAPI.put(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, "nType",
						String.valueOf(nType));
				GcmNotification gcmNotification = new GcmNotification();
				
				regIds.clear();
				//reg_device = KeyValueAPI.get(CommunicationConstants.TEAM_NAME, CommunicationConstants.PASSWORD, contact);
				Log.d(TAG, "Send to regid: " + reg_device);
				regIds.add(reg_device);
				
				gcmNotification
						.sendNotification(
								msgParams,
								regIds,
								edu.neu.madcourse.deborahho.communication.Communication.this);
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
