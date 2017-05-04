package za.ac.up.cs.www.navup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
* @author Keoagile Dinake
* @version v1
* This class serves to provide a list of FAQs that a user may need, at current,
* the FAQ are just mocked.
*/
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
