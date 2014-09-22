package edu.neu.madcourse.deborahho.sudoku;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.sudoku.Game;
import edu.neu.madcourse.deborahho.About;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class Sudoku extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku);
        
       // Set up click listeners for all the buttons
        View continueButton = findViewById(R.id.sudoku_continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.sudoku_new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.sudoku_about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
     
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.sudoku_about_button:
    		Intent i = new Intent(this, SudokuAbout.class);
    		startActivity(i);
    		break;
    	case R.id.sudoku_new_button:
    		openNewGameDialog();
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.sudoku_continue_button:
    		startGame(Game.DIFFICULTY_CONTINUE);
    		break;
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.sudoku_settings:
    		startActivity(new Intent(this, Prefs.class));
    		return true;
    // More items go here (if any) ...
    }
    return false;
    }
    
    private static final String TAG = "Sudoku";
    
    private void openNewGameDialog() {
    	new AlertDialog.Builder(this)
    		.setTitle(R.string.new_game_title)
    		.setItems(R.array.difficulty,
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialoginterface, int i) {
    						startGame(i);
    					}
    				})
    				.show();
    }
    	
    private void startGame(int i) {
    	Log.d(TAG, "clicked on " + i);
    	Intent intent = new Intent(Sudoku.this, Game.class);
    	intent.putExtra(Game.KEY_DIFFICULTY, i);
    	startActivity(intent);
    	
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Music.play(this, R.raw.main);
    }
    
   @Override
   protected void onPause() {
	   super.onPause();
	   Music.stop(this);
   }
}

