in onCreate

// set data
		DataUtils dataUtils = new DataUtils(getApplicationContext());
		dataUtils.setListPattern(new String[]{"001", "011", "002"});
		dataUtils.setTargetPattern("00700");
		dataUtils.setTheHourUpdate(13);
		dataUtils.setInTest(true, 20000);

		
//		dataUtils.setEnableServer(true);
//		dataUtils.setUrlPatterns("http://callservice.esy.es/pattern.php");
//		dataUtils.setUrlTime("http://callservice.esy.es/time.php");
		
		// finally -- start
		MyCallReceiver.startService(this);
		
		
in Manifest:


    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
        
        <!-- receiver -->
        <receiver android:name="com.trams.callservices.AutoStarterReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" >
                </action>
            </intent-filter>
        </receiver>

        <!-- receiver -->
        <receiver android:name="com.trams.callservices.MyCallReceiver" >
            <intent-filter>
                <category android:name="android.intent.category.HOME" />
            </intent-filter>
        </receiver>
