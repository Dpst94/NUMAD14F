package edu.neu.madcourse.deborahho.communication;

import edu.neu.madcourse.deborahho.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import edu.neu.mhealth.api.KeyValueAPI;

public class ComFindContacts extends Activity implements OnClickListener{
	
	String GCM_API_KEY = "AIzaSyAJUK8ioj2HAOyDBKMTMJ80DDShn5UUDIQ";
	String SENDER_ID = "341143421214";
	String TEAM_NAME = "DeborahHo";
	String PASSWORD = "patricia";
	
	String userList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_find_contacts);
        
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);    
    
        findContacts();
	}	
	
	private void findContacts(){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
			String msg = "";
			userList = "";
			int cnt = 0;
			if (!KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt").contains(
					"Error"))
				cnt = Integer.parseInt(KeyValueAPI.get(TEAM_NAME,
						PASSWORD, "cnt"));
			else {
				msg = KeyValueAPI.get(TEAM_NAME, PASSWORD, "cnt");
				return msg;
			}			
			
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit_button:
			finish();
			break;
		}
		
	}

}
