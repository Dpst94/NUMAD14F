package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BananaGame extends Activity implements OnClickListener {
	private static final String TAG = "Bananagrams" ;
	private CountDownTimer countDownTimer;
	public TextView timerText; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bananagame);
        
       // Set up click listeners for all the buttons
        View pauseButton = findViewById(R.id.banana_pause_button);
        pauseButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.banana_quit_button);
        exitButton.setOnClickListener(this);
        
        timerText = (TextView) this.findViewById(R.id.banana_timer);
        countDownTimer = new CountDownTimer(60000, 1000) {

			@Override
			public void onFinish() {
				timerText.setText("Finished!");
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timerText.setText(" " + millisUntilFinished/1000);
				
			}
        	
        }.start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sudoku_settings, menu);
        return true;
    }
    
	@Override
	public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.banana_quit_button:
    		finish();
    		break;
    	}
		
	}

}
