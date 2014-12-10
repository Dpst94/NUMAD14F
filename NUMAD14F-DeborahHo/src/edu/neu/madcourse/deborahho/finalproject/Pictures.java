package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Pictures extends Activity implements OnClickListener {
	
	ImageView iView;
	TextView namePicture;
	Integer img = R.drawable.picture_correct;
	Integer img2 = R.drawable.picture_false;
	Context context;
	SharedPreferences name;
	String username;
	boolean isEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_pictures);
		
		context = this;
		
		WebView myWebView = (WebView) findViewById(R.id.webview);
		
		final SharedPreferences prefs = getGCMPreferences(this);
		
		name = getSharedPreferences("picure_model", 0);
		username = name.getString("model", "UNKNOWN");
		
		atLeastOneImgUploaded();
		
		myWebView.loadUrl("http://www.eighilaza.tk/GetPicture.php?username="+username);
		
		namePicture = (TextView) findViewById(R.id.finalproject_name_picture);
		namePicture.setText("");
		
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		}
	}

	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	
	public void atLeastOneImgUploaded() {
		if (!isOnline()) {
			Toast.makeText(context, "Failed to connect to the Internet",
					Toast.LENGTH_LONG).show();
			isEmpty = true;
			return;
		}
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				if (KeyValueAPI.isServerAvailable()) {
						if(KeyValueAPI.get("eighilaza", "eighilaza", username+"_picture").equals("yes")){
							isEmpty = false;
							return msg;
						}
						else{
							isEmpty = true;
							return msg;
						}
				} else {
					msg = "Error :" + "Backup Server is not available";
					isEmpty = true;
					return msg;
				}
			}
			@Override
			protected void onPostExecute(String msg) {	
				runOnUiThread(new Runnable() {
				    public void run() {
				    	if(isEmpty){
					        namePicture.setText(username+" did not upload any pictures");
				    	}
				    	else{
				    		namePicture.setText("Photos of "+username);
				    	}
				    }
				});	
			}
		}.execute(null, null, null);
	}
	// Checks if connected to the Internet
		public boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
			return false;
		}
}
