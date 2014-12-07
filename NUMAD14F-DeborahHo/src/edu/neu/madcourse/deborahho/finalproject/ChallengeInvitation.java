package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.R.layout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ChallengeInvitation extends Activity implements OnClickListener{
	TextView tx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_challenge_invitation);
		
		SharedPreferences challengersName = getSharedPreferences(
				"challenger", 0);
		challengersName.getString("challenger", "UNKNOWN");
		
		tx = (TextView) findViewById(R.id.challenge_invitation_text);
		tx.setText("Would you like to accept the invitation?");
		
		View yesButton = findViewById(R.id.yes_button);
        yesButton.setOnClickListener(this);
        View noButton = findViewById(R.id.no_button);
        noButton.setOnClickListener(this);		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yes_button:
			finish();
			break;
		case R.id.no_button:
			finish();
			break;			
		}
	}
}
