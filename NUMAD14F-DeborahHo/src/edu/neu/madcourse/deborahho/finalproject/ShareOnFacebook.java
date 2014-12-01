package edu.neu.madcourse.deborahho.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class ShareOnFacebook extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_share_facebook);
		
		View logInButton = findViewById(R.id.login_facebook_button);
        logInButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.login_facebook_button:
//    		Intent i = new Intent(this, Game.class);//TutorialFriends.class);
//    		startActivity(i);
    		break;
		}
    		
		
	}
}
