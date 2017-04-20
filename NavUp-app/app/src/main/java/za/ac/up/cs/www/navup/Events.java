package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class Events extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Button UpcomingE = (Button) findViewById(R.id.btnUpcomingEvents);
        Button MyEvents = (Button) findViewById(R.id.btnMyEvents);
        Button Admin = (Button) findViewById(R.id.btnAdmin);

        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UpcomingIntent = new Intent(v.getContext(),Admin_Events.class);
                startActivityForResult(UpcomingIntent,0);
            }
        });

        UpcomingE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UpcomingIntent = new Intent(v.getContext(),Upcoming_Events.class);
                startActivityForResult(UpcomingIntent,0);
            }
        });

        MyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MyEventsIntent = new Intent(v.getContext(),MyEvents.class);
                startActivityForResult(MyEventsIntent,0);
            }
        });

    }
}
