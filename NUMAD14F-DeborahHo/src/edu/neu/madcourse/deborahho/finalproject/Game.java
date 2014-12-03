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

	public static final String DAY_PREFS = "DayNumber";

	TextView mCurrentDay;
	TextView mNrOfCrunches;
	TextView mCountdownCrunches;

	private SensorManager sensorManager = null;
	private Sensor currentSensor = null;
	float currentLux = 0;
	boolean crunchDown = false;
	int nrOfCrunches = 10;
	int nrOfRepetitions = 1;
	long oldSeconds = 0;
	long currentSeconds;
	long differenceInSeconds;
	Calendar c;
	static final int limitLux = 10;
	static final int limitMilliSecBetweenCrunches = 1500;

	int day_nb = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_game);

		mCurrentDay = (TextView) findViewById(R.id.crunchy_current_day);
		mNrOfCrunches = (TextView) findViewById(R.id.crunchy_nr_of_crunches);
		mCountdownCrunches = (TextView) findViewById(R.id.crunchy_countdown_crunches);

		// Restore day number
		SharedPreferences schedule = getSharedPreferences(DAY_PREFS, 0);
		day_nb = schedule.getInt("day_nb", 1);

		nrOfCrunches = WorkOutConstants.day[day_nb-1];
		nrOfRepetitions = WorkOutConstants.repetition[day_nb-1];
		
		mCurrentDay.append("Day " + day_nb);
		mNrOfCrunches.append("Do "+nrOfRepetitions+"x "+nrOfCrunches+" crunches");
		mCountdownCrunches.append(nrOfCrunches+" CRUNCHES TO GO");

		View nextButton = findViewById(R.id.finalproject_menu_button);
		nextButton.setOnClickListener(this);
		View backButton = findViewById(R.id.finalproject_back_button);
		backButton.setOnClickListener(this);

		c = Calendar.getInstance();
		oldSeconds = c.getTimeInMillis();
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if (currentSensor != null) {
			sensorManager.registerListener(this, currentSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		} else {
			Toast.makeText(this, "Can't initialize the LIGHT sensor.",
					Toast.LENGTH_LONG).show();
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

			if (event.values[0] < limitLux) {

				if (!crunchDown) {

					c = Calendar.getInstance();
					currentSeconds = c.getTimeInMillis();

					differenceInSeconds = currentSeconds - oldSeconds;
					if (differenceInSeconds > limitMilliSecBetweenCrunches) {
						crunchDown = true;
						nrOfCrunches--;

						if (nrOfCrunches > 0) {
							Music.playOnce(this, R.raw.beep);
							mCountdownCrunches.setText(nrOfCrunches
									+ " CRUNCHES TO GO");
						} else {
							// We need an Editor object to make preference
							// changes.
							SharedPreferences schedule = getSharedPreferences(
									DAY_PREFS, 0);
							SharedPreferences.Editor editor = schedule.edit();
							editor.putInt("day_nb", day_nb + 1);
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
