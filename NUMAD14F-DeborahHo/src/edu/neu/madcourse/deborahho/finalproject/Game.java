package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.trickiestpart.Uploader;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;


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
	
	int day_nb = 0;
	Calendar c;
	
	private static final String TAG = "CamTestActivity";
	Preview preview;
	Button buttonClick;
	Camera camera;
	Activity act;
	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.finalproject_game);
		
		context = this;
		act = this;

		preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((FrameLayout) findViewById(R.id.layout)).addView(preview);
		preview.setKeepScreenOn(true);

		preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});

		Toast.makeText(context, getString(R.string.take_photo_help), Toast.LENGTH_LONG).show();
		

		mCurrentDay = (TextView) findViewById(R.id.crunchy_current_day);
		mNrOfCrunches = (TextView) findViewById(R.id.crunchy_nr_of_crunches);
		mCountdownCrunches = (TextView) findViewById(R.id.crunchy_countdown_crunches);
		
		c = Calendar.getInstance();

		// Restore day number
		SharedPreferences schedule = getSharedPreferences(WorkOutConstants.DAY_PREFS, 0);
		day_nb = schedule.getInt("day_nb", 0);
		
		if (day_nb == 0) {
			
			nrOfCrunches = WorkOutConstants.DAY[0];
			nrOfRepetitions = WorkOutConstants.REPETITIONS[0];

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

		} else {
			
			day_nb = c.get(Calendar.DAY_OF_YEAR) - day_nb;
			//In case first day was December 25th and today is January 5th for example
			if(day_nb < 0){
				day_nb = 365 + day_nb;
			}
			
			nrOfCrunches = WorkOutConstants.DAY[day_nb];
			nrOfRepetitions = WorkOutConstants.REPETITIONS[day_nb];

			mCurrentDay.append("Day " + (day_nb+1));
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
		int numCams = Camera.getNumberOfCameras();
		if(numCams > 0){
			try{
				camera = Camera.open(1);
				camera.startPreview();
				preview.setCamera(camera);
			} catch (RuntimeException ex){
				Toast.makeText(context, getString(R.string.camera_not_found), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPause() {
		if(camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
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
							if(day_nb == 0){
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
	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
	}

	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			//			 Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			//			 Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			new SaveImageTask().execute(data);
			resetCam();
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};

	private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

		@Override
		protected Void doInBackground(byte[]... data) {
			FileOutputStream outStream = null;

			// Write to SD Card
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File (sdCard.getAbsolutePath() + "/crunchy");
				dir.mkdirs();				

				String fileName = String.format("%d.jpg", System.currentTimeMillis());
				File outFile = new File(dir, fileName);

				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();

				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

				refreshGallery(outFile);
				
				Uri uri = Uri.fromFile(outFile);
				String imgpath = Uploader.getPath(context, uri);
				Log.d("IMGPATH",imgpath);
				Uploader.uploadFile(imgpath);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			return null;
		}

	}
}
