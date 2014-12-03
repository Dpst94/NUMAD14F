package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Game extends Activity implements OnClickListener,
		SensorEventListener {

	TextView mCurrentDay;
	TextView mNrOfCrunches;
	TextView mCountdownCrunches;

	private SensorManager sensorManager = null;
	private Sensor currentSensor = null;
	float currentLux = 0;
	long oldSeconds = 0;
	long currentSeconds;
	long differenceInSeconds;	

	boolean crunchDown = false;
	int nrOfCrunches = 10;
	int nrOfRepetitions = 1;
	
	int day_nb = 1;
	Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_game);

		mCurrentDay = (TextView) findViewById(R.id.crunchy_current_day);
		mNrOfCrunches = (TextView) findViewById(R.id.crunchy_nr_of_crunches);
		mCountdownCrunches = (TextView) findViewById(R.id.crunchy_countdown_crunches);
		
		c = Calendar.getInstance();

		// Restore day number
		SharedPreferences schedule = getSharedPreferences(WorkOutConstants.DAY_PREFS, 0);
		day_nb = schedule.getInt("day_nb", 1);
		
		if (day_nb == 1) {
			
			nrOfCrunches = WorkOutConstants.DAY[0];
			nrOfRepetitions = WorkOutConstants.REPITITIONS[0];

			mCurrentDay.append("Day " + day_nb);
			mNrOfCrunches.append("Do " + nrOfRepetitions + "x " + nrOfCrunches
					+ " crunches");
			mCountdownCrunches.append(nrOfCrunches + " CRUNCHES TO GO");

			View nextButton = findViewById(R.id.finalproject_menu_button);
			nextButton.setOnClickListener(this);
			View backButton = findViewById(R.id.finalproject_back_button);
			backButton.setOnClickListener(this);
			
			oldSeconds = c.getTimeInMillis();
			sensorManager = (SensorManager) this
					.getSystemService(SENSOR_SERVICE);
			currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
			if (currentSensor != null) {
				sensorManager.registerListener(this, currentSensor,
						SensorManager.SENSOR_DELAY_FASTEST);
			} else {
				Toast.makeText(this, "Can't initialize the LIGHT sensor.",
						Toast.LENGTH_LONG).show();
			}

		} else {
			
			day_nb = c.get(Calendar.DAY_OF_YEAR) - day_nb;
			//In case first day was December 25th and today is January 5th for example
			if(day_nb < 0){
				day_nb = 365 + day_nb;
			}
			
			nrOfCrunches = WorkOutConstants.DAY[day_nb];
			nrOfRepetitions = WorkOutConstants.REPITITIONS[day_nb];

			mCurrentDay.append("Day " + day_nb+1);
			mNrOfCrunches.append("Do " + nrOfRepetitions + "x " + nrOfCrunches
					+ " crunches");
			mCountdownCrunches.append(nrOfCrunches + " CRUNCHES TO GO");

			View nextButton = findViewById(R.id.finalproject_menu_button);
			nextButton.setOnClickListener(this);
			View backButton = findViewById(R.id.finalproject_back_button);
			backButton.setOnClickListener(this);

			oldSeconds = c.getTimeInMillis();
			sensorManager = (SensorManager) this
					.getSystemService(SENSOR_SERVICE);
			currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
			if (currentSensor != null) {
				sensorManager.registerListener(this, currentSensor,
						SensorManager.SENSOR_DELAY_FASTEST);
			} else {
				Toast.makeText(this, "Can't initialize the LIGHT sensor.",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (currentSensor != null)
			sensorManager.registerListener(this, currentSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		case R.id.finalproject_menu_button:
			Intent i = new Intent(this, CrunchyMenu.class);
			startActivity(i);
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

			if (event.values[0] < WorkOutConstants.LIMITLUX) {

				if (!crunchDown) {

					c = Calendar.getInstance();
					currentSeconds = c.getTimeInMillis();

					differenceInSeconds = currentSeconds - oldSeconds;
					if (differenceInSeconds > WorkOutConstants.limitMilliSecBetweenCrunches) {
						crunchDown = true;
						nrOfCrunches--;

						if (nrOfCrunches > 0) {
							Music.playOnce(this, R.raw.beep);
							mCountdownCrunches.setText(nrOfCrunches
									+ " CRUNCHES TO GO");
						} else {
							if(day_nb == 1){
								SharedPreferences schedule = getSharedPreferences(
										WorkOutConstants.DAY_PREFS, 0);
								SharedPreferences.Editor editor = schedule.edit();
								editor.putInt("day_nb", c.get(Calendar.DAY_OF_YEAR));
								editor.commit();
							}
							

							SharedPreferences schedule = getSharedPreferences(
									WorkOutConstants.DONE_WORKOUT_PREFS, 0);
							SharedPreferences.Editor editor = schedule.edit();
							editor.putInt(""+day_nb, WorkOutConstants.DONE);
							editor.commit();

							Music.playOnce(this, R.raw.beep);
							Intent i = new Intent(this, CrunchyMenu.class);
							finish();
							startActivity(i);
						}
						oldSeconds = currentSeconds;
					}
				}
			} else {
				if (crunchDown) {
					crunchDown = false;
				}
			}
		}
	}
}
