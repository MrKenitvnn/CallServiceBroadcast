package com.sktelink.sk00700.callservices;

import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;

public class MyCallReceiver extends BroadcastReceiver {
	
	private static PendingIntent mAlarmIntent;
	private static AlarmManager manager;
	private static DataUtils dataUtils;
	private static CallUtils callUtils;
	
	private int mHourUpdate;
	private String mUrlPatterns;
	private String mUrlTime;
	private String[] mArrPatterns;
	private List<String> listPatterns;
    private Time time;
    private Date callDate;
	
	/*
	 * TODO: on receive
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(">>> trams <<<", "1. onReceive --START");
		try {
			// very important
			dataUtils = new DataUtils(context);
			callUtils = new CallUtils(context);
			
			// get time
	        time = new Time();
	        time.setToNow();
	        callDate = new Date(Long.valueOf(Long.toString(time.toMillis(false))));
			
			// check is in test
			if (dataUtils.isInTest()) {
				// 1. never check hour
				// 2. check server enable to set data
				// 3. update
				doTaskUpdate(context, dataUtils.isEnableServer());
			
			} else {
				// get hour update
				mHourUpdate = dataUtils.getTheHourUpdate();
				// 1. check the hour for update call log, contact
				// 2. check server enable to set data
				// 3. update
				if (callDate.getHours() == mHourUpdate) {
					doTaskUpdate(context, dataUtils.isEnableServer());
				}
			}
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
		Log.d(">>> trams <<<", "2. onReceive --OKE");
	}
	
	
	/*
	 * TODO: setup data
	 */
	
	private void doTaskUpdate (Context context, boolean isEnableServer) {
		try {
			if (isEnableServer && MyUtils.isOnline(context)) {
				new AsyncDataFromServer().execute(context);
			} else {
				// get list patterns
				listPatterns = dataUtils.getListPattern();
				
				// call update
				myUpdate(context);
			}
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
	/*
	 * TODO: method update
	 */
	
	private void myUpdate(Context context){
		try{
			// update contact
			callUtils.updateAllContact(context.getContentResolver(),listPatterns ,dataUtils.getTargetPattern());
			
			// update call log
			callUtils.updateCallLog(context, listPatterns, dataUtils.getTargetPattern());
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
	/*
	 * TODO asyncTask get data from server
	 */
	
	private class AsyncDataFromServer extends
		AsyncTask<Context, Void, Void> {

		Context context;
		
		@Override
		protected Void doInBackground(Context... params) {
			context = params[0];
			
			try {
				mUrlPatterns = dataUtils.getUrlPatterns();
				mUrlTime = dataUtils.getUrlTime();
				
				mArrPatterns = MyJsonHandler.getPattern(mUrlPatterns);
				mHourUpdate = MyJsonHandler.getTime(mUrlTime).getHour();
			} catch (Exception ex) {
				Log.d(">>> trams <<<", Log.getStackTraceString(ex));
			}
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) {
			try{
				// set data 
				dataUtils.setListPattern(mArrPatterns);
				dataUtils.setTheHourUpdate(mHourUpdate);
				
				// get list patterns
				listPatterns = dataUtils.getListPattern();
				// get hour update
				mHourUpdate = dataUtils.getTheHourUpdate();
				
				// call update
				myUpdate(context);
			} catch (Exception ex) {
				Log.d(">>> trams <<<", Log.getStackTraceString(ex));
			}
		}
	}// end-async AsyncDataFromServer


	/*
	 * TODO: start service
	 */
	public static void startService (Context context) {
		try {
			// very important
			dataUtils = new DataUtils(context);
			
			if (dataUtils.isFirstRun()) {
				// create alarm receiver
				Intent launchIntent = new Intent(context, MyCallReceiver.class);
				mAlarmIntent = PendingIntent.getBroadcast(context, 0, launchIntent, 0);
		
				// create receiver
				manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
				// start receiver
				manager.setInexactRepeating(AlarmManager.RTC,
						SystemClock.elapsedRealtime(), dataUtils.getTimeCallBack(), mAlarmIntent);
				dataUtils.setFirstRun();
			}
			
		} catch (Exception ex) {
				Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
}
