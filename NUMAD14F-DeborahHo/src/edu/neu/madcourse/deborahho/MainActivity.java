package edu.neu.madcourse.deborahho;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.sudoku.Game;
import edu.neu.madcourse.deborahho.sudoku.Sudoku;
import edu.neu.madcourse.deborahho.dictionary.*;

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

public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    	}
    }
    
}

