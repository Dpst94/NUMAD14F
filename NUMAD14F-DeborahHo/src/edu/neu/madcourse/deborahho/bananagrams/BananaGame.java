package edu.neu.madcourse.deborahho.bananagrams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaKeypad;
import edu.neu.madcourse.deborahho.bananagrams.BananaPuzzleView;
import edu.neu.madcourse.deborahho.sudoku.PuzzleView;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class BananaGame extends Activity implements OnClickListener {
	private static final String TAG = "Bananagrams" ;
	private CountDownTimer countDownTimer;
	public TextView timerText; 
	public TextView score; 
	private BananaPuzzleView bananaPuzzleView;
	public static int nrOfColumns = 8;
	public String lettersToUse[] = new String[nrOfColumns*nrOfColumns];
	
	private String puzzle[] = new String[nrOfColumns * nrOfColumns];
	private String startLetters;
	
	final String alphabet = "AAAAAAAAAAAAABBBCCCDDDDDDEEEEEEEEEEEEEEEEEEFFFGGGGHHHIIIIIIIIIIIIJJKKLLLLLMMMNNNNNNNNOOOOOOOOOOOPPPQQRRRRRRRRRSSSSSSTTTTTTTTTUUUUUUVVVWWWXXYYYZZ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bananagame);
		bananaPuzzleView = new BananaPuzzleView(this);
		bananaPuzzleView.requestFocus();

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
        
        startLetters = getRandomLetters();
        puzzle = fromLettersString(startLetters);
        lettersToUse = puzzle;  
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
	
	public char getNewLetter() {
		Random r = new Random();
		return alphabet.charAt(r.nextInt(alphabet.length()));
	}
	
	private String getRandomLetters() {
		String letters = "";
		for (int i = 0; i < nrOfColumns*2; i++) {
			letters += getNewLetter();
		}
		return letters;
	}
	
	static protected String[] fromLettersString(String string) {
		String[] puz = new String[nrOfColumns*nrOfColumns];
		for (int j = 0; j<puz.length-nrOfColumns*2; j++) {
			puz[j] = " ";
		}
		for (int i = puz.length-nrOfColumns*2; i < puz.length; i++) {
			puz[i] = Character.toString(string.charAt(i-nrOfColumns*(nrOfColumns-2)));
		}
		//Log.d(TAG, "puzzle string long: " + puz);
		return puz;
	}
	
	String getTile(int x, int y) {
		return puzzle[y * nrOfColumns + x];
	}
		
	void setTile(int x, int y, String value) {
		puzzle[y * nrOfColumns + x] = value;
		Log.d(TAG, "game.setTile x y: " + x + y);
		Log.d(TAG, "show puzzle: " + puzzle);
	}
	
	protected boolean setTileIfValid(int x, int y, String value) {
		setTile(x, y, value);
		
		// do other stuff as erasing tile etc. 
		return true;
	}	
	
	protected void showKeypad() {
		Dialog v = new BananaKeypad(this, puzzle, bananaPuzzleView);
		v.show();
	}

}
