package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
/**
* @author Keoagile Dinake
* @version v1
* This class serves as the landing page for all settings, all sub settings are accessed through this class
*/
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
        //System.out.println("----------------------------------------openInterest---------------------");
        Intent intent = new Intent(this, InterestPointActivity.class); // change to Interest Point Activity
        startActivity(intent);
    }

    public void openMyLocations(View view){
        //System.out.println("----------------------------------------openMyLocations---------------------");
        Intent intent = new Intent(this, SettingsMainActivity.class); // change to MyLocations Activity
        startActivity(intent);
    }

    public void openNotifications(View view){
        //System.out.println("----------------------------------------open Notifications---------------------");
        Intent intent = new Intent(this, NotificationActivity.class); // change to Notifications Activity
        startActivity(intent);
    }

    public void openHelpFAQ(View view){
        //System.out.println("----------------------------------------FAQ---------------------");
        Intent intent = new Intent(this, HelpFAQActivity.class); // change to Help/FAQ Activity
        startActivity(intent);
    }

    /**
    * This method serves to logout the user
    * @param view The current View object clicked
    * @return void
    */
    public void go_to_map(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("userName","");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
