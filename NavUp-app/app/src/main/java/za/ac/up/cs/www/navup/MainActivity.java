package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @use This class acts as the Login class, it is where the user can either login or navigate to registration
 * it is called Main as this was the original start of the app
 * @author Idrian van der Westhuizen
 */

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

    public void Login(View view)
    {
        //change for UP-Longsword
        /*
            Once I am done with the singleton class I will save the address there so it can be called repeatedly from there and saved once
            but for now I wrote it in Login and register
         */
        String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/nav-up/users/authenticate";

        final EditText userName, password;
        final TextView errorlbl;

        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.Password);
        errorlbl = (TextView) findViewById(R.id.errorMsg);

        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("username", userName.getText().toString());
            userDetails.put("password", password.getText().toString());
            //testing purposes
            System.out.println(userDetails.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //request to POST data
        if(userName.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())
        {
            errorlbl.setText("Missing fields, please enter");
        }
        else
        {
            JsonObjectRequest jsonRequest  = new JsonObjectRequest(Request.Method.POST, urlAddress, userDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            /*I expect a JSON object called user, if you're sending a string
                                I think you can simply chnage the response type to String
                                and change the respone listener and catch types to correspond to the string
                            */
                            /*
                                quick note the App will crash or atleast the current activity page will crash if the response is not a valid one
                                i.e. it is better not to send anything and have the request timeout than to send wrong data.
                             */
                            try{
                                Boolean success = Boolean.parseBoolean(response.getString("Success"));
                                if(!success)
                                {
                                    // Some error message
                                    errorlbl.setText("Error logging in");
                                }
                                else
                                {
                                    // User is logged in
                                    errorlbl.setText("Successfully Logged in");
                                    go_to_map(userName.getText().toString());
                                }



                            }
                            catch(JSONException e)
                            {
                                Toast.makeText(MainActivity.this,
                                        "Json convert error",
                                        Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error handling
                            System.out.println("Something went wrong!");
                            Toast.makeText(MainActivity.this, "Volley error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json;");
                    return headers;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            Volley.newRequestQueue(this).add(jsonRequest);
        }


    }

    public void login_as_guest(View view)
    {
        go_to_map("Guest");
    }

    public void go_to_map(String name)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("userName",name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
