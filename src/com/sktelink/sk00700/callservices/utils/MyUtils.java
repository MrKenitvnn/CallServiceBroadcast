package com.sktelink.sk00700.callservices.utils;

import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyUtils {


	/**
	 * TODO: check network
	 */
	public static boolean isOnline(Context context) {

		ConnectivityManager cm  = (ConnectivityManager) 
								context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo		= cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	
	/**
	 * TODO: return string: day + month + year
	 */
	public static String stringFileDate () {
		
		Calendar c = Calendar.getInstance(); 
    	int day = c.get(Calendar.DAY_OF_MONTH);
    	int month = c.get(Calendar.MONTH) + 1;
    	int year = c.get(Calendar.YEAR);
    	
    	return "" + day + month + year;
	}
	
}
