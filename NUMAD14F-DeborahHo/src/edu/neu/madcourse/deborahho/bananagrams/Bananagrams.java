package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.sudoku.SudokuAbout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class Bananagrams extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bananagrams);
        
       // Set up click listeners for all the buttons
        View continueButton = findViewById(R.id.banana_continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.banana_new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.banana_about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sudoku_settings, menu);
        return true;
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.banana_about_button:
    		Intent i = new Intent(this, SudokuAbout.class);
    		startActivity(i);
    		break;
    	case R.id.banana_new_button:
    		startNewGame();
    		//openNewGameDialog();
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.banana_continue_button:
    		//startGame(Game.DIFFICULTY_CONTINUE);
    		break;
    	}
    }
    
    private static final String TAG = "Bananagrams";
    
    private void startNewGame() {
    	Log.d(TAG, "Start Game");
    	Intent intent = new Intent(Bananagrams.this, BananaGame.class);
    	startActivity(intent);
    	
    }
    
}
