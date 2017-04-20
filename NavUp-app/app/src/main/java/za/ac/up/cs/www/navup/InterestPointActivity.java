package za.ac.up.cs.www.navup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
* @author Keoagile Dinake
* @version v1
* This class serves to provide user preferences for Points of Interest
*/

public class InterestPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_point);
        /**
        * This method serves to persist the user's choice to share their point of interests to the database.
        * @param void
        * @return void
        */
        Switch sharePOI = (Switch) findViewById(R.id.switch1);
        sharePOI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/poi/share-poi/?pref=true";
                    // send request
                    // handle response
                }
            }
        });
        /**
        * This method serves to persist the user's choice to show notifications with regards 
        * to point of interests to the database.
        * @param void
        * @return void
        */
        Switch showNotification = (Switch) findViewById(R.id.switch2);
        showNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/user/settings/poi/show-notification/?pref=true";
                    // send request
                    // handle response
                }
            }
        });
        
    }
}
