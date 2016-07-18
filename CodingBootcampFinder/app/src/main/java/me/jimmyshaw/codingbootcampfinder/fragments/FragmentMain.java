package me.jimmyshaw.codingbootcampfinder.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.jimmyshaw.codingbootcampfinder.R;
import me.jimmyshaw.codingbootcampfinder.models.Camp;
import me.jimmyshaw.codingbootcampfinder.services.DataService;

public class FragmentMain extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MarkerOptions mUserMarker;

    private final int ZOOM_LEVEL = 15;

    private EditText mEditTextZipCode;

    private int mZipCode;

    public FragmentMain() {
        // Required empty public constructor
    }

    public static FragmentMain newInstance() {
        FragmentMain fragment = new FragmentMain();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        captureUserInputZipCodeFromEditText(view);

        return view;
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

    }

    public void setUserMarker(LatLng latLng) {
        // If there's no user market, we create one based on the passed in lat and long.
        if (mUserMarker == null) {
            Log.d("FragmentMain", "Current location: " + "lat " + latLng.latitude + " lng " + latLng.longitude);
            mUserMarker = new MarkerOptions().position(latLng).title("Current location");
            mMap.addMarker(mUserMarker);

        }

        try {
            // Use Geocoder to automatically set the user marker at the user's present location.
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            mZipCode = Integer.parseInt(addresses.get(0).getPostalCode());

            updateMapForZipCode(mZipCode);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        updateMapForZipCode(94102);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
    }

    private void updateMapForZipCode(int zipCode) {
        // Get a list of camps from our data service singleton.
        ArrayList<Camp> campsList = DataService.getInstance().getCampLocationsWithinTenMilesOfZipCode(zipCode);

        for (int i = 0; i < campsList.size(); i++) {
            // For each camp in our camps list, add that camp's lat and lng plus other fields
            // to a new map marker object. Finally, add the new marker to our map.
            Camp camp = campsList.get(i);

            MarkerOptions marker = new MarkerOptions().position(new LatLng(camp.getLatitude(), camp.getLongitude()));
            marker.title(camp.getLocationTitle());
            marker.snippet(camp.getLocationAddress());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));

            mMap.addMarker(marker);
        }
    }

    private void captureUserInputZipCodeFromEditText(View view) {
        mEditTextZipCode = (EditText) view.findViewById(R.id.edit_text_zip_code);
        mEditTextZipCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    // TODO: Add validation
                    String userInput = mEditTextZipCode.getText().toString();
                    mZipCode = Integer.parseInt(userInput);

                    // We get rid of the keyboard after the user submits the edit text input.
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEditTextZipCode.getWindowToken(), 0);

                    updateMapForZipCode(mZipCode);

                    return true;
                }

                return false;
            }
        });
    }

}
