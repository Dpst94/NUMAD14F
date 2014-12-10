package edu.neu.madcourse.deborahho.finalproject;

import java.util.Calendar;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.finalproject.WorkOutConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


public class CheckOutSchedule extends Activity implements OnClickListener{
	
	ListView list;
	
	SharedPreferences schedule;
	String[] workout = new String[WorkOutConstants.DAY.length];
	Integer[] icons = new Integer[WorkOutConstants.DAY.length];
	int day;
	int isDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_check_out_schedule);	

        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
        
        calculateScore();
        getWorkoutSchedule();
        
        CustomList adapter = new CustomList(CheckOutSchedule.this, workout, icons);
        list=(ListView)findViewById(R.id.listview_schedule);
        list.setAdapter(adapter);
	}
	
	void getWorkoutSchedule() {
		for(int i=0; i<WorkOutConstants.DAY.length; i++) {
			day = i+1;
			workout[i] = "Day " + day + "\n" + WorkOutConstants.REPETITIONS[i] + " Times " + WorkOutConstants.DAY[i] + " Crunches";
			schedule = getSharedPreferences(
					WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = schedule.getInt(""+i, WorkOutConstants.UPCOMING);
			switch (isDone) {
			case WorkOutConstants.MISS:
				icons[i]=R.drawable.miss;
				break;
			case WorkOutConstants.UPCOMING:
				icons[i]=R.drawable.upcoming;
				break;
			case WorkOutConstants.DONE:
				icons[i]=R.drawable.check;
				break;
			}			
		}		
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
}
