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

public class ComFindContacts extends Activity {
	
	String GCM_API_KEY = "AIzaSyAJUK8ioj2HAOyDBKMTMJ80DDShn5UUDIQ";
	String SENDER_ID = "341143421214";
	String TEAM_NAME = "DeborahHo";
	String PASSWORD = "patricia";
	
	String userList = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_find_contacts);
        
        getUsersList();
	}	
	
	private void getUsersList(){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
			String msg = "";
			int cnt = 0;
			if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt").contains(
					"Error"))
				cnt = Integer.parseInt(KeyValueAPI.get(TEAM_NAME,
						PASSWORD, "cnt"));
			else {
				msg = KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt");
				return msg;
			}			
			//ME
			for (int i = 1; i <= cnt; i++) {
			userList = userList + "\n" + KeyValueAPI.get(TEAM_NAME, PASSWORD, "user"
					+ String.valueOf(i));
			}
			return msg;
		}

		@Override
		protected void onPostExecute(String msg) {
			((TextView) findViewById(R.id.communication_users))
			.setText(userList);
		}
		}.execute(null, null, null);
	}

}
