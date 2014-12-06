package edu.neu.madcourse.deborahho;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.sudoku.Sudoku;
import edu.neu.madcourse.deborahho.trickiestpart.TrickiestPart;
//import edu.neu.madcourse.deborahho.twoplayerwordgame.TwoPlayerWordGame;
import edu.neu.madcourse.deborahho.bananagrams.Bananagrams;
//import edu.neu.madcourse.deborahho.communication.Communication;
import edu.neu.madcourse.deborahho.dictionary.*;
import edu.neu.madcourse.deborahho.finalproject.FinalProjectDescription;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_title);
        setContentView(R.layout.activity_main);
        
       // Set up click listeners for all the buttons
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View errorButton = findViewById(R.id.error_button);
        errorButton.setOnClickListener(this);
        View sudokuButton = findViewById(R.id.sudoku_button);
        sudokuButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        
        View dictionaryButton = findViewById(R.id.dictionary_button);
        dictionaryButton.setOnClickListener(this);
        View bananagramsButton = findViewById(R.id.bananagrams_button);
        bananagramsButton.setOnClickListener(this);
        View communicationButton = findViewById(R.id.communication_button);
        communicationButton.setOnClickListener(this);
        View twoplayerButton = findViewById(R.id.twoplayer_button);
        twoplayerButton.setOnClickListener(this);
        View trickiestpartButton = findViewById(R.id.trickiestpart_button);
        trickiestpartButton.setOnClickListener(this);
        View finalprojectButton = findViewById(R.id.finalproject_button);
        finalprojectButton.setOnClickListener(this);
     
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
    	case R.id.about_button:
    		Intent i = new Intent(this, About.class);
    		startActivity(i);
    		break;
    	case R.id.sudoku_button:
    		Intent j = new Intent(this, Sudoku.class);
    		startActivity(j);
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.error_button:
    		int error = 6/0;
    		break;
    	case R.id.dictionary_button:
    		Intent k = new Intent(this, Dictionary.class);
    		startActivity(k);
    		break;
    	case R.id.bananagrams_button:
    		Intent l = new Intent(this, Bananagrams.class);
    		startActivity(l);
    		break;
    	/*case R.id.communication_button:
    		Intent m = new Intent(this, Communication.class);
    		startActivity(m);
    		break;*/
    	/*case R.id.twoplayer_button:
    		Intent n = new Intent(this, TwoPlayerWordGame.class);
    		startActivity(n);
    		break;*/
    	case R.id.trickiestpart_button:
    		Intent o = new Intent(this, TrickiestPart.class);
    		startActivity(o);
    		break;
    	case R.id.finalproject_button:
    		Intent p = new Intent(this, FinalProjectDescription.class);
    		startActivity(p);
    	}
    }
    
}

