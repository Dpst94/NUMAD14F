package edu.neu.madcourse.deborahho.twoplayerwordgame;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaAcknowledgements;
import edu.neu.madcourse.deborahho.bananagrams.BananaGame;
import edu.neu.madcourse.deborahho.bananagrams.BananaInstructions;
import edu.neu.madcourse.deborahho.bananagrams.Bananagrams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class TwoPlayerWordGame extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twoplayerwordgame);
        
       // Set up click listeners for all the buttons
        View newButton = findViewById(R.id.twoplayer_new_button);
        newButton.setOnClickListener(this);
        View highScoresButton = findViewById(R.id.highscores_button);
        highScoresButton.setOnClickListener(this);
        //View aboutButton = findViewById(R.id.banana_about_button);
        //aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View acknowledgements_button = findViewById(R.id.twoplayer_acknowledgements_button);
        acknowledgements_button.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//    	case R.id.twoplayer_about_button:
//    		Intent i = new Intent(this, BananaInstructions.class);
//    		startActivity(i);
//    		break;
    	case R.id.twoplayer_new_button:
    		Intent l = new Intent(this, TwoPlayerRegister.class);
    		startActivity(l);
    		//startNewGame();
    		break;
    	case R.id.highscores_button:
    		//Intent k = new Intent(this, TwoPlayerHighScores.class);
    		//startActivity(k);
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.twoplayer_acknowledgements_button:
    		Intent j = new Intent(this, BananaAcknowledgements.class);
    		startActivity(j);
    		break;
    	}
		
	}
	
	private static final String TAG = "Two Player Word Game";
	
	private void startNewGame() {
	    	Log.d(TAG, "Start Game");
	    	Intent intent = new Intent(TwoPlayerWordGame.this, BananaGame.class);
	    	startActivity(intent);	
	    }

}
