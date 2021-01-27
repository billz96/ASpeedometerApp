package gr.example.zografos.vasileios.aspeedometerapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationListActivity extends AppCompatActivity {

    DBHelper dbHelper;
    public static final String USER_PREF = "UserPref" ;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        dbHelper = new DBHelper(this);

        sharedPrefs = getSharedPreferences(USER_PREF, MODE_PRIVATE);
        String username = sharedPrefs.getString("username", " ");

        TextView listHead = findViewById(R.id.listHead);
        listHead.setText(username+"'s list");

        TextView locationsTxt = findViewById(R.id.listBody);
        ArrayList<HashMap<String, String>> locations = dbHelper.findLocations(username);
        String result = "";
        if (locations.size() > 0) {
            for (int i = 0; i < locations.size(); i++) {
                String lat = locations.get(i).get("lat");
                String lng = locations.get(i).get("lng");
                String speed = locations.get(i).get("speed");
                String timestamp = locations.get(i).get("timestamp");

                result += (i+1)+".\n\ttimestamp = " + timestamp + "\n\tlat = " + lat + "\n\tlng = " + lng + "\n\tspeed = " + speed + "\n";
            }
        } else {
            result = "Empty";
        }

        locationsTxt.setText(result);
    }
}
