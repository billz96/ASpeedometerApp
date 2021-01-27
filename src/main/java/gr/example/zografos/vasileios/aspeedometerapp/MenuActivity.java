package gr.example.zografos.vasileios.aspeedometerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import android.os.Build;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.annotation.TargetApi;

public class MenuActivity extends AppCompatActivity{

    public static final String USER_PREF = "UserPref";
    SharedPreferences sharedPrefs;
    TextView locationTxt;
    LocationTracker tracker;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        sharedPrefs = getSharedPreferences(USER_PREF, MODE_PRIVATE);
        String username = sharedPrefs.getString("username", " ");

        TextView welcomeTxt = findViewById(R.id.welcomeTxt);
        welcomeTxt.setText("Welcome "+username+" !");

        Button locListBtn = findViewById(R.id.locListBtn);
        locListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, LocationListActivity.class);
                startActivity(i);
            }
        });

        Button locMapBtn = findViewById(R.id.locMapBtn);
        locMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, LocationMapActivity.class);
                startActivity(i);
            }
        });

        locationTxt = findViewById(R.id.locationTxt);

        tracker = new LocationTracker(this, locationTxt, username);

        if (!tracker.canGetLocation()) {
            // ask for permission
            tracker.showSettingsAlert();
        } else {
            tracker.getLocation();
            float speed = tracker.getSpeed();
            double longitude = tracker.getLongitude();
            double latitude = tracker.getLatitude();

            if (speed < 1.0f) {
                locationTxt.setBackgroundColor(Color.rgb(0, 153, 51));
                locationTxt.setTextColor(Color.WHITE);
            } else {
                locationTxt.setBackgroundColor(Color.rgb(255, 204, 102));
                locationTxt.setTextColor(Color.rgb(204, 51, 0));
            }

            locationTxt.setText(" Your current location is:\n"+" longitude: "+longitude+","+" latitude: "+latitude+","+" speed: "+speed);
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MenuActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tracker.stopListener();
    }
}
