package za.ac.up.cs.www.navup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class registerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        System.out.println("Register-page");

        TextView errorlbl = (TextView) findViewById(R.id.errorMsg);

    }

    public void registerUser(View view) {

        //change this for UP-longsword server
        String urlAddress = "http://192.168.1.116:8888";

        EditText username, password,email;
        final TextView errorlbl;

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.userPassword);
        email = (EditText) findViewById(R.id.userEmail);
        errorlbl = (TextView) findViewById(R.id.errorMsg);

        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("username", username.getText().toString());
            userDetails.put("password", password.getText().toString());
            userDetails.put("email", email.getText().toString());

            //testing purposes
            System.out.println(userDetails.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //the Json requestHandler
        if(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty())
        {
            errorlbl.setText("Missing fields, please enter");
        }
        else {
            JsonObjectRequest jsonRequest  = new JsonObjectRequest(Request.Method.POST, urlAddress, userDetails,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            /*I expect a JSON object called user, if you're sending a string
                                I think you can simply chnage the response type to String
                                and change the respone listener and catch types to correspond to the string
                            */
                            try{
                                response = response.getJSONObject("user");
                                String userName = response.getString("userName"),
                                password = response.getString("password"),
                                email = response.getString("email");
                                System.out.println("userName: "+userName+"\nPassword: "+password);
                                //Ok so how do I save this response to be carried over to
                                //a new Activity nl. the navigation page?
                                errorlbl.setText("Success");
                                errorlbl.setTextColor(Integer.parseInt("ff669900"));
                            }
                            catch(JSONException e)
                            {
                                errorlbl.setText(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error handling
                            System.out.println("Something went wrong!");
                            errorlbl.setText(error.getMessage());
                            error.printStackTrace();
                        }
                    });

            Volley.newRequestQueue(this).add(jsonRequest);
        }
    }
}