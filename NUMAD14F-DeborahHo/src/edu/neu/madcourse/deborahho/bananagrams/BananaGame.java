package edu.neu.madcourse.deborahho.bananagrams;

import java.util.concurrent.TimeUnit;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BananaGame extends Activity implements OnClickListener {
	private static final String TAG = "Bananagrams" ;
	private CountDownTimer countDownTimer;
	public TextView timerText; 
	public TextView score; 
	
	private char puzzle[] = new char[8 * 8];

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
        score = (TextView) this.findViewById(R.id.banana_score);
        score.setText("Score: 0	");
        countDownTimer = new CountDownTimer(90000, 1000) {

			@Override
			public void onFinish() {
				timerText.setText("Finished!");
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timerText.setText(" " + String.format("%d:%d", 
	                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
	                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
	                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
				
			}
        	
        }.start();
        
        puzzle = fromLettersString(startLetters);
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
	
	private final String startLetters = "aeebtxlpowidgrtf";
	
	static protected char[] fromLettersString(String string) {
		char[] puz = new char[64];
		for (int i = puz.length-16; i < puz.length; i++) {
			puz[i] = string.charAt(i-48);
		}
		Log.d(TAG, "puzzle string long: " + puz);
		return puz;
	}
	
	private char getTile(int x, int y) {
		return puzzle[y * 8 + x];
	}
		
	private void setTile(int x, int y, char value) {
		puzzle[y * 8 + x] = value;
	}
	
	protected String getTileString(int x, int y) {
		char v = getTile(x, y);
		if (v == 0)
			return "" ;
		else
			return String.valueOf(v);
	}	

}
