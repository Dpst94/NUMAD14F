package edu.neu.madcourse.deborahho.trickiestpart;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.twoplayerwordgame.TwoPlayerAcknowledgements;
import edu.neu.madcourse.deborahho.twoplayerwordgame.TwoPlayerSelectGame;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
 
public class TrickiestPart extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trickiestpart_main);
        
       // Set up click listeners for all the buttons
        View detectCrunchesButton = findViewById(R.id.trickiestpart_detect_crunches);
        detectCrunchesButton.setOnClickListener(this);
        View recordAudioButton = findViewById(R.id.trickiestpart_record_audio);
        recordAudioButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View acknowledgements_button = findViewById(R.id.trickiestpart_acknowledgements_button);
        acknowledgements_button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
    	case R.id.trickiestpart_detect_crunches:			
    		Intent l = new Intent(this, TrickiestPartDetectCrunches.class);
    		startActivity(l);
			break;	
    	case R.id.trickiestpart_record_audio:
    		//Intent k = new Intent(this, TwoPlayerHighScores.class);
    		//startActivity(k);
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.trickiestpart_acknowledgements_button:
    		Intent j = new Intent(this, TwoPlayerAcknowledgements.class);
    		startActivity(j);
    		break;
    	}
		
	}

}
