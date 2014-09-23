package edu.neu.madcourse.deborahho.dictionary;

import edu.neu.madcourse.deborahho.R;
import android.os.Bundle; // A mapping from String values to various Parcelable types.
import android.app.Activity; // Required to create an activity.
import android.view.Menu; // Interface for managing the items in a menu.
import android.view.MenuInflater;

public class Dictionary extends Activity { // all classes extends activity
        @Override
        protected void onCreate(Bundle savedInstanceState) { // Create an activity/ screen
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dictionary); // display activity textbox  when app starts (Main)
        }
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
        	super.onCreateOptionsMenu(menu);    
        	MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
                return true;
        }
}
