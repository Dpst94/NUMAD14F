/*package edu.neu.madcourse.deborahho.twoplayerwordgame;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TwoPlayerSelectGame extends Activity implements OnClickListener{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twoplayer_select_game);
        
       // Set up click listeners for all the buttons
        View newButton = findViewById(R.id.single_player_button);
        newButton.setOnClickListener(this);
        View highScoresButton = findViewById(R.id.multi_player_button);
        highScoresButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.single_player_button:			
    		Intent i = new Intent(this, edu.neu.madcourse.deborahho.bananagrams.BananaGame.class);
    		startActivity(i);
			break;	
    	case R.id.multi_player_button:
    		Intent j = new Intent(this, TwoPlayerRegister.class);
    		startActivity(j);
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	}
		
	}

}*/
