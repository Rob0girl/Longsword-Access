package za.ac.up.cs.www.navup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import static za.ac.up.cs.www.navup.R.layout.activity_location_list;

public class LocationList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_location_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.fab);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go back
            }
        });

        String[] locations = {"Tribecca","Aula","Piazza", "Adlers", "Thuto"};
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
        ListView locationView = (ListView) findViewById(R.id.locationListView);
        locationView.setAdapter(listAdapter);

        locationView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedLocation = String.valueOf(parent.getItemAtPosition(position));
                        // send location name to navigation
                        // change view to navigation
                    }
                }
        );
    }

}
