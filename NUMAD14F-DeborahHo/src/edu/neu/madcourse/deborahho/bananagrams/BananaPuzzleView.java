package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.bananagrams.Game;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

public class BananaPuzzleView extends View{
	private static final String TAG = "Bananagram";
	
	private static final String SELX = "selX"; 
	private static final String SELY = "selY";
	private static final String VIEW_STATE = "viewState";
	
	private float width;    // width of one tile
	private float height;   // height of one tile
	private int selX;       // X index of selection
	private int selY;       // Y index of selection
	private final Rect selRect = new Rect();	
	
	private final Game game;
	
	public BananaPuzzleView(Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		      
		      // ...
		      //setId(ID); 
		}	
	
	   @Override
	   protected Parcelable onSaveInstanceState() { 
	      Parcelable p = super.onSaveInstanceState();
	      Log.d(TAG, "onSaveInstanceState");
	      Bundle bundle = new Bundle();
	      bundle.putInt(SELX, selX);
	      bundle.putInt(SELY, selY);
	      bundle.putParcelable(VIEW_STATE, p);
	      return bundle;
	   }
	   
	   @Override
	   protected void onRestoreInstanceState(Parcelable state) { 
	      Log.d(TAG, "onRestoreInstanceState");
	      Bundle bundle = (Bundle) state;
	      //select(bundle.getInt(SELX), bundle.getInt(SELY));
	      super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
	   }

	   @Override
	   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      width = w / 20f;
	      height = w / 20f;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	   }
	   
	   private void getRect(int x, int y, Rect rect) {
		  rect.set((int) (x * width), (int) (y * height), (int) (x
		            * width + width), (int) (y * height + height));
	   }	
	   
	   
}
