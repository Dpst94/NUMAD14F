package edu.neu.madcourse.deborahho.trickiestpart;

import edu.neu.madcourse.deborahho.R;
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
        
        View detectCrunchesButton = findViewById(R.id.trickiestpart_detect_crunches);
        detectCrunchesButton.setOnClickListener(this);
        View recordAudioButton = findViewById(R.id.trickiestpart_record_audio);
        recordAudioButton.setOnClickListener(this);
        View takePictureButton = findViewById(R.id.trickiestpart_take_picture);
        takePictureButton.setOnClickListener(this);
        View acknowledgements_button = findViewById(R.id.trickiestpart_ack);
        acknowledgements_button.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {		
		switch (v.getId()) {
    	case R.id.trickiestpart_detect_crunches:
    		startActivity(new Intent(this, TrickiestPartDetectCrunches.class));
			break;	
    	case R.id.trickiestpart_record_audio:
    		startActivity(new Intent(this, TrickiestPartRecordAudio.class));
    		break;	
    	case R.id.trickiestpart_take_picture:
    		startActivity(new Intent(this, TrickiestPartTakePicture.class));
    		break;
    	case R.id.trickiestpart_ack:
    		startActivity(new Intent(this, Ack.class));
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	}		
	}
}
