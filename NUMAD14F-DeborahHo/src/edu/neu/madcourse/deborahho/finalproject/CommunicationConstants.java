package edu.neu.madcourse.deborahho.finalproject;

public class CommunicationConstants
{
    public static final String TAG = "GCM_Globals";
    public static final String GCM_SENDER_ID = "61305684538";
    public static final String BASE_URL = "https://android.googleapis.com/gcm/send";
    public static final String PREFS_NAME = "GCM_Communication";
    public static final String GCM_API_KEY = "AIzaSyDnbOGo3lLx1VsAWeZneJubYJzXNMODHFw";
    public static final int SIMPLE_NOTIFICATION = 22;
    public static final long GCM_TIME_TO_LIVE = 60L * 60L * 24L * 7L * 4L; // 4 Weeks
    public static int mode = 0;
    
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_ALERT_TEXT = "alertText";
	public static final String PROPERTY_TITLE_TEXT = "titleText";
	public static final String PROPERTY_CONTENT_TEXT = "contentText";
	public static final String PROPERTY_NTYPE = "nType";
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
}