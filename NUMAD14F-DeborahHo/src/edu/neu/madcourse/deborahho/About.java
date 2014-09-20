package edu.neu.madcourse.deborahho;

import edu.neu.madcourse.deborahho.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		
		ImageView image = (ImageView) findViewById(R.id.headshot);
		image.setImageResource(R.drawable.headshot);
	}

}
