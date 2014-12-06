// source: http://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html

package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.finalproject.WorkOutConstants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;


public class CheckOutSchedule extends Activity implements OnClickListener{
	
	ListView list;
	
	SharedPreferences schedule;
	String[] workout = new String[WorkOutConstants.DAY.length];
	Integer[] icons = new Integer[WorkOutConstants.DAY.length];
	int day;
	int isDone;
	
//	  String[] web = {
//	    "Google Plus",
//	      "Twitter \n Hello World",
//	      "Windows",
//	      "Bing",
//	      "Itunes",
//	      "Wordpress",
//	      "Drupal"
//	  } ;
//	  Integer[] imageId = {
//	      R.drawable.check,
//	      R.drawable.check,
//	      R.drawable.miss,
//	      R.drawable.check,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming,
//	      R.drawable.upcoming
//	  };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_check_out_schedule);	

        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
        
        getWorkoutSchedule();
        
        CustomList adapter = new CustomList(CheckOutSchedule.this, workout, icons);
        list=(ListView)findViewById(R.id.listview_schedule);
        list.setAdapter(adapter);
	}
	
	void getWorkoutSchedule() {
		for(int i=0; i<WorkOutConstants.DAY.length; i++) {
			day = i+1;
			workout[i] = "Day " + day + "\n" + WorkOutConstants.REPETITIONS[i] + " Times " + WorkOutConstants.DAY[i] + " Crunches";
			schedule = getSharedPreferences(
					WorkOutConstants.DONE_WORKOUT_PREFS, 0);
			isDone = schedule.getInt(""+i, WorkOutConstants.UPCOMING);
			switch (isDone) {
			case WorkOutConstants.MISS:
				icons[i]=R.drawable.miss;
				break;
			case WorkOutConstants.UPCOMING:
				icons[i]=R.drawable.upcoming;
				break;
			case WorkOutConstants.DONE:
				icons[i]=R.drawable.check;
				break;
			}
			
		}
		
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
