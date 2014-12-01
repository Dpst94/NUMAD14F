package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class ShareOnFacebookPostWall extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_share_fb_post_wall);
		
		View postWallButton = findViewById(R.id.post_wall_facebook_button);
        postWallButton.setOnClickListener(this);
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.post_wall_facebook_button:
    		//Pop up fb post wall in screen..
//    		Intent i = new Intent(this, Game.class);//TutorialFriends.class);
//    		startActivity(i);
    		break;
    	case R.id.finalproject_back_button:
    		finish();
    		break;
		}
    		
		
	}
}
