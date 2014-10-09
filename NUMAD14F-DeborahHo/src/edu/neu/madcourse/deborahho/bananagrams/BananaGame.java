package edu.neu.madcourse.deborahho.bananagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaKeypad;
import edu.neu.madcourse.deborahho.bananagrams.BananaPuzzleView;
import edu.neu.madcourse.deborahho.dictionary.Dictionary;
import edu.neu.madcourse.deborahho.dictionary.BloomFilter;
import edu.neu.madcourse.deborahho.dictionary.Music;
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
		Music.play(this, R.raw.beep);
		findWordsOnBoard();
		
		// Erase used letter and get a new letter	
		for(int i=lettersToUse.length-nrOfColumns*2; i < lettersToUse.length; i++) {
			if (lettersToUse[i].equals(value)) {
				lettersToUse[i] = Character.toString(getNewLetter());
				break;
			}
			
		}
		
		// do other stuff as erasing tile etc. 
		return true;
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
						horizontalWordLength = 0;
						horizontalWord = "";
						// Get points
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
		BloomFilter<String> bloomFilter;
	    double falsePositiveProb = 0.01;
	    int expectedNrOfElements = 17000;
	    
	    word = word.toLowerCase();
	    char firstLetter = word.charAt(0);
	    
	    int resourceFile = GetResourceFile(firstLetter);
			
		bloomFilter = new BloomFilter<String>(falsePositiveProb, expectedNrOfElements);
		InputStream in = getResources().openRawResource(resourceFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				bloomFilter.add(line);
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (bloomFilter.contains(word)) {
			Log.d(TAG, "Found word: " + word);
			
			return true;
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
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause" );
		
		// Save the current puzzle
		getPreferences(MODE_PRIVATE).edit().commit();
	}

}
