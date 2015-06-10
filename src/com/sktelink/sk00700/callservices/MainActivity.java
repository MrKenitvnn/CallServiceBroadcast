package com.sktelink.sk00700.callservices;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	// set data
	DataUtils dataUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	protected void onResume() {
		super.onResume();
		dataUtils = new DataUtils(getApplicationContext());

		dataUtils.setInTest(true, 5000);
//		dataUtils.setEnableServer(true);
//		dataUtils.setUrlPatterns("http://callservice.esy.es/pattern.php");
//		dataUtils.setUrlTime("http://callservice.esy.es/time.php");

		MyCallReceiver.startService(this);
	}

}
