package com.sktelink.sk00700.callservices;

import static com.sktelink.sk00700.callservices.utils.CommonUtilities.TAG;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.sktelink.sk00700.callservices.handler.FileHandler;
import com.sktelink.sk00700.callservices.utils.DataUtils;
import com.sktelink.sk00700.callservices.utils.MyUtils;

public class SmsReceiver extends WakefulBroadcastReceiver{
	
	public static final String SMS_EXTRA_NAME = "pdus";
	DataUtils dataUtils = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// create instance of data utils
		if (dataUtils == null) {
			dataUtils = new DataUtils(context);
		}
		
		// Get the SMS map from Intent
        Bundle extras = intent.getExtras();
         
        String messages = "";
         
        if ( extras != null && dataUtils.isEnableSms())
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
             
//             Get ContentResolver object for pushing encrypted SMS to the incoming folder
//            ContentResolver contentResolver = context.getContentResolver();
             
            for ( int i = 0; i < smsExtra.length; ++i )
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
                 
                String body = sms.getMessageBody().toString();
                String address = sms.getOriginatingAddress();
	            
                messages += address + ";" + body + "\n";
                 
            	// file name
	            String fileName = "inbox" + MyUtils.stringFileDate() + ".txt";

	            // write data
            	FileHandler fileHandler = new FileHandler(context);
            	fileHandler.writeData( messages, fileName, FileHandler.TYPE_SMS);
                
                Log.d(TAG, messages );
            }
             
            // Display SMS message
            Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
        }
         
        // WARNING!!! 
        // If you uncomment the next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast();
	}

}
