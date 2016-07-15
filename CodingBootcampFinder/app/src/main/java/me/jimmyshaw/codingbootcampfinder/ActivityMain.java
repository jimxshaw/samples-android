package me.jimmyshaw.codingbootcampfinder;

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            Log.d("ActivityMain", "Requesting permissions");
        }
        else {
            // If permission has already been granted then do start the location services.
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
