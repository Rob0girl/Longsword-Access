Readme for Listview:
XML's for layout:
		activity_location_list.xml
		content_location_list.xml
.java:
		LocationList.java

In AndroidManifest.xml:
		Below .registerPage activity:
		 <activity
            android:name=".LocationList"
            android:label="@string/title_activity_location_list"
            android:theme="@style/AppTheme.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>