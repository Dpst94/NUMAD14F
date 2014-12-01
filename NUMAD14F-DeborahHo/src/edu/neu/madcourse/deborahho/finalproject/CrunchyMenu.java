package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class CrunchyMenu extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_menu);
		
		View shareButton = findViewById(R.id.share_facebook_button);
        shareButton.setOnClickListener(this);
        View challengeButton = findViewById(R.id.challenge_friends_button);
        challengeButton.setOnClickListener(this);
        View teamUpButton = findViewById(R.id.team_up_friends_button);
        teamUpButton.setOnClickListener(this);
        View scoresButton = findViewById(R.id.view_scores_button);
        scoresButton.setOnClickListener(this);
        View scheduleButton = findViewById(R.id.check_schedule_button);
        scheduleButton.setOnClickListener(this);
        
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.share_facebook_button:
    		Intent i = new Intent(this, ShareOnFacebookLogIn.class);
    		startActivity(i);
    		break;
    	case R.id.challenge_friends_button:
    		Intent j = new Intent(this, ChallengeFriends.class);
    		startActivity(j);
    		break;
//    	case R.id.team_up_friends_button:
//    		Intent k = new Intent(this, TeamUpWithFriends.class);
//    		startActivity(k);
//    		break;
//    	case R.id.view_scores_button:
//    		Intent l = new Intent(this, ViewScores.class);
//    		startActivity(l);
//    		break;
//    	case R.id.check_schedule_button:
//    		Intent m = new Intent(this, CheckOutSchedule.class);
//    		startActivity(m);
//    		break;
    	case R.id.finalproject_back_button:
			finish();
			break;
		}
    		
		
	}

}

