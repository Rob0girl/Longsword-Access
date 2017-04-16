package za.ac.up.cs.www.navup;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Bernard van Tonder
 * @version v1
 * credit:  http://stackoverflow.com/questions/31248257/androidgoogle-maps-get-the-location-on-touch
 */

public class SelectNewPOILocation extends FragmentActivity implements OnMapReadyCallback {
    private boolean hasSelectedLocation;
    private GoogleMap mMap;
    private LatLng selectedLocation;
    private Marker marker;
    Geocoder geocoder;
    SupportMapFragment mapFragment;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_poi_set_location);
        hasSelectedLocation = false;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSelectedLocation) {
                    go_to_new_poi();
                } else {
                    Toast.makeText(SelectNewPOILocation.this, "Please select a location first.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * Sets up the map display settings once the map is available. It is called automatically.
     * @param googleMap Google Map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng current_location = new LatLng(-25.755224, 28.2324730);
        mMap.addMarker(new MarkerOptions().position(current_location).title("Marker in UP-Hatfield"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location,17));
        mMap.setMaxZoomPreference(15);
        mMap.setMinZoomPreference(20);

        setUpMap();
    }

    /**
     * Defines the behaviour  when the map is touched to save the LatLng coordinates.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                selectedLocation = point;

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                        sb.append(address.getAddressLine(i) + "\n");
                    }
                    Toast.makeText(SelectNewPOILocation.this, sb.toString(), Toast.LENGTH_LONG).show();

                }

                if (marker != null) {
                    marker.remove();
                }

                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                hasSelectedLocation = true;
            }

        });
    }

    /**
     * Sends location, building name and room name strings and navigates back to new point of
     * interest view
     */
    public void go_to_new_poi()
    {
        Bundle extras = getIntent().getExtras();
        String pointOfInterestBuilding="";
        String pointOfInterestRoom="";
        if (extras != null) {
            pointOfInterestBuilding = extras.getString("BUILDING");
            pointOfInterestRoom = extras.getString("ROOM");
        }
        Intent intent = new Intent(this, NewPOIActivity.class);
        Double latitudeDouble = selectedLocation.latitude;
        Double longitudeDouble = selectedLocation.longitude;
        String latitudeString = latitudeDouble.toString();
        String longitudeString = longitudeDouble.toString();
        intent.putExtra("LATITUDE", latitudeString);
        intent.putExtra("LONGITUDE", longitudeString);
        intent.putExtra("BUILDING", pointOfInterestBuilding);
        intent.putExtra("ROOM", pointOfInterestRoom);
        startActivity(intent);
    }
}
