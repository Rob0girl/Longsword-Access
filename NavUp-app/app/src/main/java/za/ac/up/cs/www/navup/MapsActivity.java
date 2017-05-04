package za.ac.up.cs.www.navup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This is the Main page of the app, it is the central hubb that displays the map
 * as well as navigation drawers and menus to other pages.
 * This class is responsible for anything that happens and is displayed on the map.
 * @author Idrian van der Westhuizen, Merissa Joubert, Bernard van Tonder
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @use handles and creates the main page and functionality of the map
 * @author Idrian van der Westhuizen, Merissa Joubert, Bernard van Tonder
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    String userName;

    LatLng destination;
    boolean loggedIn = false;
    boolean navigating = false;
    Polyline route = null;
    PolylineOptions routeOptions = new PolylineOptions();

                
    private String selectedLocationName="IT";
    private String[] buildings;
    private int buildingCount;
    private int id;
    private double latitude;
    private double longitude;
    private List<List<String>> rooms;
    private int locationCount;
    private String[] locations;
    private LinkedList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * @use if the Activity was paused and started up again this will check if anything was sent to this activity
     *      to be used like userName or the requested destination
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            userName = extras.getString("userName");
            loggedIn = true;
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            MenuItem login = navigationView.getMenu().getItem(3).setVisible(false);
            MenuItem logout = navigationView.getMenu().getItem(4).setVisible(true);

            if(extras.get("LATITUDE") != null) {
                destination = new LatLng(extras.getDouble("LATITUDE"), extras.getDouble("LONGITUDE"));
                selectedLocationName = extras.getString("SELECTED_LOCATION_NAME");
                navigating = true;
            }
        }

        //onMapReady(mMap);
        return;
    }


    /**
     * @use creates the initial map and calls the populatlocations function
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(false);
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(15);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }



        populateLocationListArrayAndDisplay();
        route = mMap.addPolyline(routeOptions);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     *
     * Updates everytime the user location updates
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
       /* if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }*/

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(mCurrLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(userName);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }
        else
        {
            mCurrLocationMarker.setPosition(latLng);
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        //update navigation
        if(navigating)
        {
            route = calcRoute(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),destination);
            mMap.addPolyline(routeOptions);
        }
        else
        {
            if(route != null)
            {
                route.remove();
            }
        }


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    /**
     * @use create a polyline that is displayed on the map based on the LatLng values gotten from navigation
     * @param location
     * @param destination
     * @return
     */
    private Polyline calcRoute(LatLng location,LatLng destination)
    {
        List<LatLng> waypoints = new List<LatLng>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<LatLng> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(LatLng latLng) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends LatLng> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends LatLng> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public LatLng get(int index) {
                return null;
            }

            @Override
            public LatLng set(int index, LatLng element) {
                return null;
            }

            @Override
            public void add(int index, LatLng element) {

            }

            @Override
            public LatLng remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<LatLng> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<LatLng> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<LatLng> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        JSONObject sourceObj = new JSONObject();
        JSONObject destinationObj = new JSONObject();
        JSONObject userLocation = new JSONObject();
        try {
            sourceObj.put("lat",location.latitude);
            sourceObj.put("long",location.longitude);

            destinationObj.put("lat",location.latitude);
            destinationObj.put("long",location.longitude);

            userLocation.put("source",sourceObj);
            userLocation.put("destination",destinationObj);
            userLocation.put("restrictions",false);
            userLocation.put("preferences",Double.POSITIVE_INFINITY);
            //testing purposes
            System.out.println(userLocation.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String urlAddress = "http://affogato.cs.up.ac.za:8080/NavUP/nav-up/navigation/get-route/";
        waypoints.add(location);
        final List<LatLng> finalWaypoints = waypoints;
        JsonObjectRequest jsonRequest  = new JsonObjectRequest(Request.Method.GET, urlAddress, userLocation,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            int length = Integer.parseInt(response.getString("length"));
                           JSONArray path = response.getJSONArray("waypoints");
                            for(int i = 0; i< path.length(); i++) {
                                JSONObject o =  (JSONObject)path.get(i);
                                finalWaypoints.add(new LatLng(
                                        o.getDouble("lat"),
                                        o.getDouble("long")
                                        )
                                );
                            }
                        }
                        catch(JSONException e)
                        {
                            //Toast.makeText(MapsActivity.this,
                                    //"Json convert error",
                                   // Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                       // Toast.makeText(MapsActivity.this, "error getting route", Toast.LENGTH_LONG).show();
                        route = loadTestRoute(finalWaypoints);
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


        if(jsonRequest.isCanceled())
        {
            //do nothing
        }
        else {
            for (LatLng i : finalWaypoints) {
                routeOptions.add(i);
            }
        }
        /*routeOptions.add(
                new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                new LatLng(-25.7557438,28.2331601),
                destination);*/
        routeOptions.color(Color.RED);
        route.setPoints(finalWaypoints);

        return route;
    }

    /**
     * @use loads a test route incase the server crashes
     * @param waypoints
     * @return
     */
    private Polyline loadTestRoute(List<LatLng> waypoints)
    {
        waypoints.add(new LatLng(-25.7560989,28.2330501));
        waypoints.add(destination);

        routeOptions.add(
                new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                new LatLng(-25.7557438,28.2331601),
                destination);
        routeOptions.color(Color.RED);


       route.setPoints(waypoints);

        return route;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @use creates and populates the menu with the selected menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_page, menu);
        return true;
    }

    /**
     * @use handles the options menu onclick events
     * @param item: item selected in the options menu
     * @return return true if menu responded fals if failed to respond
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            go_to_Settings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @use This handles the onlcik events of the burger/drawer menu
     * @param item: this is the item selected in the burger/drawer menu
     * @return return true if the menu responded or false if failed to respond
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Profile) {

        } else if (id == R.id.POI) {
            go_to_POI();
        } else if (id == R.id.Events) {
            go_to_Events();
        } else if (id == R.id.Login) {
            go_to_login();
        }
        else if (id == R.id.Logout) {
            userName = null;
            mCurrLocationMarker.setTitle(userName);
            loggedIn = false;
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
            MenuItem login = navigationView.getMenu().getItem(3).setVisible(true);
            MenuItem logout = navigationView.getMenu().getItem(4).setVisible(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void go_to_login()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void go_to_POI()
    {
        Intent intent = new Intent(this, LocationList.class);
        startActivity(intent);
    }

    public void go_to_Events()
    {
        Intent intent = new Intent(this, Events.class);
        startActivity(intent);
    }

    public void go_to_Settings()
    {
        Intent intent = new Intent(this, SettingsMainActivity.class);
        startActivity(intent);
    }

    /**
     *  @use populates the map with the values optained from the GIS module
     */
    public void populateLocationListArrayAndDisplay() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://affogato.cs.up.ac.za:8080/NavUP//nav-up/gis/get-all-buildings";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        buildingCount = response.length();
                        locationCount = 0;
                        buildings = new String[buildingCount];
                        rooms = new ArrayList<List<String>>(buildingCount);
                        try {
                            JSONArray array = response.getJSONArray("buildings");
                            for (int i=0; i<buildingCount; i++) {
                                buildings[i] = array.getJSONObject(i).getString("building");
                                rooms.add(new ArrayList<String>());
                                populateRoomsOfBuilding(i);
                            }
                            onAllBuildingsStored();
                        } catch (JSONException jE) {
                           // Toast.makeText(MapsActivity.this,
                             //       "Json convert error",
                               //     Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // Toast.makeText(MapsActivity.this, "error getting buildings", Toast.LENGTH_LONG).show();
                loadTestMarkers();

            }
        });
        queue.add(getRequest);
    }

    /**
     * @use fills the map with mock markers if the server does not respond
     */
    private void loadTestMarkers()
    {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-25.75585, 28.2330092))
                .title(selectedLocationName)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
        );
    }


    /**
     * This fills the location list with the data requested previously
     */
    public void onAllBuildingsStored() {
        locations = new String[locationCount];
        int currentLocationIndex = 0;
        int roomsInBuilding;
        for (int i=0; i<buildingCount; i++) {
            roomsInBuilding = rooms.get(i).size();
            for (int j=0; j < roomsInBuilding; j++){
                locations[currentLocationIndex] = buildings[i] + ":" + rooms.get(i).get(j);
                ++currentLocationIndex;
            }
        }
    }
    /**
     * This function
     * @param inBuildingNumber is the selected index from the listView.
     */
    public void populateRoomsOfBuilding(int inBuildingNumber){
        final int buildingNumber = inBuildingNumber;
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://affogato.cs.up.ac.za:8080/NavUP/nav-up/gis/get-venues?building={"+buildings[buildingNumber]+"}";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int roomCount = response.length();
                        try {
                            JSONArray array = response.getJSONArray("rooms");
                            markers = new LinkedList<Marker>();
                            for (int i=0; i<roomCount; i++) {
                                rooms.get(buildingNumber).add(array.getJSONObject(i).getString("room"));
                                requestCoordinates(buildings[buildingNumber],
                                        array.getJSONObject(i).getString("room"));
                                ++locationCount;
                            }
                        } catch (JSONException jE) {
                           // Toast.makeText(MapsActivity.this,
                             //       "Json convert error",
                               //     Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // Toast.makeText(MapsActivity.this, "error getting venues", Toast.LENGTH_LONG).show();

            }
        });
        queue.add(getRequest);
    }

    /**
     * This function requests a building&room 's coordinates and displays a marker.
     * @param buildingName name of the building
     * @param roomName name of the room
     */
    private void requestCoordinates(String buildingName, String roomName) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://affogato.cs.up.ac.za:8080/nav-up/gis/get-location?building={"+
                buildingName+"}&venue={"+roomName+"}";
        final String title = buildingName + ":" + roomName;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude = response.getJSONObject("location").getDouble("lat");
                            longitude = response.getJSONObject("location").getDouble("long");
                            id = response.getJSONObject("location").getInt("id");

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))

                                    .title(title)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                            );

                        } catch (JSONException jE) {
                            //Toast.makeText(MapsActivity.this,
                              //      "Json convert error",
                                //    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(MapsActivity.this, "error getting locations", Toast.LENGTH_LONG).show();

            }
        });
        queue.add(getRequest);
    }
}
