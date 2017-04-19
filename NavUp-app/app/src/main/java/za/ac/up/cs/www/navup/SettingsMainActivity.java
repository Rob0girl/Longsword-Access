package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
    }

    public void openProfile(View view){
        Intent intent = new Intent(this, SettingsMainActivity.class); // change to Profile Activity that Dewald is doing
        startActivity(intent);
    }

    public void openInterestPoints(View view){
        Intent intent = new Intent(this, InterestPointActivity.class); // change to Interest Point Activity
        startActivity(intent);
    }

    public void openMyLocations(View view){
        Intent intent = new Intent(this, SettingsMainActivity.class); // change to MyLocations Activity
        startActivity(intent);
    }

    public void openNotifications(View view){
        Intent intent = new Intent(this, NotificationActivity.class); // change to Notifications Activity
        startActivity(intent);
    }

    public void openHelpFAQ(View view){
        Intent intent = new Intent(this, HelpFAQActivity.class); // change to Help/FAQ Activity
        startActivity(intent);
    }

    public void logout(){
        // logout the user
    }
}
