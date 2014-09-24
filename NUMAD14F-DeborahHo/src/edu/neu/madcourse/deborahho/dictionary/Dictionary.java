package edu.neu.madcourse.deborahho.dictionary;

import edu.neu.madcourse.deborahho.R;
import android.os.Bundle;
import android.app.Activity; 
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu; 
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;


public class Dictionary extends Activity implements OnClickListener { 
        
	EditText wordText;
	TextView tv;

	@Override    
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary); 
        
		View clear_button = findViewById(R.id.dict_clear_button);
        clear_button.setOnClickListener(this);
        View return_button = findViewById(R.id.dict_return_button);
        return_button.setOnClickListener(this);
        View acknowledgements_button = findViewById(R.id.dict_acknowledgements_button);
        acknowledgements_button.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.found_words);
        wordText = (EditText) findViewById(R.id.text_field);
        wordText.addTextChangedListener(new TextWatcher () {
        	
        	@Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }
        	
        	@Override
        	public void afterTextChanged(Editable s) {
        		String word = wordText.getText().toString();
        		tv.append("\n"+word);
        	}
        });
	}
        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    
		super.onCreateOptionsMenu(menu);    

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);      
		return true;
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dict_clear_button:
			break;
		case R.id.dict_return_button:
			finish();
			break;
		case R.id.dict_acknowledgements_button:
			Intent i = new Intent(this, DictAcknowledgements.class);
			startActivity(i);
			break;
		}
	}
	

}
