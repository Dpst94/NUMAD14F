package edu.neu.madcourse.deborahho.finalproject;

import java.util.Calendar;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ViewScores extends Activity implements OnClickListener{
	
	TextView user01;
	TextView score01;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_view_scores);
		
		user01 = (TextView) findViewById(R.id.username_01);		
		user01.append("Debbie");
		
		score01 = (TextView) findViewById(R.id.score_01);		
		score01.append(""+calculateScore());

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
				score = score + 5;
			}
			else if(isDone==0){
				score = score - 5;
			}
			else{
				SharedPreferences.Editor editor = workout.edit();
				editor.putInt(""+i, WorkOutConstants.MISS);
				editor.commit();
			}
		}
		return score;
	}
}
