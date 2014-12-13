package edu.neu.madcourse.deborahho.finalproject;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.deborahho.R;

public class ChallengeFriends extends Activity implements OnClickListener{
	private UiLifecycleHelper uiHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, null);
	    uiHelper.onCreate(savedInstanceState);		

		setContentView(R.layout.finalproject_challenge_friends);
	    
	    View logInButton = findViewById(R.id.login_facebook_button);
        logInButton.setOnClickListener(this);
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
    	case R.id.login_facebook_button:
    		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
            .setLink("https://developers.facebook.com/android").setApplicationName("Crunchy")
            .build();
    uiHelper.trackPendingDialogCall(shareDialog.present());
    		break;
    	case R.id.finalproject_back_button:
    		finish();
    		break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
}
