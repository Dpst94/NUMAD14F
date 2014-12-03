package edu.neu.madcourse.deborahho.finalproject;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class TutorialPictures extends Activity implements OnClickListener{
	
	public static final String FIRST_OPEN_PREFS = "FirstOpen";
	int isDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_tutorial_pictures);
		
		View nextButton = findViewById(R.id.finalproject_next_button);
        nextButton.setOnClickListener(this);        
        
		Calendar c;
		c = Calendar.getInstance();
		SharedPreferences schedule = getSharedPreferences(WorkOutConstants.DAY_PREFS, 0);
		int first_day = schedule.getInt("day_nb", 0);
		if(first_day == 0){
			isDone = 1;
		}
		else{
			int day_nb = c.get(Calendar.DAY_OF_YEAR) - first_day;
			SharedPreferences workout = getSharedPreferences(WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = workout.getInt(""+day_nb, WorkOutConstants.DONE);
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.finalproject_next_button:
			SharedPreferences schedule = getSharedPreferences(
					FIRST_OPEN_PREFS, 0);
			SharedPreferences.Editor editor = schedule.edit();
			editor.putBoolean("never_opened", false);
			editor.commit();
			if(isDone == 1){
				Intent i = new Intent(this, Game.class);
				finish();
	    		startActivity(i);
			}
			else{
				Intent i = new Intent(this, Main.class);
				finish();
	    		startActivity(i);
			}			
    		break;
		}		
	}
}
