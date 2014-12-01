package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class ShareOnFacebookLogIn extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_share_fb_login);
		
		View logInButton = findViewById(R.id.login_facebook_button);
        logInButton.setOnClickListener(this);
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.login_facebook_button:
    		//Pop up fb log in screen..
    		Intent i = new Intent(this, ShareOnFacebookPostWall.class);//TutorialFriends.class);
    		startActivity(i);
    		break;
    	case R.id.finalproject_back_button:
    		finish();
    		break;
		}
    		
		
	}
}
