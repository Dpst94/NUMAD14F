package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Bananagrams extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bananagrams);
        
       // Set up click listeners for all the buttons
        View newButton = findViewById(R.id.banana_new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.banana_about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        View acknowledgements_button = findViewById(R.id.banana_acknowledgements_button);
        acknowledgements_button.setOnClickListener(this);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.banana_settings, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.banana_settings:
    		startActivity(new Intent(this, Prefs.class));
    		return true;
    // More items go here (if any) ...
    }
    return false;
    }    
    
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.banana_about_button:
    		Intent i = new Intent(this, BananaInstructions.class);
    		startActivity(i);
    		break;
    	case R.id.banana_new_button:
    		startNewGame();
    		//openNewGameDialog();
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	case R.id.banana_acknowledgements_button:
    		Intent j = new Intent(this, BananaAcknowledgements.class);
    		startActivity(j);
    		break;
    	}
    }
    
    private static final String TAG = "Bananagrams";
    
    private void startNewGame() {
    	Log.d(TAG, "Start Game");
    	Intent intent = new Intent(Bananagrams.this, BananaGame.class);
    	startActivity(intent);
    	
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Music.play(this, R.raw.bananamain);
    }
    
   @Override
   protected void onPause() {
	   super.onPause();
	   Music.stop(this);
   }
    
}
