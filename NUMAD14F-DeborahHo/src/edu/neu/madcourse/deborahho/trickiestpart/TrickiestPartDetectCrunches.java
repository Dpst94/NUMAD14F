// Source: http://android-apps-blog.blogspot.com/2011/05/how-to-use-light-sensor-on-android.html

package edu.neu.madcourse.deborahho.trickiestpart;

import edu.neu.madcourse.deborahho.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
 
public class TrickiestPartDetectCrunches extends Activity implements SensorEventListener {
	private  SensorManager sensorManager = null;
	private  Sensor currentSensor = null; 
	float currentLux = 0;
	boolean crunchDown = false;
	int nrOfCrunches = -1;
	static final int limitLux = 10;
  
	@Override
	public void onResume(){
		super.onResume();
		if(currentSensor != null)sensorManager.registerListener(this, currentSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}
  
	@Override
	public void onPause(){
		super.onPause();
		sensorManager.unregisterListener(this);
	} 
  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trickiestpart_detect_crunches);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        currentSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT );
        if(currentSensor != null){
        	sensorManager.registerListener(this, currentSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }else{
        	((TextView) findViewById(R.id.textView1)).setText("Can't initialize the LIGHT sensor.");   
        } 
    }
 
 	@Override
 	public void onAccuracyChanged(Sensor sensor, int accuracy) {
 		// TODO Auto-generated method stub
 	}
 
 	@Override
 	public void onSensorChanged(SensorEvent event) {
 	 // TODO Auto-generated method stub
   
 		if (event.sensor.getType() == Sensor.TYPE_LIGHT){  
 			
 			if(event.values[0] < limitLux) {
 				if (!crunchDown) {
 					crunchDown = true;
 	 				nrOfCrunches++;
 	 				TextView tv = (TextView) findViewById(R.id.textView1);
 	 				tv.setText("\nNumber of crunches " + nrOfCrunches);
 	 				Music.playOnce(this, R.raw.beep);
 					
 				} 	
 			} else {
 				if (crunchDown) {
 					crunchDown = false;
 				}
 			}
 			
 			// Get the current intensity of the light sensor:
 			
// 			if (event.values[0] != currentLux) {
// 				currentLux = event.values[0];
// 				TextView tv = (TextView) findViewById(R.id.textView1);
// 				//tv.setText( tv.getText()+ "value: " +event.values[0] + " lux , time: " + new Date() + "\n");   
// 				tv.setText( tv.getText()+ "value: " + currentLux + " lux , time: " + new Date() + "\n");
// 			}
 		}
 	}
}

