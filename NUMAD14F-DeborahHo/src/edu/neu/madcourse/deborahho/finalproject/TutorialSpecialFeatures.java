package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class TutorialSpecialFeatures extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_tutorial_special_features);
		
		View nextButton = findViewById(R.id.finalproject_next_button);
        nextButton.setOnClickListener(this);
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.finalproject_next_button:
    		Intent i = new Intent(this, TutorialPictures.class);
    		startActivity(i);
    		break;
    	case R.id.finalproject_back_button:
    		finish();
    		break;
		}
    		
		
	}

}
