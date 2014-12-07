package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Pictures extends Activity implements OnClickListener {
	
	ImageView iView;
	TextView namePicture;
	Integer img = R.drawable.picture_correct;
	Integer img2 = R.drawable.picture_false;
	
	SharedPreferences name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_pictures);
		
		WebView myWebView = (WebView) findViewById(R.id.webview);
		
		final SharedPreferences prefs = getGCMPreferences(this);
		
		name = getSharedPreferences("picure_model", 0);  
		myWebView.loadUrl("http://www.eighilaza.tk/GetPicture.php?username="+name.getString("model", "UNKNOWN"));
		
		namePicture = (TextView) findViewById(R.id.finalproject_name_picture);
		namePicture.setText("Pictures of Elyes");
		
        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		/*case R.id.finalproject_next_button:
			iView.setImageResource(img2);
			break;
		case R.id.finalproject_previous_button:
			iView.setImageResource(img);*/
		}
	}

	private  SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(Main.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
}
