package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class FinalProjectDescription extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_description);
		
		View startAppButton = findViewById(R.id.finalproject_start_app_button);
        startAppButton.setOnClickListener(this);
        View ackButton = findViewById(R.id.finalproject_ack_button);
        ackButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.finalproject_start_app_button:
    		Intent i = new Intent(this, Main.class);
    		startActivity(i);
    		break;
    	case R.id.finalproject_ack_button:
    		Intent j = new Intent(this, Ack.class);
    		startActivity(j);
    		break;
		}
    		
		
	}

}
