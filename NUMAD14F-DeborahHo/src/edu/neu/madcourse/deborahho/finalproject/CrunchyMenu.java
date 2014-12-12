package edu.neu.madcourse.deborahho.finalproject;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import edu.neu.madcourse.deborahho.R;

public class CrunchyMenu extends Activity implements OnClickListener{
	
	String registrationId;
	TextView dayText;
	TextView doneText;
	int isDone;
	int day_nb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_menu);
		
		View shareButton = findViewById(R.id.share_facebook_button);
        shareButton.setOnClickListener(this);
        View inviteButton = findViewById(R.id.invite_friends_button);
        inviteButton.setOnClickListener(this);
        View viewPicturesButton = findViewById(R.id.view_pictures_friends_button);
        viewPicturesButton.setOnClickListener(this);
        View scoresButton = findViewById(R.id.view_scores_button);
        scoresButton.setOnClickListener(this);
        View scheduleButton = findViewById(R.id.check_schedule_button);
        scheduleButton.setOnClickListener(this);        
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
        
        dayText = (TextView) findViewById(R.id.crunchy_current_day);
        doneText = (TextView) findViewById(R.id.crunchy_completed_workout_text);        
        
        Calendar c;
		c = Calendar.getInstance();
		SharedPreferences schedule = getSharedPreferences(WorkOutConstants.DAY_PREFS, 0);
		int first_day = schedule.getInt("day_nb", 0);
		if(first_day == 0){
			isDone = 1;
			day_nb=0;
		}
		else{
			day_nb = c.get(Calendar.DAY_OF_YEAR) - first_day;
			SharedPreferences workout = getSharedPreferences(WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = workout.getInt(""+day_nb, WorkOutConstants.UPCOMING);
		}
		
		dayText.setText("Day "+(day_nb+1));
		if(isDone == 1){
			doneText.setText("Remember to do your crunches today!");
		}
		else{
			doneText.setText("You have completed your workout for today!");
		}		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.share_facebook_button:
    		Intent i = new Intent(this, ShareOnFacebookLogIn.class);
    		startActivity(i);
    		break;
    	case R.id.invite_friends_button:
    		final SharedPreferences prefs = getSharedPreferences(Main.class.getSimpleName(),
					Context.MODE_PRIVATE);;
			registrationId = prefs.getString("registration_id", "");
			if (registrationId.isEmpty()) {
				startActivity(new Intent(this, Register.class));
				break;
			}			
			startActivity(new Intent(this, Users.class));
			break;	
    		
    	case R.id.view_pictures_friends_button:
    		final SharedPreferences prefs2 = getSharedPreferences(Main.class.getSimpleName(),
					Context.MODE_PRIVATE);;
			registrationId = prefs2.getString("registration_id", "");
			if (registrationId.isEmpty()) {
				startActivity(new Intent(this, Register.class));
				break;
			}	
    		Intent k = new Intent(this, UsersPicture.class);
    		startActivity(k);
    		break;
    	case R.id.view_scores_button:
    		Intent l = new Intent(this, ViewScores.class);
    		startActivity(l);
    		break;
    	case R.id.check_schedule_button:
    		Intent m = new Intent(this, CheckOutSchedule.class);//RecordAudio.class);
    		startActivity(m);
    		break;
    	case R.id.finalproject_back_button:
			finish();
			break;
		}	
	}
}

