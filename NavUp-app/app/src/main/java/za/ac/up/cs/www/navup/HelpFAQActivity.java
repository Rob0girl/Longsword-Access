package za.ac.up.cs.www.navup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HelpFAQActivity extends AppCompatActivity {

    private String [] helpList = {"How to save a location", "Help Me", "Seriously Help",
            "How to navigate to a class room", "How to update my profile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_faq);
        setTitle("Help/FAQ");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.listview, helpList);
        ListView listView = (ListView) findViewById(R.id.lv_HelpFAQ);
        listView.setAdapter(adapter);
    }
}
