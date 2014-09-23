package edu.neu.madcourse.deborahho;

import edu.neu.madcourse.deborahho.R;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		
		ImageView image = (ImageView) findViewById(R.id.headshot);
		image.setImageResource(R.drawable.headshot);
		
		TelephonyManager manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		String imei = manager.getDeviceId();
		
		TextView tv = (TextView) findViewById(R.id.imei_content);
		tv.setTypeface(null, 2);
		tv.setText("\nPhone's ID (IMEI): "+imei);
	}
	
	

}
