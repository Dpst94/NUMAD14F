package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaGame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	
	private final BananaGame game;
	
	public BananaPuzzleView(Context context) {
		super(context);
		this.game = (BananaGame) context;
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
	      width = w / 15f;
	      height = h / 15f;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	   }
	   
	   @Override
	   protected void onDraw(Canvas canvas) {
		   // Draw the background
		   Paint background = new Paint();
		   background.setColor(getResources().getColor(R.color.puzzle_background));
		   canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		   
		   // Define colors for the grid lines
		   Paint dark = new Paint();
		   dark.setColor(getResources().getColor(R.color.puzzle_dark));
		   
		   Paint hilite = new Paint();
		   hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		   
		   Paint light = new Paint();
		   light.setColor(getResources().getColor(R.color.puzzle_light));
		   
		   // Draw the grid lines
		   for (int i=0; i<15; i++) {
			   canvas.drawLine(0, i*height, getWidth(), i*height, light);
			   canvas.drawLine(0, i*height + 1, getWidth(), i*height + 1, hilite);
			   canvas.drawLine(i*width, 0, i*width, getHeight(), light);
			   canvas.drawLine(i*width + 1, 0, i*width +1, getHeight(), hilite);
			   
		   }
		   
	   }
	   
	   private void getRect(int x, int y, Rect rect) {
		  rect.set((int) (x * width), (int) (y * height), (int) (x
		            * width + width), (int) (y * height + height));
	   }	
	   
	   @Override
	   protected void onFinishInflate() {
	       // TODO Auto-generated method stub
	       super.onFinishInflate();
	   } 
}
