package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class TutorialPictures extends Activity implements OnClickListener{
	
	public static final String FIRST_OPEN_PREFS = "FirstOpen";
	boolean never_opened = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_tutorial_pictures);
		
		View nextButton = findViewById(R.id.finalproject_next_button);
        nextButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.finalproject_next_button:
    		// We need an Editor object to make preference
			// changes.
			SharedPreferences schedule = getSharedPreferences(
					FIRST_OPEN_PREFS, 0);
			SharedPreferences.Editor editor = schedule.edit();
			editor.putBoolean("never_opened", false);
			editor.commit();
    		Intent i = new Intent(this, Game.class);
    		startActivity(i);
    		break;
		}
    		
		
	}

}
