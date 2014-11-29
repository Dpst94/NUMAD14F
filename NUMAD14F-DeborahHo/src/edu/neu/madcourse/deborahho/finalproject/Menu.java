package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class Menu extends Activity implements OnClickListener{
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
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.share_facebook_button:
//    		Intent i = new Intent(this, Game.class);//TutorialFriends.class);
//    		startActivity(i);
    		break;
    	case R.id.challenge_friends_button:
    		break;
    	case R.id.team_up_friends_button:
    		break;
    	case R.id.view_scores_button:
    		break;
    	case R.id.check_schedule_button:
    		break;
		}
    		
		
	}

}

