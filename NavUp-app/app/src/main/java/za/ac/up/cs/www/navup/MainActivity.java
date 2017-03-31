package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*goes to register page when register clicked */
    public void go_to_registerPage(View view)
    {
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }
}
