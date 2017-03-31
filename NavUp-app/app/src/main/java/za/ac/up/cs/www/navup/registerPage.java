package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class registerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    /*goes back to Login page when back clicked
    public void go_back(View view)
    {
        Intent intent = new Intent(this, this.getParent().getClass());
        startActivity(intent);
    }*/
}
