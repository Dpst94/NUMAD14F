package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ViewScores extends Activity implements OnClickListener{
	
	TextView day01;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_view_scores);
		
		day01 = (TextView) findViewById(R.id.schedule_day_1);
		
		day01.append("Day 01");

        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		}
	}
}
