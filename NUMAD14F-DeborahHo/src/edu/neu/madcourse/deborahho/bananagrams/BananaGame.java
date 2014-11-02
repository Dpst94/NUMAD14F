package edu.neu.madcourse.deborahho.bananagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaKeypad;
import edu.neu.madcourse.deborahho.bananagrams.Music;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class BananaGame extends Activity implements OnClickListener {
	private static final String TAG = "Bananagrams" ;
	private CountDownTimerPausable countDownTimer;
	public TextView timerText; 
	public TextView score; 
	public int points;
	private BananaPuzzleView bananaPuzzleView;
	public static int nrOfColumns = 8;
	public String lettersToUse[] = new String[nrOfColumns*nrOfColumns];
	
	private String puzzle[] = new String[nrOfColumns * nrOfColumns];
	private String startLetters;
	
	final String alphabet = "AAAAAAAAAAAAABBBCCCDDDDDDEEEEEEEEEEEEEEEEEEFFFGGGGHHHIIIIIIIIIIIIJJKKLLLLLMMMNNNNNNNNOOOOOOOOOOOPPPQQRRRRRRRRRSSSSSSTTTTTTTTTUUUUUUVVVWWWXXYYYZZ";
	Context context;
//	InputStream in;
//	BufferedReader reader;
	
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
		
        context = getApplicationContext();
        timerText = (TextView) this.findViewById(R.id.banana_timer);
        score = (TextView) this.findViewById(R.id.banana_score);
        score.setText("Score: 0");
        countDownTimer = new CountDownTimerPausable(90000, 1000) {

			@Override
			public void onFinish() {
				//timerText.setText("Finished!");
				Intent i = new Intent(BananaGame.this, BananaFinish.class);
	    		startActivity(i);
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timerText.setText(" " + String.format("%d:%d", 
	                    TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
	                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
	                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
				
				 if((TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished)==0) &&
		                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
		                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))==5)
		            {

		            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		            // Vibrate for 500 milliseconds
		            //v.vibrate(5000);
		            }
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
    	case R.id.banana_pause_button:
    		Intent i = new Intent(this, BananaPause.class);
    		startActivity(i);
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
	
	protected boolean setTileIfValid(int x, int y, int prevX, int prevY, String value) {
		if((x != prevX && y!= prevY) && y*nrOfColumns+x<nrOfColumns*(nrOfColumns-2)) {
			setTile(x, y, value);
			points = 0;
			findWordsOnBoard();
			Music.playOnce(this, R.raw.beep);
			
			// Erase used letter and get a new letter		
			if(prevY*nrOfColumns+prevX> nrOfColumns*(nrOfColumns-2)-1) {
				setTile(prevX, prevY, Character.toString(getNewLetter()));
			} else {
				setTile(prevX, prevY, " ");
			}
			
			score.setText("Score: " + points); 
			return true;
	
		} else {
			Toast.makeText(context, "Invalid move!", Toast.LENGTH_LONG).show();
			return false;
			
		}
		
		
		
	}	
	
	protected void showKeypad() {
		Dialog v = new BananaKeypad(this, puzzle, bananaPuzzleView);
		v.show();
	}
	
	public void findWordsOnBoard() {
		int horizontalWordLength = 0;
		int verticalWordLength = 0;
		String horizontalWord = "";
		String verticalWord = "";
		
		
		for (int row=0; row<nrOfColumns-2; row++) {
			for (int column=0; column<nrOfColumns; column++) {
				
				String hLetter = puzzle[row*nrOfColumns+column];
				String vLetter = puzzle[column*nrOfColumns+row];
				//Log.d(TAG, "check letter: " + hLetter + " vletter: " + vLetter);
				if(horizontalWordLength>2 && hLetter.equals(" ")) {
					//Check word
					Log.d(TAG, "check word: " + horizontalWord);
					if(lookUpWord(horizontalWord)) {
						points += horizontalWordLength;
						horizontalWordLength = 0;
						horizontalWord = "";
												
						Log.d(TAG, "word is approved");
					}
	
				}else if(hLetter != " ") {
					horizontalWordLength++;
					horizontalWord += hLetter;
				}				
				if(verticalWordLength>2 && vLetter.equals(" ")) {
					Log.d(TAG, "check word: " + verticalWord);
					//Check word
					if(lookUpWord(verticalWord)) {
						points += verticalWordLength;
						verticalWordLength = 0;
						verticalWord = "";
						// Get points
						
						Log.d(TAG, "word is approved");
					}
				}else if(vLetter != " ") {
					//Log.d(TAG, "vletter++ " + vLetter);
					verticalWordLength++;
					verticalWord += vLetter;
				}

			}	
			horizontalWordLength = 0;
			verticalWordLength = 0;
			horizontalWord = "";
			verticalWord = "";
		}
	}
	
	public boolean lookUpWord(String word) {
//		BloomFilter<String> bloomFilter;
//	    double falsePositiveProb = 0.01;
//	    int expectedNrOfElements = 17000;
//	    
//	    word = word.toLowerCase();
//	    char firstLetter = word.charAt(0);
//	    
//	    int resourceFile = GetResourceFile(firstLetter);
//			
//		bloomFilter = new BloomFilter<String>(falsePositiveProb, expectedNrOfElements);
//		InputStream in = getResources().openRawResource(resourceFile);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			
//		try {
//			String line;
//			while ((line = reader.readLine()) != null) {
//				bloomFilter.add(line);
//		}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		//if (bloomFilter.contains(word)) {
		
	    word = word.toLowerCase();
	    char firstLetter = word.charAt(0);
	    
	    int resourceFile = GetResourceFile(firstLetter);
		InputStream in = getResources().openRawResource(resourceFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		
		try {
			while ((line = reader.readLine()) != null) {
				if(line.equals(word)) {
					Log.d(TAG, "Found word: " + word);
					
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return false;
		
	}
	
	int GetResourceFile(char firstLetter) {
		int resourceFile = 0;
		switch (firstLetter) {
		case 'a':
			resourceFile = R.raw.a;
			break;
		case 'b':
			resourceFile = R.raw.b;
			break;
		case 'c':
			resourceFile = R.raw.c;
			break;
		case 'd':
			resourceFile = R.raw.d;
			break;
		case 'e':
			resourceFile = R.raw.e;
			break;
		case 'f':
			resourceFile = R.raw.f;
			break;
		case 'g':
			resourceFile = R.raw.g;
			break;
		case 'h':
			resourceFile = R.raw.h;
			break;
		case 'i':
			resourceFile = R.raw.i;
			break;
		case 'j':
			resourceFile = R.raw.j;
			break;
		case 'k':
			resourceFile = R.raw.k;
			break;
		case 'l':
			resourceFile = R.raw.l;
			break;
		case 'm':
			resourceFile = R.raw.m;
			break;
		case 'n':
			resourceFile = R.raw.n;
			break;
		case 'o':
			resourceFile = R.raw.o;
			break;
		case 'p':
			resourceFile = R.raw.p;
			break;
		case 'q':
			resourceFile = R.raw.q;
			break;
		case 'r':
			resourceFile = R.raw.r;
			break;
		case 's':
			resourceFile = R.raw.s;
			break;
		case 't':
			resourceFile = R.raw.t;
			break;
		case 'u':
			resourceFile = R.raw.u;
			break;
		case 'v':
			resourceFile = R.raw.v;
			break;
		case 'w':
			resourceFile = R.raw.w;
			break;
		case 'x':
			resourceFile = R.raw.x;
			break;
		case 'y':
			resourceFile = R.raw.y;
			break;
		case 'z':
			resourceFile = R.raw.z;
			break;
		}
		return resourceFile;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.banana);
		countDownTimer.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause" );
		Music.stop(this);
		countDownTimer.pause();
		
		// Save the current puzzle
		getPreferences(MODE_PRIVATE).edit().commit();
	}

}
