package com.nitobi.phonegap;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.WebView;

public class PhoneGap{
	
	private static final String LOG_TAG = "PhoneGap";
	/*
	 * UUID, version and availability	
	 */
	public boolean droid = true;
	private String version = "0.1";	
    private Context mCtx;    
    private Handler mHandler;
    private WebView mAppView;    
    private GpsListener mGps;
    private NetworkListener mNetwork;
    
	public PhoneGap(Context ctx, Handler handler, WebView appView) {
        this.mCtx = ctx;
        this.mHandler = handler;
        this.mAppView = appView;
        mGps = new GpsListener(ctx);
        mNetwork = new NetworkListener(ctx);
    }
	
	public void updateAccel(){
		mHandler.post(new Runnable() {
			public void run() {
				int accelX = SensorManager.DATA_X;
				int accelY = SensorManager.DATA_Y;
				int accelZ = SensorManager.DATA_Z;
        		mAppView.loadUrl("javascript:gotAcceleration(" + accelX + ", " + accelY + "," + accelZ + ")");
			}			
		});
				
	}
	
	public void takePhoto(){
		// TO-DO: Figure out what this should do
	}
	
	public void playSound(){
		// TO-DO: Figure out what this should do
	}
	
	public void vibrate(long pattern){
        // Start the vibration
        Vibrator vibrator = (Vibrator) mCtx.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern);
	}
	
	/*
	 * Android requires a provider, since it can fall back on triangulation and other means as well as GPS	
	 */
	
	public void getLocation(final String provider){
		mHandler.post(new Runnable() {
            public void run() {
    			GeoTuple geoloc = new GeoTuple();
    			Location loc = mGps.hasLocation() ?  mGps.getLocation() : mNetwork.getLocation();
    			if (loc != null)
        		{
        			geoloc.lat = loc.getLatitude();
        			geoloc.lng = loc.getLongitude();
        			geoloc.ele = loc.getAltitude();
        		}
        		else
        		{
        			geoloc.lat = 0;
        			geoloc.lng = 0;
        			geoloc.ele = 0;
        		}
        		mAppView.loadUrl("javascript:gotLocation(" + geoloc.lat + ", " + geoloc.lng + ")");
            }
        });
	}
	
	public String outputText(){
		String test = "<p>Test</p>";
		return test;
	}
	

	public String getUuid()
	{

		TelephonyManager operator = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE);
		String uuid = operator.getDeviceId();
		return uuid;
	}
	
	public String getVersion()
	{
		return version;
	}	
	
	public boolean exists()
	{
		return true;	
	}
	

	
}
