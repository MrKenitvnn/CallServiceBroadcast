package com.sktelink.sk00700.callservices;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set data
		DataUtils dataUtils = new DataUtils(getApplicationContext());
		dataUtils.setListPattern(new String[] { "001", "011", "002" });
		dataUtils.setTargetPattern("00700");
		dataUtils.setTheHourUpdate(13);
		dataUtils.setInTest(true, 20000);

		// dataUtils.setEnableServer(true);
		// dataUtils.setUrlPatterns("http://callservice.esy.es/pattern.php");
		// dataUtils.setUrlTime("http://callservice.esy.es/time.php");

		MyCallReceiver.startService(this);

	}

}
