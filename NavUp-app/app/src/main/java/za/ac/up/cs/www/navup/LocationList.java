
package za.ac.up.cs.www.navup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.graphics.Color;

import static za.ac.up.cs.www.navup.R.layout.activity_location_list;

/**
 * This class displays a list of all the locations. A user can select a location to navigate to it.
 * A user can also delete or change activity to update or create a point of interest.
 * @author Bernard van Tonder, Idrian van der Westhuizen
 */

public class LocationList extends AppCompatActivity
{
    private String selectedLocationName;
    private String[] buildings;
    private int buildingCount;
    private int id;
    private double latitude;
    private double longitude;
    private boolean hasValidCoordinates;
    private List<List<String>> rooms;
    private int locationCount;
    private String[] locations;
    private String activeBehaviour;
    Button deleteBtn;
    Button updateBtn;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_location_list);
        hasValidCoordinates = false;
        activeBehaviour = "navigate";

        selectedLocationName = "";
        ListView locationView = (ListView) findViewById(R.id.locationListView);

       populateLocationListArray(locationView);
       //loadTestList();




        locationView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedLocationName = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(LocationList.this, selectedLocationName, Toast.LENGTH_LONG).show();
                        //setCoordinates();
                        if (hasValidCoordinates){
                            goToMap();
                        }
                    }
                }
        );

        updateBtn = (Button) findViewById(R.id.updatePOIBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeBehaviour = "update";
                changeSelectBehaviour();
            }
        });

        deleteBtn = (Button) findViewById(R.id.deletePOIBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeBehaviour = "delete";
                changeSelectBehaviour();;
            }
        });

        createBtn = (Button) findViewById(R.id.createPOIBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreatePOI();
            }
        });
        updateBtn.setBackgroundColor(Color.LTGRAY);
        updateBtn.setTextColor(Color.WHITE);
        deleteBtn.setBackgroundColor(Color.LTGRAY);
        deleteBtn.setTextColor(Color.WHITE);
        createBtn.setBackgroundColor(Color.LTGRAY);
        createBtn.setTextColor(Color.WHITE);

    }
    /**
     * This function gets all locations from server and puts each building into the building array.
     * It then gets each room within a building.
     * Performance can be improved if buildings and rooms were given from the server as one object.
     * Credit:http://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
     **/
    public void populateLocationListArray(final ListView locationView) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="affogato.cs.up.ac.za:8080/NavUP/nav-up/gis/get-all-buildings";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        locationCount = 0;
                        try {
                            JSONArray array = response.getJSONArray("buildings");
                            buildingCount = array.length();
                            buildings = new String[buildingCount];
                            rooms = new ArrayList<List<String>>(buildingCount);
                            for (int i=0; i<buildingCount; i++) {
                                buildings[i] = array.getJSONObject(i).getString("name");
                                rooms.add(new ArrayList<String>());
                                populateRoomsOfBuilding(i);
                            }
                            onAllBuildingsStored(locationView);

                        } catch (JSONException jE) {
                            //Toast.makeText(LocationList.this,
                              //      "Json convert error",
                                //    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   Toast.makeText(LocationList.this, "Volley error", Toast.LENGTH_LONG).show();
                        loadTestList(locationView);
                    }
                });
        queue.add(getRequest);
    }

    private void loadTestList(ListView locationView)
    {
            buildings = new String[1];
            rooms = new ArrayList<List<String>>(3);
            buildings[0] = "IT";
            buildingCount = 1;
            locationCount = 3;
            rooms.add(new ArrayList<String>());
            rooms.get(0).add("4-1");
        rooms.get(0).add("4-4");
        rooms.get(0).add("4-5");
            locations = new String[locationCount];
            locations[0] = buildings[0] + ":" + rooms.get(0).get(0);
        locations[1] = buildings[0] + ":" + rooms.get(0).get(1);
        locations[2] = buildings[0] + ":" + rooms.get(0).get(2);
            latitude =-25.75585;
            longitude = 28.2330092;
            hasValidCoordinates = true;

        if (locations != null) {
            ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
            locationView.setAdapter(listAdapter);
        } else {
            //Toast.makeText(LocationList.this, "LocationList empty", Toast.LENGTH_SHORT).show();
        }

    }

     public void onAllBuildingsStored(ListView locationView) {
        locations = new String[locationCount];
        int currentLocationIndex = 0;
        int roomsInBuilding;
        for (int i=0; i<buildingCount; i++) {
            roomsInBuilding = rooms.get(i).size();
            for (int j=0; j < roomsInBuilding; j++) {
                locations[currentLocationIndex] = buildings[i] + ":" + rooms.get(i).get(j);
                ++currentLocationIndex;
            }
        }

         if (locations != null) {
             ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
             locationView.setAdapter(listAdapter);
         } else {
             //Toast.makeText(LocationList.this, "LocationList empty", Toast.LENGTH_SHORT).show();
         }
    }
    /**
     * This function
     * @param inBuildingNumber is the building index from which to retrieve rooms
     */
    public void populateRoomsOfBuilding(int inBuildingNumber) {
        final int buildingNumber = inBuildingNumber;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "affogato.cs.up.ac.za:8080/NavUP/nav-up/gis/get-venues?building = {"+buildings[buildingNumber]+"}";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int roomCount = response.length();
                        try {
                            JSONArray array = response.getJSONArray("rooms");
                            for (int i=0; i<roomCount; i++) {
                                rooms.get(buildingNumber).add(array.getJSONObject(i).getString("room"));
                                ++locationCount;
                            }
                        } catch (JSONException jE) {
                           // Toast.makeText(LocationList.this,
                             //       "Json convert error",
                               //     Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LocationList.this, "Volley error", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(getRequest);
    }

    public void goToMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("SELECTED_LOCATION_NAME", selectedLocationName);
        intent.putExtra("LATITUDE", latitude);
        intent.putExtra("LONGITUDE", longitude);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * This retrieves the coordinates and id of a location and sets the relevant member variables.
     */
    private void setCoordinates() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String parts[] = selectedLocationName.split(":");
        String url = "affogato.cs.up.ac.za:8080/NavUP/nav-up/gis/get-location?building={"+parts[0]+"}&venue={"+parts[1]+"}";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude = response.getJSONObject("location").getDouble("lat");
                            longitude = response.getJSONObject("location").getDouble("long");
                            id = response.getJSONObject("location").getInt("id");
                            hasValidCoordinates = true;
                        } catch (JSONException jE) {
                            //Toast.makeText(LocationList.this,
                             //       "Json convert error",
                              //      Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(LocationList.this, "Volley error", Toast.LENGTH_LONG).show();

            }
        });
        queue.add(getRequest);
    }



    /**
     * This changes the behaviour of clicking on an item in the list.
     */
    public void changeSelectBehaviour() {
        updateBtn = (Button) findViewById(R.id.updatePOIBtn);
        deleteBtn = (Button) findViewById(R.id.deletePOIBtn);
        updateBtn.setBackgroundColor(Color.LTGRAY);
        updateBtn.setTextColor(Color.WHITE);
        deleteBtn.setBackgroundColor(Color.LTGRAY);
        deleteBtn.setTextColor(Color.WHITE);
        ListView locationView = (ListView) findViewById(R.id.locationListView);

        if (activeBehaviour.equals("delete")) {
            deleteBtn.setBackgroundColor(Color.GRAY);
            locationView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedLocationName = String.valueOf(parent.getItemAtPosition(position));
                            Toast.makeText(LocationList.this, "Tap delete to delete " + selectedLocationName, Toast.LENGTH_LONG).show();

                        }
                    }
            );

            deleteBtn = (Button) findViewById(R.id.deletePOIBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteSelectedLocation();
                }
            });
        } else if (activeBehaviour.equals("update")){
            updateBtn.setBackgroundColor(Color.GRAY);
            locationView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedLocationName = String.valueOf(parent.getItemAtPosition(position));
                            goToUpdatePOI();
                        }
                    }
            );
            deleteBtn = (Button) findViewById(R.id.deletePOIBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activeBehaviour = "delete";
                    changeSelectBehaviour();;
                }
            });
        } else if (activeBehaviour.equals("navigate")){
            locationView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedLocationName = String.valueOf(parent.getItemAtPosition(position));
                            Toast.makeText(LocationList.this, selectedLocationName, Toast.LENGTH_LONG).show();
                            setCoordinates();
                            if (hasValidCoordinates){
                                goToMap();
                            }
                        }
                    }
            );
        }
    }

    /**
     * This deletes a selected location.
     */
    public void deleteSelectedLocation() {
        String urlAddress = "affogato.cs.up.ac.za:8080/NavUP/nav-up/gis/delete-location";
        RequestQueue queue = Volley.newRequestQueue(this);

        if (selectedLocationName.equals("")) {
            Toast.makeText(LocationList.this, "Please select a location to delete by tapping on it.", Toast.LENGTH_LONG).show();
        } else {
            String parts[] = selectedLocationName.split(":");
            JSONObject toDeletePOI = new JSONObject();
            try {
                toDeletePOI.put("building", parts[0]);
                toDeletePOI.put("room", parts[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                    urlAddress,
                    toDeletePOI,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean successfulResponse = response.getBoolean("success");
                                if (successfulResponse) {
                                    Toast.makeText(LocationList.this, "Location successfully deleted.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LocationList.this, "Failed to send", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LocationList.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(LocationList.this, "Volley send error.", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(jsonRequest);
        }
    }

    /**
     * This changes the active screen to create new activity
     */
    public void goToCreatePOI() {
        Intent intent = new Intent(this, NewPOIActivity.class);
        startActivity(intent);
    }

    /**
     * This changes the active screen to the update screen and passes the selected location details.
     */
    public void goToUpdatePOI() {
        if (selectedLocationName.equals("")){
            Toast.makeText(LocationList.this, "Select location to update", Toast.LENGTH_LONG).show();
        } else {
           goToUpdatePOI();
        }
        Intent intent = new Intent(this, UpdatePOIActivity.class);
        String parts[] = selectedLocationName.split(":");
        intent.putExtra("BUILDING", parts[0]);
        intent.putExtra("ROOM", parts[1]);
        intent.putExtra("ID", id);
  //     intent.putExtra("LATITUDE", latitude);
//        intent.putExtra("LONGITUDE", longitude);
        startActivity(intent);
    }
}

