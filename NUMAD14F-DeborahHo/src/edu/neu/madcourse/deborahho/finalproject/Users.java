package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.neu.mhealth.api.KeyValueAPI;

public class Users extends Activity {
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	
	String [] usersList = {""};
	ArrayAdapter<String> adapter;
	ArrayList<String> list = new ArrayList<String>();
	Context context;
	String regid; 
	GoogleCloudMessaging gcm;
	String receiver = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_users);
		getUsersList();

		context = getApplicationContext();
		final ListView listview = (ListView) findViewById(R.id.listview_users);
		
		for (int i = 0; i < usersList.length; ++i) {
			list.add(usersList[i]);
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				receiver = (String) parent.getItemAtPosition(position);	
				sendMessage("You received a challenge request!");
				Intent i = new Intent(context, CrunchyMenu.class);
				finish();
				startActivity(i);
			}
		});
	}
	
	private void getUsersList() {
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
				int cnt = 0;
				if (!KeyValueAPI.get("eighilaza", "eighilaza", "cnt").contains(
						"Error"))
					cnt = Integer.parseInt(KeyValueAPI.get("eighilaza",
							"eighilaza", "cnt"));
				else {
					msg = KeyValueAPI.get("eighilaza", "eighilaza", "cnt");
					return msg;
				}
				usersList = new String [cnt];
				for (int i = 1; i <= cnt; i++) {
					usersList[i-1] = KeyValueAPI.get("eighilaza", "eighilaza", "user"
									+ String.valueOf(i));
					Log.d("User List",usersList[i-1]);
				}				
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				list.clear();
				
				for (int i = 0; i < usersList.length; ++i) {
					list.add(usersList[i]);
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
		return registrationId;
	}

	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
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
				if (KeyValueAPI.get("eighilaza", "eighilaza", "cnt").contains(
						"Error")){
					msg = KeyValueAPI.get("eighilaza", "eighilaza", "cnt");
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
				KeyValueAPI.put("eighilaza", "eighilaza", "alertText",
						"Message Notification");
				KeyValueAPI.put("eighilaza", "eighilaza", "titleText",
						"Sending Message");
				KeyValueAPI.put("eighilaza", "eighilaza", "contentText",
						message);
				KeyValueAPI.put("eighilaza", "eighilaza", "nIcon",
						String.valueOf(nIcon));
				KeyValueAPI.put("eighilaza", "eighilaza", "nType",
						String.valueOf(nType));
				GcmNotification gcmNotification = new GcmNotification();
				regIds.clear();
				reg_device = KeyValueAPI.get("eighilaza", "eighilaza", receiver);
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
