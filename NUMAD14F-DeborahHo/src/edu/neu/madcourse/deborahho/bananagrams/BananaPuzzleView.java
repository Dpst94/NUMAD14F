package edu.neu.madcourse.deborahho.bananagrams;

import edu.neu.madcourse.deborahho.R;
import edu.neu.madcourse.deborahho.bananagrams.BananaGame;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
	public float width_field;
	public float height_field;
	public float nrOfColumns = 8;
	public float nrOfRows = 8;
	
	private final BananaGame game;
	
	public BananaPuzzleView(Context context) {
		super(context);
		this.game = (BananaGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
    
		      // ...
		      //setId(ID); 
		}	
	
	public BananaPuzzleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.game = (BananaGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);		
	}
	
	public BananaPuzzleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.game = (BananaGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);		
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
	      select(bundle.getInt(SELX), bundle.getInt(SELY));
	      super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
	   }

	   @Override
	   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      width = w / nrOfColumns;
	      height = h / nrOfColumns;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	   }
	   
	   @Override
	   protected void onDraw(Canvas canvas) {
		   width_field = getWidth();
		   height_field = getHeight();
		   
		   // Draw the background
		   Paint background = new Paint();
		   background.setColor(getResources().getColor(R.color.puzzle_background));
		   canvas.drawRect(0, 0, width_field, height_field, background);
		   
		   // Define colors for the grid lines
		   Paint dark = new Paint();
		   dark.setColor(getResources().getColor(R.color.puzzle_dark));
		   
		   Paint hilite = new Paint();
		   hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		   
		   Paint light = new Paint();
		   light.setColor(getResources().getColor(R.color.puzzle_light));
		   
		   // Draw the grid lines
		   for (int i=0; i<nrOfColumns; i++) {
			   canvas.drawLine(0, i*height, width_field, i*height, dark);
			   canvas.drawLine(0, i*height + 1, width_field, i*height + 1, hilite);
			   canvas.drawLine(i*width, 0, i*width, height_field, dark);
			   canvas.drawLine(i*width + 1, 0, i*width +1, height_field, hilite);
			   
		   }
		   
		   dark.setStyle(Paint.Style.STROKE);
		   canvas.drawLine(0, (nrOfColumns-2)*height, width_field, (nrOfColumns-2)*height, dark);
		      
		   // Draw the numbers...
		   // Define color and style for numbers
		   Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		   foreground.setColor(getResources().getColor(
				   R.color.puzzle_foreground));
		   foreground.setStyle(Style.FILL);
		   foreground.setTextSize(height * 0.75f);
		   foreground.setTextScaleX(width / height);
		   foreground.setTextAlign(Paint.Align.CENTER);	
		   
		   
		   // Draw the number in the center of the tile
		   FontMetrics fm = foreground.getFontMetrics();
		   // Centering in X: use alignment (and X at midpoint)
		   float x = width / 2;
		   // Centering in Y: measure ascent/descent first
		   float y = height / 2 - (fm.ascent + fm.descent) / 2;
		   for (int i = 0; i < nrOfColumns; i++) {
		      for (int j = 0; j <  nrOfColumns; j++) {
		         canvas.drawText(this.game.getTile(i, j), i
		               * width + x, j * height + y, foreground);
		      }
		   }
		   
		      // Draw the selection...
		      Log.d(TAG, "selRect=" + selRect);
		      Paint selected = new Paint();
		      selected.setColor(getResources().getColor(
		            R.color.puzzle_selected));
		      canvas.drawRect(selRect, selected);		   
		      
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
	   
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
	      if (event.getAction() != MotionEvent.ACTION_DOWN)
	         return super.onTouchEvent(event);

	      select((int) (event.getX() / width),
	            (int) (event.getY() / height));
	      game.showKeypad();
	      Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
	      return true;
	   }
	   
	   public void setSelectedTile(String tile) {
		   Log.d(TAG, "setSelectedTile x: " + selX + ", y " + selY);
		   if(game.setTileIfValid(selX, selY, tile)) {
			   invalidate();// may change hints   
		   }
 
	   }	   
	   
	   private void select(int x, int y) {
		      invalidate(selRect);
		      selX = Math.min(Math.max(x, 0), (int)nrOfColumns-1);
		      selY = Math.min(Math.max(y, 0), (int)nrOfColumns-1);
		      getRect(selX, selY, selRect);
		      invalidate(selRect);
	   }
	   

}
