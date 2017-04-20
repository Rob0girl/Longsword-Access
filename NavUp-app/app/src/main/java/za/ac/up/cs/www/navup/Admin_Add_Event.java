package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_Add_Event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__event);

        Button Cancel = (Button) findViewById(R.id.btnCancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UpcomingIntent = new Intent(v.getContext(),Admin_Events.class);
                startActivityForResult(UpcomingIntent,0);
            }
        });


    }
}
