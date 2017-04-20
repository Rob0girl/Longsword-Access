package za.ac.up.cs.www.navup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
* @author Keoagile Dinake
* @version v1
* This class serves to provide user preference settings for navigation
*/
public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        
        /**
         * This method serves to persist the user's choice to show message notifications with regards
         * to navigation to a local config file.
         * @param void
         * @return void
         */
        Switch showMsgNotification = (Switch)findViewById(R.id.switch1);
        showMsgNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/nav/show-notification/?pref=true";
                    // send request
                    // handle response
                }
            }
        });

        /**
         * This method serves to persist the user's choice to vibrate upon receiving an
         * message notification with regards to navigation to a local config file.
         * @param void
         * @return void
         */
        Switch vibrateMsgNotification = (Switch)findViewById(R.id.switch2);
        showMsgNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/nav/vibrate/?pref=true";
                    // send request
                    // handle response
                }
            }
        });

        /**
         * This method serves to persist the user's choice to show in-app notifications with regards
         * to navigation to a local config file.
         * @param void
         * @return void
         */
        Switch showInAppNotification = (Switch)findViewById(R.id.switch3);
        showMsgNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/nav/show-notification/?pref=true";
                    // send request
                    // handle response
                }
            }
        });

        /**
         * This method serves to persist the user's choice to vibrate upon receiving an
         * in-app notification with regards to navigation to a local config file.
         * @param void
         * @return void
         */
        Switch vibrateInAppNotification = (Switch)findViewById(R.id.switch4);
        showMsgNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/nav/vibrate/?pref=true";
                    // send request
                    // handle response
                }
            }
        });
    }
}
