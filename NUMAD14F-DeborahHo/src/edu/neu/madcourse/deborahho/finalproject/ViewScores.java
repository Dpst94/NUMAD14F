package edu.neu.madcourse.deborahho.finalproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewScores extends Activity implements OnClickListener{
	
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
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		
		
//		user01 = (TextView) findViewById(R.id.username_01);

		context=getApplicationContext();
		final SharedPreferences prefs = getGCMPreferences(context);
//		user01.append(prefs.getString("username", "Unknown"));

		score = calculateScore();
//		map.put("user", prefs.getString("username", "Unknown"));
//		map.put("score", Integer.toString(score));
		map.put("user", "USERNAME");
		map.put("score", "SCORE");
		list.add(map);
		//score01 = (TextView) findViewById(R.id.score_01);		
		//score01.append(""+calculateScore());

		map = new HashMap<String, String>();
		map.put("user", prefs.getString("username", "Unknown"));
		map.put("score", Integer.toString(score));
		list.add(map);
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.finalproject_two_column_list, new String[]{"user", "score"}, new int[]{R.id.finalproject_scores_username, R.id.finalproject_scores});
        listview.setAdapter(simpleAdapter);
        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {				
				HashMap<String,String> mapy =(HashMap<String,String>)parent.getItemAtPosition(position);
				String receiver = mapy.get("user");
				Log.d("RECEIVER",receiver);
				//final SharedPreferences prefs = getGCMPreferences(context);
				//sendMessage(prefs.getString("username", "Unknown"));
				//Toast.makeText(context, "You invited "+ receiver, Toast.LENGTH_LONG).show();
				Intent i = new Intent(context, RecordAudio.class);
				//finish();
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
	
	private int calculateScore(){
		int score = 0;
		int today = 0;
		int isDone = 1;
		
		Calendar c;
		c = Calendar.getInstance();
		SharedPreferences schedule = getSharedPreferences(WorkOutConstants.DAY_PREFS, 0);
		int first_day = schedule.getInt("day_nb", 0);
		if(first_day == 0){
			today = 0;
		}
		else{
			today = c.get(Calendar.DAY_OF_YEAR) - first_day;
		}
		
		for(int i  = 0; i < today+1; i++){
			SharedPreferences workout = getSharedPreferences(WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = workout.getInt(""+i, WorkOutConstants.DONE);
			if(isDone==2){
				score = score + 10;
			}
			else if(isDone==0){
				score = score - 5;
			}
			else if(i != today){
				SharedPreferences.Editor editor = workout.edit();
				editor.putInt(""+i, WorkOutConstants.MISS);
				editor.commit();
				score = score - 5;
			}
		}
		return score;
	}
	
	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
}