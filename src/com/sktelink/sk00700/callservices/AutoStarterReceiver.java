package com.sktelink.sk00700.callservices;


import static com.sktelink.sk00700.callservices.utils.CommonUtilities.TAG;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.sktelink.sk00700.callservices.utils.DataUtils;

public class AutoStarterReceiver extends BroadcastReceiver {

	PendingIntent mAlarmIntent;
	AlarmManager manager;
	DataUtils dataUtils = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {

			// very important
			dataUtils = new DataUtils (context);
			
			// create alarm receiver
			Intent launchIntent = new Intent(context, MyCallReceiver.class);
			mAlarmIntent = PendingIntent.getBroadcast(context, 0, launchIntent, 0);

			// create alarm
			manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
			// start receiver
			if (Build.VERSION.SDK_INT >= 19) {
				manager.setRepeating(AlarmManager.RTC_WAKEUP,
						SystemClock.elapsedRealtime(), dataUtils.getTimeCallBack(), mAlarmIntent);
			} else {
				manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						SystemClock.elapsedRealtime(), dataUtils.getTimeCallBack(), mAlarmIntent);
			}
			
//			// sms observer
//			if (dataUtils.isEnableSms()) {
//				SmsObserver smsObeserver = (new SmsObserver(new Handler()));
//				SmsObserver.context = context;
//				SmsObserver.contentResolver = context.getContentResolver();
//				SmsObserver.contentResolver.registerContentObserver(Uri.parse("content://sms"),true, smsObeserver);
//			}
			
			Log.d(TAG, "on Receive Boot Completed");
		} catch (Exception ex) {
			Log.d(TAG, Log.getStackTraceString(ex));
		}
	}

}
