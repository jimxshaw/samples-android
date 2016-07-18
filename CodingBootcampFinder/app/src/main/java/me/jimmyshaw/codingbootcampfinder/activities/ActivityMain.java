package me.jimmyshaw.codingbootcampfinder.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import me.jimmyshaw.codingbootcampfinder.R;
import me.jimmyshaw.codingbootcampfinder.fragments.FragmentMain;

public class ActivityMain extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private final int PERMISSION_LOCATION = 100;

    private FragmentMain mFragmentMain;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        loadFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // This method is called by our Google API services client after a connection has been
        // established. Where the connection actually happens is within this activity's onStart and
        // the disconnection is within this activity's onStop.

        // However, we only start location services if the user gives permission. If permission hasn't
        // been granted then we request it.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // When we request permissions, we use a string array to place any number of permissions
            // in this particular request. In this case, we're only requesting one permission, the
            // fine location permission. We use an arbitrary constant int value as the identifier of
            // this permission request.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.d("ActivityMain", "Requesting permissions");
        }
        else {
            // If permission has already been granted then do start the location services.
            Log.d("ActivityMain", "onConnected start location services");
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("ActivityMain", "lat: " + location.getLatitude() + " " + "lng: " + location.getLongitude());
        // Our fragment, which displays the map, need to know the user's location.
        mFragmentMain.setUserMarker(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // The request code constant is simply an arbitrary code that acts as an identifier of a
        // permission request.
        switch (requestCode) {
            case PERMISSION_LOCATION:
                // We check if there's at least one result of permission being granted and check that
                // the first result has actually been granted. If all of that is true then start
                // the location services.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ActivityMain", "onRequestPermissionResult start location services");
                    startLocationServices();
                }
                else {
                    // If permission is denied, show a dialog stating that location services cannot
                    // run if permission is denied.
                    Log.d("ActivityMain", "Permission denied, cannot start location services");
                }
                break;
        }
    }

    private void loadFragment() {
        mFragmentMain = (FragmentMain) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout_container_main);

        if (mFragmentMain == null) {
            mFragmentMain = mFragmentMain.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_container_main, mFragmentMain)
                    .commit();
        }
    }

    public void startLocationServices() {
        // After the Google API Client is connected, we start our location services.
        Log.d("ActivityMain", "startLocationServices called");

        // We use a try-catch because location services need explicit permission approval from the
        // user. If permission is denied then the code drops to the catch statement where, for example,
        // we show a dialog saying we can't get the location unless user permission is granted.
        try {
            // Setting the priority as low power means we're wanting city-level accuracy.
            LocationRequest request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
        }
        catch (SecurityException ex) {
            Log.d("ActivityMain", ex.toString());
            ex.printStackTrace();
        }
    }

}
