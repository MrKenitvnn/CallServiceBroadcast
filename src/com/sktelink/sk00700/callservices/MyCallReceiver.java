package com.sktelink.sk00700.callservices;

import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class MyCallReceiver extends WakefulBroadcastReceiver {
	
	private static PendingIntent mAlarmIntent;
	private static AlarmManager manager;
	private static DataUtils dataUtils;
	private static CallUtils callUtils;
	
	private int mHourUpdate;
	private String mUrlPatterns;
	private String mUrlTime;
	private String[] mArrPatterns;
	private List<String> listPatterns;
    private static Time time;
    private Date callDate;
	
	/*
	 * TODO: on receive
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Log.d(">>> trams <<<", "1. onReceive --START");
			// very important
			dataUtils = new DataUtils(context);
			callUtils = new CallUtils(context);
			
			// get time
	        time = new Time();
	        time.setToNow();
	        callDate = new Date(Long.valueOf(Long.toString(time.toMillis(false))));
			
			// check is in test
			if (dataUtils.isInTest()) {
				// never check hour
				doTaskUpdate(context, dataUtils.isEnableServer());
			
			} else {
				// get hour update
				mHourUpdate = dataUtils.getTheHourUpdate();
				// --update
				if (callDate.getHours() == mHourUpdate) {
					doTaskUpdate(context, dataUtils.isEnableServer());
				}
			}
			Log.d(">>> trams <<<", "2. onReceive --OKE");
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
	
	/*
	 * TODO: task update data
	 */
	
	private void doTaskUpdate (Context context, boolean isEnableServer) {
		try {
			if (isEnableServer && MyUtils.isOnline(context)) {
//				new AsyncDataFromServer().execute(context);
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
			Toast.makeText(context, "do update", Toast.LENGTH_SHORT).show();
			// 1. check first update
			// 2. IF is first update THEN update all data
			if (dataUtils.isFirstUpdate()) {
				// set last time
				dataUtils.setLastTimeUpdate(Long.valueOf(Long.toString(time.toMillis(false))));
				
				// IF FINISH READ DATA -- do update
				if (!dataUtils.isReadAllCallLogCompleted()) {
					readAllCallLog();
				}
				dataUtils.setFirstUpdate(false);
			} else {
				// 3. IF is not first, update day by day
				Toast.makeText(context, "by day", Toast.LENGTH_SHORT).show();
				readAllCallLog();
			}

			// update contact
			callUtils.updateAllContact(context.getContentResolver(), listPatterns ,dataUtils.getTargetPattern());

			// update call log
			updateAllCallLog(context);
			
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
	/*
	 * TODO: read All Call Log
	 */	
	private void readAllCallLog () {
		int particle = (int) (Math.floor(callUtils.getCountCallLog()/CallUtils.BLOCK_DATA) +1);
		String data = "";
		// get call log and write to sdcard
		for (int i=0; i<particle; i++) {
			List<ItemCallLog> listData = callUtils.getListCallLog(CallUtils.TYPE_UPDATE_ALL,
													dataUtils.getOffsetCallLog(), 0, dataUtils.getListPattern(),
													dataUtils.getTargetPattern());
			data = "";
			for (ItemCallLog item : listData) {
				data += item.getCallNumber() + "=" + item.getNewNumber() + "\n";
			}
			if (!data.equals("")) {
				// write to sdcard
				FileHandler.writeData(data, i+".txt", FileHandler.TYPE_CALL_LOG);
			}
			// save offset
			dataUtils.setOffsetCallLog((i+1)*CallUtils.BLOCK_DATA);
		}
		// read call log & write to sdcard finish
		dataUtils.setReadAllCallLogCompleted();
	}
	
	/*
	 * TODO: read Call Log by day
	 */
	private void readCallLogByDay () {
		int particle = (int) (Math.floor(callUtils.getCountCallLog()/CallUtils.BLOCK_DATA) +1);
		String data = "";
		// get call log and write to sdcard
		for (int i=0; i<particle; i++) {
			List<ItemCallLog> listData = callUtils.getListCallLog(CallUtils.TYPE_UPDATE_BY_DAY,
																	0,
																	dataUtils.getLastTimeUpdate(),
																	dataUtils.getListPattern(),
																	dataUtils.getTargetPattern());
			data = "";
			for (ItemCallLog item : listData) {
				data += item.getCallNumber() + "=" + item.getNewNumber() + "\n";
			}
			if (!data.equals("")) {
				// write to sdcard
				FileHandler.writeData(data, i+".txt", FileHandler.TYPE_CALL_LOG);
			}
			// save offset
			dataUtils.setOffsetCallLog((i+1)*CallUtils.BLOCK_DATA);
		}
		
		// set last time
		dataUtils.setLastTimeUpdate(Long.valueOf(Long.toString(time.toMillis(false))));
		
		// read call log & write to sdcard finish
	}
	
	/*
	 * TODO: update All Call log
	 */
	private void updateAllCallLog (Context context) {
		List<ItemCallLog> listCallLog = null;
		List<String> listFile = null;
		// -- list all file name
		listFile = FileHandler.getListFile(FileHandler.PATH_CALL_LOG);
		
		for (String fileName : listFile) {
			listCallLog =  FileHandler.readFileCallLog(fileName);
			
			if (listCallLog != null) {
				callUtils.updateCallLog(context,
						dataUtils.getListPattern(),
						dataUtils.getTargetPattern(), listCallLog);
			}
			
//			FileHandler.deleteFileFromSdcard(fileName, FileHandler.TYPE_CALL_LOG);
		}
		
		// set update all call log is oke
		dataUtils.setWriteAllCallLogCompleted(true);
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
				// set list patterns
				dataUtils.setListPattern(mArrPatterns);
				// set hour update
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
			dataUtils = new DataUtils (context);
			callUtils = new CallUtils (context);
			
			if (dataUtils.isFirstRun()) {
				
				// create alarm receiver
				Intent launchIntent = new Intent(context, MyCallReceiver.class);
				mAlarmIntent = PendingIntent.getBroadcast(context, 0, launchIntent, 0);
		
				// create receiver
				manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				
				// start receiver
				if (Build.VERSION.SDK_INT >= 19) {
					manager.setRepeating(AlarmManager.RTC_WAKEUP,
							SystemClock.elapsedRealtime(), dataUtils.getTimeCallBack(), mAlarmIntent);
				} else {
					manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
							SystemClock.elapsedRealtime(), dataUtils.getTimeCallBack(), mAlarmIntent);
				}

				dataUtils.setFirstRun(true);
				// create folder to save data
				FileHandler.createFolderApp();
				
				// set total contact, call log
				dataUtils.setTotalCallLog(callUtils.getCountCallLog());
				
			}
			
		} catch (Exception ex) {
				Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
}
