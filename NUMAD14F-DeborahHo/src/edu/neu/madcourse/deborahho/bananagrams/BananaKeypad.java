package edu.neu.madcourse.deborahho.bananagrams;

import java.util.Arrays;

import edu.neu.madcourse.deborahho.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BananaKeypad extends Dialog {
	
	protected static final String TAG = "Bananagrams";
	
	private final View keys[] = new View[26];
	private View keypad;
	
	private final String lettersToUse[];

	private final BananaPuzzleView bananaView;
	
	public BananaKeypad(Context context, String[] lettersToUse, BananaPuzzleView bananaView) {
		super(context);
		this.lettersToUse = lettersToUse;
		this.bananaView = bananaView;
		Log.d(TAG, "show my view: " + bananaView);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "show my view in oncreate: " + bananaView);
		setTitle(R.string.keypad_title);
		setContentView(R.layout.banana_keypad);
		findViews();
		Log.d(TAG, "lettersToUse" + lettersToUse);
		for (int i=0; i<26; i++) {
			keys[i].setVisibility(View.INVISIBLE);
			String letter = Character.toString((char)(i+65));
			Log.d(TAG, "letters to use " + Arrays.asList(lettersToUse));
			if(Arrays.asList(lettersToUse).contains(letter)) {
				Log.d(TAG, "show letters");
				keys[i].setVisibility(View.VISIBLE);	
			}
		}

		setListeners();
	}
	
	private void findViews() {
		keypad = findViewById(R.id.keypad);
		keys[0] = findViewById(R.id.keypad_a);
		keys[1] = findViewById(R.id.keypad_b);
		keys[2] = findViewById(R.id.keypad_c);
		keys[3] = findViewById(R.id.keypad_d);
		keys[4] = findViewById(R.id.keypad_e);
		keys[5] = findViewById(R.id.keypad_f);
		keys[6] = findViewById(R.id.keypad_g);
		keys[7] = findViewById(R.id.keypad_h);
		keys[8] = findViewById(R.id.keypad_i);		
		keys[9] = findViewById(R.id.keypad_j);
		keys[10] = findViewById(R.id.keypad_k);
		keys[11] = findViewById(R.id.keypad_l);
		keys[12] = findViewById(R.id.keypad_m);
		keys[13] = findViewById(R.id.keypad_n);
		keys[14] = findViewById(R.id.keypad_o);
		keys[15] = findViewById(R.id.keypad_p);
		keys[16] = findViewById(R.id.keypad_q);
		keys[17] = findViewById(R.id.keypad_r);
		keys[18] = findViewById(R.id.keypad_s);	
		keys[19] = findViewById(R.id.keypad_t);
		keys[20] = findViewById(R.id.keypad_u);
		keys[21] = findViewById(R.id.keypad_v);
		keys[22] = findViewById(R.id.keypad_w);
		keys[23] = findViewById(R.id.keypad_x);
		keys[24] = findViewById(R.id.keypad_y);
		keys[25] = findViewById(R.id.keypad_z);	
	}
	
	// Error here!! In returnResult(c)....
	private void setListeners() {
		for (int i = 0; i < keys.length; i++) {
			final int t = i + 1;
			keys[i].setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					String c = Character.toString((char)(t+64));
					Log.d(TAG, "place " + c + v);
					returnResult(c);
				}});
		}
		keypad.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				returnResult(" ");
			}});
	}
	
	private void returnResult(String tile) {
		Log.d(TAG, "place this tile: " + tile);
		bananaView.setSelectedTile(tile);
		dismiss();
	}

}
