package com.pratamawijaya.panggilpeta.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pratamawijaya.panggilpeta.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private double lat, lng;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location location;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map);
        ButterKnife.inject(this);
        setUpMapIfNeeded();


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this) // connection callback
                .addOnConnectionFailedListener(this) // when connection failed
                .addApi(LocationServices.API) // called api
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Starting Point")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
        );
        lat = latLng.latitude;
        lng = latLng.longitude;
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        Toast.makeText(this, "fab click", Toast.LENGTH_SHORT).show();
        Bundle data = new Bundle();
        data.putDouble("lat", lat);
        data.putDouble("lng", lng);
        Intent resultIntent = new Intent();
        resultIntent.putExtras(data);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (location == null) {
            // get last location device
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("Starting Point")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                );
                lat = location.getLatitude();
                lng = location.getLongitude();

                Toast.makeText(this, "Lokasi kamu saat ini, sebagai patokan titik awal perjalanan kamu kak :')", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
