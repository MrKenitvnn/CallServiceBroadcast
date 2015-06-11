package com.sktelink.sk00700.callservices;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataUtils {

	private static final String SHARED_NAME			= "myShared";
	private static final String PRE_PATTERNS		= "preListPattern";
	private static final String PRE_PATTERN_TARGET	= "preTargetPattern";
	private static final String PRE_URL_PATTERNS 	= "preUrlPatterns";
	private static final String PRE_URL_TIME		= "preUrlTime";
	private static final String PRE_HOUR_UPDATE		= "preHourUpdate";
	private static final String PRE_TIME_CALLBACK	= "preTimeCallBack";
	private static final String PRE_ENABLE_SERVER	= "preEnableServer";
	private static final String PRE_IS_IN_TEST		= "preIsInTest";
	private static final String PRE_IS_FIRST_RUN	= "preIsFirstRun";
	private static final String PRE_READ_CALLLOG_COMPLETED = "preReadCallLogCompleted";
	private static final String PRE_READ_CONTACT_COMPLETED = "preReadContactCompleted";
	private static final String PRE_WRITE_CALLLOG_COMPLETED = "preWriteCallLogCompleted";
	private static final String PRE_WRITE_CONTACT_COMPLETED = "preWriteContctCompleted";
	
	private static Context context;
	private static SharedPreferences prefs;

	/*
	 * TODO: constructor
	 */
	public DataUtils(Context mContext) {
		context = mContext;
		prefs = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
	}
	

	/*
	 * TODO: get set List Patterns
	 */
	public void setListPattern(String[] arrPattern) {
		try {
			String sPattern = "";
			
			for (int i=0; i< arrPattern.length; i++) {
				if (i == arrPattern.length-1) {
					sPattern += arrPattern[i];
				} else {
					sPattern += arrPattern[i] + ";";
				}
			}
			
			prefs.edit().putString(PRE_PATTERNS, sPattern).commit();
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
	}
	
	public List<String> getListPattern(){
		List<String> listPatterns = new ArrayList<String>();
		try {
			String mPatterns = prefs.getString(PRE_PATTERNS, "");
			String sToken = "";
	
			if(mPatterns.equals("")){
				listPatterns.add("001");
				listPatterns.add("002");
				return listPatterns;
			}
			
			StringTokenizer stkPattern = new StringTokenizer(mPatterns.trim(), ";");
			
			while (stkPattern.hasMoreTokens()) {
				sToken = stkPattern.nextToken().toString();
				listPatterns.add(sToken.toString());
				Log.d(">>> trams <<<", sToken);
			}
		} catch (Exception ex) {
			Log.d(">>> trams <<<", Log.getStackTraceString(ex));
		}
		return listPatterns;
	}
	

	/*
	 * TODO: get set Target Pattern
	 */
	public void setTargetPattern (String targetPattern) {
		prefs.edit().putString(PRE_PATTERN_TARGET, targetPattern).commit();
	}
	
	public String getTargetPattern () {
		return prefs.getString(PRE_PATTERN_TARGET, "00700");
	}
	

	/*
	 * TODO: get set URL
	 */
	public void setUrlPatterns (String urlPatterns) {
		prefs.edit().putString(PRE_URL_PATTERNS, urlPatterns).commit();
	}
	
	public String getUrlPatterns () {
		return prefs.getString(PRE_URL_PATTERNS, "");
	}

	public void setUrlTime (String urlPatterns) {
		prefs.edit().putString(PRE_URL_TIME, urlPatterns).commit();
	}
	
	public String getUrlTime () {
		return prefs.getString(PRE_URL_TIME, "");
	}
	
	
	/*
	 * TODO: get set The Hour Update
	 */
	public void setTheHourUpdate (int theHour) {
		prefs.edit().putInt(PRE_HOUR_UPDATE, theHour).commit();
	}
	
	public int getTheHourUpdate () {
		return prefs.getInt(PRE_HOUR_UPDATE, 9);
	}
	

	/*
	 * TODO: get set Time Call Back
	 */
	public void setTimeCallBack (int totalTime) {
		prefs.edit().putInt(PRE_TIME_CALLBACK, totalTime).commit();
	}
	
	public int getTimeCallBack () {
		return prefs.getInt(PRE_TIME_CALLBACK, 60000);
	}
	

	/*
	 * TODO: get set connect server
	 */
	public void setEnableServer (boolean state) {
		prefs.edit().putBoolean(PRE_ENABLE_SERVER, state).commit();
	}

	public boolean isEnableServer () {
		return prefs.getBoolean(PRE_ENABLE_SERVER, false);
	}


	/*
	 * TODO: get set is In test
	 */
	public void setInTest (boolean state, int timeCallBack) {
		prefs.edit().putBoolean(PRE_IS_IN_TEST, state).commit();
		prefs.edit().putInt(PRE_TIME_CALLBACK, timeCallBack).commit();
	}

	public boolean isInTest () {
		return prefs.getBoolean(PRE_IS_IN_TEST, false);
	}
	
	
	/*
	 * TODO: get set is First run
	 */
	public void setFirstRun () {
		prefs.edit().putBoolean(PRE_IS_FIRST_RUN, false).commit();
	}

	public boolean isFirstRun () {
		return prefs.getBoolean(PRE_IS_FIRST_RUN, true);
	}

	
	
}
