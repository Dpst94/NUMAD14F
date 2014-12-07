package edu.neu.madcourse.deborahho.finalproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import edu.neu.madcourse.deborahho.R;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewScores extends Activity implements OnClickListener {

	TextView user01;
	TextView score01;
	Context context;
	ListView listview;
	int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_view_scores);

		listview = (ListView) findViewById(R.id.listview_scores);

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();

		context = getApplicationContext();
		final SharedPreferences prefs = getGCMPreferences(context);

		score = calculateScore();
		map.put("user", "USERNAME");
		map.put("score", "SCORE");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("user", prefs.getString("username", "Unknown"));
		map.put("score", Integer.toString(score));
		list.add(map);
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.finalproject_two_column_list, new String[] { "user",
						"score" }, new int[] {
						R.id.finalproject_scores_username,
						R.id.finalproject_scores });
		listview.setAdapter(simpleAdapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				HashMap<String, String> mapy = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				String receiver = mapy.get("user");
				SharedPreferences name = getSharedPreferences("audio_receiver",
						0);
				SharedPreferences.Editor editor = name.edit();
				editor.putString("receiver", receiver);
				editor.commit();

				Log.d("RECEIVER", receiver);
				Intent i = new Intent(context, RecordAudio.class);
				startActivity(i);
			}
		});

		View backButton = findViewById(R.id.finalproject_back_button);
		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		}
	}

	public int calculateScore() {
		int score = 0;
		int today = 0;
		int isDone = 1;

		Calendar c;
		c = Calendar.getInstance();
		SharedPreferences schedule = getSharedPreferences(
				WorkOutConstants.DAY_PREFS, 0);
		int first_day = schedule.getInt("day_nb", 0);
		if (first_day == 0) {
			today = 0;
		} else {
			today = c.get(Calendar.DAY_OF_YEAR) - first_day;
		}

		for (int i = 0; i < today + 1; i++) {
			SharedPreferences workout = getSharedPreferences(
					WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = workout.getInt("" + i, WorkOutConstants.UPCOMING);
			if (isDone == 2) {
				score = score + 10;
			} else if (isDone == 0) {
				score = score - 5;
			} else if (i != today) {
				SharedPreferences.Editor editor = workout.edit();
				editor.putInt("" + i, WorkOutConstants.MISS);
				editor.commit();
				score = score - 5;
			}
		}
		return score;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/*public void uploadScore() {
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
						regid = gcm
								.register(CommunicationConstants.GCM_SENDER_ID);
						KeyValueAPI.put("eighilaza", "eighilaza", "cnt",
								String.valueOf(cnt + 1));
						KeyValueAPI.put("eighilaza", "eighilaza", "regid"
								+ String.valueOf(cnt + 1), regid);
						KeyValueAPI.put("eighilaza", "eighilaza", username,
								regid);
						KeyValueAPI.put("eighilaza", "eighilaza", "user"
								+ String.valueOf(cnt + 1), username);

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
	}*/

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