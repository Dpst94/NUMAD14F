package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class Main extends Activity implements OnClickListener {

	public static final String FIRST_OPEN_PREFS = "FirstOpen";
	boolean never_opened = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if the app has been used before
		SharedPreferences first = getSharedPreferences(FIRST_OPEN_PREFS, 0);
		never_opened = first.getBoolean("never_opened", true);

		if (never_opened == true) {
			setTitle(R.string.crunchy_label);
			setContentView(R.layout.finalproject_main);

			View nextButton = findViewById(R.id.finalproject_next_button);
			nextButton.setOnClickListener(this);
		} else {
			setTitle(R.string.crunchy_label);
			setContentView(R.layout.finalproject_main_bis);

			View menuButton = findViewById(R.id.finalproject_menu_button);
			menuButton.setOnClickListener(this);
			View startButton = findViewById(R.id.finalproject_start_button);
			startButton.setOnClickListener(this);
			View backButton = findViewById(R.id.finalproject_back_button);
			backButton.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_next_button:
			Intent i = new Intent(this, TutorialFriends.class);
			startActivity(i);
			break;

		case R.id.finalproject_menu_button:
			startActivity(new Intent(this, CrunchyMenu.class));
			break;

		case R.id.finalproject_start_button:
			startActivity(new Intent(this, Game.class));
			break;
			
		case R.id.finalproject_back_button:
			finish();
			break;
		}

	}

}
