package edu.neu.madcourse.deborahho.bananagrams;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class BananaFinish extends Activity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banana_finish);
		
		 View playAgainButton = findViewById(R.id.banana_play_again_button);
	     playAgainButton.setOnClickListener(this);
	     
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.banana_play_again_button: 
			finish();
			break;
		case R.id.banana_quit_button:
    		finish();
    		break;
		}
		
	}

}