package edu.neu.madcourse.deborahho.dictionary;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.dictionary.Music;
import android.os.Bundle;
import android.app.Activity; 
import android.content.Intent;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu; 
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.util.Arrays;

public class Dictionary extends Activity implements OnClickListener { 
        
	EditText wordText;
	TextView tv;
	int resourceFile; 
	String TAG = "Dictionary";
	
	int formerLengthWord = 0;
	String[] wordsFound = new String[100];
	int nrOfWords = 0;
	
	BloomFilter<String> bloomFilter;
	BloomFilter<String> bftest = null;
    double falsePositiveProb = 0.01;
    int expectedNrOfElements = 17000;

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
        
        AssetManager am = getAssets();
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = am.open("test.txt");
            ObjectInputStream ois = new ObjectInputStream(inputStream);
			bftest = (BloomFilter<String>) ois.readObject();
			ois.close();
            outputStream = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      if(bftest.contains("text")){
    	Log.d(TAG, "Found word: text");
    }
        
//        try {
//        	InputStream in_test = getResources().openRawResource(R.raw.test);
//			ObjectInputStream ois = new ObjectInputStream(in_test);
//			bftest = (BloomFilter<String>) ois.readObject();
//			ois.close();
//		} catch (StreamCorruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			//
//		}
//        if(bftest.contains("text")){
//        	Log.d(TAG, "Found word: text");
//        }
        
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
       		if (word.length() == 1 && formerLengthWord < word.length()){
       			char firstLetter = word.charAt(0);
       			resourceFile = GetResourceFile(firstLetter);
       			
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
    		} else if (word.length() > 2){
    			Log.d(TAG, "Found word: " + word);
    			if (!Arrays.asList(wordsFound).contains(word)) {
    				LookUpWord(word);
    			}
    		}
       		
       		formerLengthWord = word.length();
    			

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
			tv.setText(null);
			wordText.setText("");
			wordsFound = null;
			nrOfWords = 0;
			break;
		case R.id.dict_return_button:
			Music.stop(this);
			finish();
			break;
		case R.id.dict_acknowledgements_button:
			Intent i = new Intent(this, DictAcknowledgements.class);
			startActivity(i);
			break;
		}
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

	public void LookUpWord(String text) {
   		if (bloomFilter.contains(text)) {
			Log.d(TAG, "Found word: " + text);
			wordsFound[nrOfWords] = text;
			nrOfWords++;
			tv.append("\n" + text);
			Music.play(this, R.raw.beep);
		}
	}

    @Override
    protected void onResume() {
    	super.onResume();
    }
    
   @Override
   protected void onPause() {
	   super.onPause();
   }

}
