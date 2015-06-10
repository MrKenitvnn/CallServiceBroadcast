package com.sktelink.sk00700.callservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class AutoStarterReceiver extends BroadcastReceiver {

	PendingIntent mAlarmIntent;
	AlarmManager manager;
	long interval = 5 * 1000; // 5 seconds

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// create alarm receiver
			Intent launchIntent = new Intent(context, MyCallReceiver.class);
			mAlarmIntent = PendingIntent.getBroadcast(context, 0, launchIntent, 0);

			// create alarm
			manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			// start alarm
			manager.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), interval, mAlarmIntent);
			

			Log.d(">>> trams <<<", "on Receive Boot Completed");
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}

}
