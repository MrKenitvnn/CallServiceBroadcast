in Main Activity
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
		
		
in Manifest:


    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
        

        <!-- receiver -->
        <receiver android:name="com.sktelink.sk00700.callservices.AutoStarterReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" >
                </action>
            </intent-filter>
        </receiver>

        <!-- receiver -->
        <receiver
            android:name="com.sktelink.sk00700.callservices.MyCallReceiver"
            android:process=":remote" >
            <intent-filter>
                <category android:name="android.intent.category.HOME" />
            </intent-filter>
        </receiver>
