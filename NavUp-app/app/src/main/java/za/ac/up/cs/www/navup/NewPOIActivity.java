package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

/**
 * @author Bernard van Tonder
 * @version v1
 * This class allows an administrator to enter details of a new point of interest and redirect to
 * another activity to select the location of the point of interest.
 */

public class NewPOIActivity extends AppCompatActivity {
    Button setLocationBtn;
    Button createNewPOIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_point_of_interest);

        setLocationBtn = (Button) findViewById(R.id.setLocation);
        setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_to_set_location();
            }
        });
        createNewPOIBtn = (Button) findViewById(R.id.createNewPOI);
        createNewPOIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPointOfInterest(v);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EditText POIBuilding, POIRoom;
            POIBuilding = (EditText) findViewById(R.id.pointOfInterestBuildingName);
            POIRoom = (EditText) findViewById(R.id.pointOfInterestRoomName);
            POIBuilding.setText(extras.getString("BUILDING"));
            POIRoom.setText(extras.getString("ROOM"));
        }
    }

    /**
     * This function requests to create new a point of interest: It sends a json object to the
     * server using volley.
     */
    public void createPointOfInterest(View view) {
        String urlAddress = "http://10.0.2.2:8080/nav-up/users/Login";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String latitude = extras.getString("LATITUDE");
            String longitude = extras.getString("LONGITUDE");

            EditText POIBuilding, POIRoom;
            final TextView errorlbl;

            POIBuilding = (EditText) findViewById(R.id.pointOfInterestBuildingName);
            POIRoom = (EditText) findViewById(R.id.pointOfInterestRoomName);
            errorlbl = (TextView) findViewById(R.id.errorMsg);

            JSONObject newPOIDetails = new JSONObject();
            try {
                newPOIDetails.put("poi_building", POIBuilding.getText().toString());
                newPOIDetails.put("poi_room", POIRoom.getText().toString());
                newPOIDetails.put("poi_latitude", latitude);
                newPOIDetails.put("poi_longitude", longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (POIBuilding.getText().toString().trim().isEmpty() || POIRoom.getText().toString().trim().isEmpty()) {
                errorlbl.setText("Missing fields, please enter");
            } else {
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, urlAddress, newPOIDetails,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    response = response.getJSONObject("Success");
                                    Boolean successfulResponse = response.getBoolean("Success");
                                    if (successfulResponse) {
                                        errorlbl.setText("Success");
                                        errorlbl.setTextColor(Integer.parseInt("ff669900"));
//                                        go_to_menu();
                                    } else {
                                        errorlbl.setText("Failed to send");
                                    }
                                } catch (JSONException e) {
                                    errorlbl.setText(e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewPOIActivity.this, "Volley send error.", Toast.LENGTH_LONG).show();
                                errorlbl.setText(error.getMessage());
                                error.printStackTrace();
                            }
                        });
                Volley.newRequestQueue(this).add(jsonRequest);
            }
        } else {
            Toast.makeText(NewPOIActivity.this,
                    "Location not set. Please select location first.",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     *  This function directs to the select map location view.
     *  Parameters are passed on with intent to preserve input when returning.
     */
    public void go_to_set_location()
    {
        Intent intent = new Intent(this, SelectNewPOILocation.class);
        EditText POIName, POIDescription;
        POIName = (EditText) findViewById(R.id.pointOfInterestBuildingName);
        POIDescription = (EditText) findViewById(R.id.pointOfInterestRoomName);
        intent.putExtra("BUILDING", POIName.getText().toString());
        intent.putExtra("ROOM", POIDescription.getText().toString());
        startActivity(intent);
    }
    /**
     *  This function directs to the menu view. It is commented out till a menu exists
     */
//    public void go_to_menu()
//    {
//        Intent intent = new Intent(this, menuClassName.class);
//        startActivity(intent);
//    }
}
