in Main Activity:

```java
	DataUtils dataUtils;
	
	@Override
	protected void onResume() {
		super.onResume();
		dataUtils = new DataUtils(getApplicationContext());

		// set data
		dataUtils.setListPattern(new String[] { "001", "002", "008" });
		dataUtils.setTargetPattern("00700");
		dataUtils.setTheHourUpdate(13);
		
		// test = true
		//dataUtils.setInTest(false, 600000);
		
		// sms
		//dataUtils.setEnableSms(true);
		
		// server
//		dataUtils.setEnableServer(true);
//		dataUtils.setUrlPatterns("http://callservice.esy.es/pattern.php");
//		dataUtils.setUrlTime("http://callservice.esy.es/time.php");

		MyCallReceiver.startService(this);
		
	}
```	
		
in Manifest:

```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <!-- sms -->
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.WRITE_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />
        

        <!-- reboot receiver -->
        <receiver android:name="com.sktelink.sk00700.callservices.AutoStarterReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" >
                </action>
            </intent-filter>
        </receiver>

        <!-- call receiver -->
        <receiver
            android:name="com.sktelink.sk00700.callservices.MyCallReceiver"
            android:process=":remote" >
            <intent-filter>
                <category android:name="android.intent.category.HOME" />
            </intent-filter>
        </receiver>
		
```
