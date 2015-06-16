package com.sktelink.sk00700.callservices;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class SMSObserver extends ContentObserver {
	
	public SMSObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	// sms
    Context context;
    private static final String CONTENT_SMS = "content://sms/";
    public static ContentResolver contentResolver ;
    
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        // save the message to the SD card here
        Cursor cur = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);

        if (cur.moveToNext()) {
            String message_id = cur.getString(cur.getColumnIndex("_id"));
            String type = cur.getString(cur.getColumnIndex("type"));
            String numeroTelephone=cur.getString(cur.getColumnIndex("address")).trim();
            String protocol = cur.getString(cur.getColumnIndex("protocol"));
            Date now = new Date(cur.getLong(cur.getColumnIndex("date")));
            String message = cur.getString(cur.getColumnIndex("body"));
            
            ContentValues values = new ContentValues();
            values.put("address", "00700112");
            contentResolver.update(Uri.parse("content://sms"), values, "_id="+message_id, null);   
        
        }
    }
}
