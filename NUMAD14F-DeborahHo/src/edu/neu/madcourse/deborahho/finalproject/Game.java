package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Game extends Activity implements OnClickListener{
	
	TextView mCurrentDay;
	TextView mNrOfCrunches;
	TextView mCountdownCrunches;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_game);
		
		mCurrentDay = (TextView) findViewById(R.id.crunchy_current_day);
		mNrOfCrunches = (TextView) findViewById(R.id.crunchy_nr_of_crunches);
		mCountdownCrunches = (TextView) findViewById(R.id.crunchy_countdown_crunches);
		
		mCurrentDay.append("Day 01");
		mNrOfCrunches.append("Do 1x 10 crunches");
		mCountdownCrunches.append("10 CRUNCHES TO GO");
		
		View nextButton = findViewById(R.id.finalproject_next_button);
        nextButton.setOnClickListener(this);
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
