package edu.neu.madcourse.deborahho.twoplayerwordgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.communication.CommunicationConstants;
import edu.neu.madcourse.deborahho.communication.GcmNotification;
import edu.neu.madcourse.deborahho.communication.Communication;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.mhealth.api.KeyValueAPI;
import edu.neu.madcourse.deborahho.communication.*;

public class TwoPlayerChallengeUser extends Activity {
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	
	String [] userList = {""};
	ArrayAdapter<String> adapter;
	ArrayList<String> list = new ArrayList<String>();
	Context context;
	String regid; 
	GoogleCloudMessaging gcm;
	String receiver = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twoplayer_challenge_user);

		context = getApplicationContext();
		final ListView listview = (ListView) findViewById(R.id.listview);
		Log.d("User list", "getting list of users");
		getuserList();
		
		for (int i = 0; i < userList.length; ++i) {
			list.add(userList[i]);
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				receiver = (String) parent.getItemAtPosition(position);	
				sendMessage("test");
				//startActivity(new Intent(context, edu.neu.madcourse.deborahho.twoPlayer.Game.class));
			}
		});
	}
	
	private void getuserList() {
		if (!isOnline()) {
			Toast.makeText(this, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
        Toast.makeText(this, "Loading users list", Toast.LENGTH_LONG).show();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				Log.d("User list", "Inside getUserList");
				int cnt = 0;
				if (!KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "cnt").contains(
						"Error")) {
					cnt = Integer.parseInt(KeyValueAPI.get("CommunicationConstants.TEAM_NAME",
							"CommunicationConstants.PASSWORD", "cnt"));
					Log.d("User list", "first if");
					
				}	
				else {
					msg = KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "cnt");
					Log.d("User list", msg);
					return msg;
				}
				userList = new String [cnt];
				Log.d("User List", "testing " + cnt);
				for (int i = 1; i <= cnt; i++) {
					userList[i-1] = KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "user"
									+ String.valueOf(i));
					Log.d("User List",userList[i-1]);
				}				
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				list.clear();
				
				for (int i = 0; i < userList.length; ++i) {
					list.add(userList[i]);
				}
				Log.d("list",list.get(0));
				runOnUiThread(new Runnable() {
			    public void run() {
			        adapter.notifyDataSetChanged();
			    }
			});
			}
		}.execute(null, null, null);
	}
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
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
		return getSharedPreferences(Communication.class.getSimpleName(),
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

	private void sendRegistrationIdToBackend() {
		// Your implementation here.
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

	@SuppressLint("NewApi")
	private void sendMessage(final String message) {
		regid = getRegistrationId(context);
		if (regid == null || regid.equals("")) {
			Toast.makeText(this, "You must register first", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (message.isEmpty()) {
			Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
			return;
		}
		if (receiver.isEmpty()) {
			Toast.makeText(this, "Empty Destination", Toast.LENGTH_LONG).show();
			return;
		}
		if (!isOnline()) {
			Toast.makeText(this, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				if (KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "cnt").contains(
						"Error")){
					msg = KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "cnt");
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
				KeyValueAPI.put("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "alertText",
						"Message Notification");
				KeyValueAPI.put("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "titleText",
						"Sending Message");
				KeyValueAPI.put("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "contentText",
						message);
				KeyValueAPI.put("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "nIcon",
						String.valueOf(nIcon));
				KeyValueAPI.put("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", "nType",
						String.valueOf(nType));
				GcmNotification gcmNotification = new GcmNotification();
				regIds.clear();
				reg_device = KeyValueAPI.get("CommunicationConstants.TEAM_NAME", "CommunicationConstants.PASSWORD", receiver);
				regIds.add(reg_device);
				gcmNotification
						.sendNotification(
								msgParams,
								regIds,context);
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
