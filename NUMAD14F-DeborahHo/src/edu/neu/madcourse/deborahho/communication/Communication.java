package edu.neu.madcourse.deborahho.communication;

import edu.neu.madcourse.deborahho.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.mhealth.api.KeyValueAPI;

public class Communication extends Activity implements OnClickListener{
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	static final String TAG = "GCM_Communication";
	TextView mDisplay;
	EditText mMessage;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	String regid;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication);
        
		//mDisplay = (TextView) findViewById(R.id.communication_display);
		//mMessage = (EditText) findViewById(R.id.communication_edit_message);
		gcm = GoogleCloudMessaging.getInstance(this);
		context = getApplicationContext();
        
        View registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
        View connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(this);
        View sendDataButton = findViewById(R.id.send_data_button);
        sendDataButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this); 
	}	
	
    @Override
    public void onClick(View v) {
    	switch (v.getId()) {
    	case R.id.register_button:
    		//GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
    		break;
    	case R.id.exit_button:
    		finish();
    		break;
    	}
    }

}
