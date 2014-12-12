package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.mhealth.api.KeyValueAPI;

public class Register extends Activity implements OnClickListener {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	
	EditText mUsername;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid; 
	
	String username = "";
	String usersList = "";
	String receiver = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_register);
		
		mUsername = (EditText) findViewById(R.id.username_input);
		View registerButton = findViewById(R.id.register_button);
		registerButton.setOnClickListener(this);
		View backButton = findViewById(R.id.finalproject_back_button);
		backButton.setOnClickListener(this);
		
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_button:
			
			username = mUsername.getText().toString();
			if (!username.equals("")) {
				if (checkPlayServices()) {
					regid = getRegistrationId(context);
					if (TextUtils.isEmpty(regid)) {
						registerInBackground();
					}
					else{
						Toast.makeText(this, "You are already registered",
								Toast.LENGTH_LONG).show();
					}
				}
			} else {
				Toast.makeText(this, "Please enter a username",
						Toast.LENGTH_LONG).show();
			}
			Intent i = new Intent(this, Users.class);
			finish();
			startActivity(i);
			break;
		case R.id.finalproject_back_button:
			finish();
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
		return registrationId;
	}

	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
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
						KeyValueAPI.put("eighilaza", "eighilaza", "alertText",
								"Register Notification");
						KeyValueAPI.put("eighilaza", "eighilaza", "titleText",
								"Register");
						KeyValueAPI.put("eighilaza", "eighilaza",
								"contentText", "Registering Successful!");
						regid = gcm
								.register(CommunicationConstants.GCM_SENDER_ID);
						int cnt = 0;
						if (KeyValueAPI.isServerAvailable()) {
							if (!KeyValueAPI.get("eighilaza", "eighilaza",
									"cnt").contains("Error")) {
								Log.d("????", KeyValueAPI.get("eighilaza",
										"eighilaza", "cnt"));
								cnt = Integer.parseInt(KeyValueAPI.get(
										"eighilaza", "eighilaza", "cnt"));
							}
							String getString;
							boolean flag = false;
							for (int i = 1; i <= cnt; i++) {
								getString = KeyValueAPI.get("eighilaza",
										"eighilaza",
										"regid" + String.valueOf(i));
								Log.d(String.valueOf(i), getString);
								if (getString.equals(regid))
									flag = true;
							}
							if (!flag) {
								KeyValueAPI.put("eighilaza", "eighilaza",
										"cnt", String.valueOf(cnt + 1));
								KeyValueAPI.put("eighilaza", "eighilaza",
										"regid" + String.valueOf(cnt + 1),
										regid);
							}
							KeyValueAPI.put("eighilaza", "eighilaza", username,
									regid);
							KeyValueAPI.put("eighilaza", "eighilaza", "user"
									+ String.valueOf(cnt + 1), username);

							msg = "Registered with the username "
									+ KeyValueAPI.get("eighilaza", "eighilaza",
											"user" + String.valueOf(cnt + 1));
						} else {
							msg = "Error :" + "Backup Server is not available";
							return msg;
						}
						//sendRegistrationIdToBackend();
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

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putString("username", username);
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
}