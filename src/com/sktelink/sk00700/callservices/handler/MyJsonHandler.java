package com.sktelink.sk00700.callservices.handler;

import static com.sktelink.sk00700.callservices.utils.CommonUtilities.TAG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sktelink.sk00700.callservices.object.ItemTime;

import android.util.Log;

public class MyJsonHandler {
	
	public static int	TIME_OUT	 = 3000;
	
	public static String TAG_PATTERN = "pattern";
	public static String TAG_TIME 	 = "time";
	public static String TAG_HOUR 	 = "hour";
	

	// //////////////////////////////////////////////////////////////////////////////
	// TODO get pattern

	public static String[] getPattern(String url) {
		String[] arrResult = null;
		List<String> listResult = new ArrayList<String>();
		
		try {
			// get string json
			String strJson = getJSONString(url, TIME_OUT);
			
			// check string json is null?
			if (strJson != null) {
				JSONObject jobRoot = new JSONObject(strJson);
				JSONArray jarrPattern = jobRoot.getJSONArray(TAG_PATTERN);
				
				for (int i=0; i< jarrPattern.length(); i++) {
					String sPattern = jarrPattern.getString(i);
					Log.d(TAG, sPattern);					
					listResult.add(sPattern);
				}// end-for
			} else {
				return null;
			}// end-if
			
			arrResult = new String[listResult.size()];
			for(int i=0; i< listResult.size(); i++){
				arrResult[i] = listResult.get(i);
			}
		
		} catch (Exception ex) {
			Log.d(TAG, Log.getStackTraceString(ex));
		}// end-try
		
		return arrResult;
	}// end-func getPattern
	

	// //////////////////////////////////////////////////////////////////////////////
	// TODO get hour

	public static ItemTime getTime(String url) {
		ItemTime item = new ItemTime();
		
		try {
			// get string json
			String strJson = getJSONString(url, TIME_OUT);
			
			// check string json is null?
			if (strJson != null) {
				
				JSONObject jsoRoot = new JSONObject(strJson);
				JSONObject jsoTime = jsoRoot.getJSONObject(TAG_TIME);
				String sHour = jsoTime.getString(TAG_HOUR);
				
				item.setHour(Integer.valueOf(sHour));
				
				Log.d(TAG, "the hour update: " + sHour);
			} else {
				return null;
			}// end-if
		} catch (Exception ex) {
			Log.d(TAG, Log.getStackTraceString(ex));
			return null;
		}// end-try
		return item;
	}// end-func getTime
	

	// //////////////////////////////////////////////////////////////////////////////
	// TODO get json string

	public static String getJSONString(String url, int timeout) {
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
			case 201:
			case 200:
				BufferedReader br = new BufferedReader(new InputStreamReader(
						c.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}

		} catch (MalformedURLException ex) {
			Log.d(TAG, Log.getStackTraceString(ex));
		} catch (IOException ex) {
			Log.d(TAG, Log.getStackTraceString(ex));
		} finally {
			if (c != null) {
				try {
					c.disconnect();
				} catch (Exception ex) {
					Log.d(TAG, Log.getStackTraceString(ex));
				}
			}
		}
		return null;
	}// end-func getJSONString
	
	

}
