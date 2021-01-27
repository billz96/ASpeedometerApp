package gr.example.zografos.vasileios.aspeedometerapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DBHelper dbHelper;
    ArrayList<HashMap<String, String>> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        dbHelper = new DBHelper(this);
        locations = dbHelper.allLocations();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // do we have any locations with high speed?
        if (locations.size() > 0) {
            for (HashMap<String, String> loc : locations) {
                // create a location object
                LatLng latLng = new LatLng(Float.parseFloat(loc.get("lat")), Float.parseFloat(loc.get("lng")));

                // create a marker object from location object
                mMap.addMarker(new MarkerOptions().position(latLng).title(loc.get("username")+" had "+loc.get("speed")+" speed"));
            }
        } else {
            Toast.makeText(this, "No locations were found!", Toast.LENGTH_SHORT);
        }

    }
}
