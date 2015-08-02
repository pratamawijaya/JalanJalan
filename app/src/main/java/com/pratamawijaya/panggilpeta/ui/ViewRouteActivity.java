package com.pratamawijaya.panggilpeta.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.pratamawijaya.panggilpeta.BaseApplication;
import com.pratamawijaya.panggilpeta.Constant;
import com.pratamawijaya.panggilpeta.R;
import com.pratamawijaya.panggilpeta.helper.Utils;
import com.pratamawijaya.panggilpeta.json.Legs;
import com.pratamawijaya.panggilpeta.json.Steps;
import com.pratamawijaya.panggilpeta.models.LatLngModel;
import com.pratamawijaya.panggilpeta.models.LocationModel;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ViewRouteActivity extends FragmentActivity {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private double latAwal, latTujuan, lngAwal, lngTujuan;
  private Bundle data;

  private String tujuan;
  private List<LatLng> latLngList;
  private com.pratamawijaya.panggilpeta.json.Response response;
  private ProgressDialog progressDialog;
  private Realm realm;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_route);

    realm = Realm.getInstance(this);
    progressDialog = new ProgressDialog(this);

    latLngList = new ArrayList<>();
    progressDialog.setMessage("Loading...");

    setUpMapIfNeeded();

    data = getIntent().getExtras();
    if (data.getInt("status") == MainActivity.FROM_LOCAL) {
      // baca db
      latLngList = readDataFromDB(data.getString("id"));

      // draw
      drawDirectionToMap(latLngList);
    } else {
      latAwal = data.getDouble("latAwal");
      lngAwal = data.getDouble("lngAwal");

      latTujuan = data.getDouble("latTujuan");
      lngTujuan = data.getDouble("lngTujuan");
      tujuan = data.getString("tujuan");

      getDirections(getGoogleDirectionsUrl(latAwal, lngAwal, latTujuan, lngTujuan));
    }
  }

  private List<LatLng> readDataFromDB(String id) {
    RealmResults<LocationModel> result =
        realm.where(LocationModel.class).equalTo("id", id).findAll();
    List<LatLng> listlatlng = new ArrayList<>();
    for (LatLngModel data : result.get(0).getLatLngRealmList())
      listlatlng.add(new LatLng(data.getLat(), data.getLng()));

    return listlatlng;
  }

  /**
   * get URL for directions
   */
  private String getGoogleDirectionsUrl(double latAwal, double lngAwal, double latTujuan,
      double lngTujuan) {
    return "https://maps.googleapis.com/maps/api/directions/json?origin=" + latAwal + "," + lngAwal
        + "&destination=" + latTujuan + "," + lngTujuan + "&key=" + Constant.API_KEY;
    //        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + latAwal + "," + lngAwal + "&destination=" + latTujuan + "," + lngTujuan;
  }

  private void getDirections(String URL) {
    JsonObjectRequest request =
        new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
          @Override public void onResponse(JSONObject jsonObject) {
            progressDialog.dismiss();
            response = new Gson().fromJson(jsonObject.toString(),
                com.pratamawijaya.panggilpeta.json.Response.class);
            Log.d("debug", "" + response.getRouteList().get(0).getLegsList().get(0).getDistance());

            //  Fetch Directions From Json
            fetchDirection(response.getRouteList().get(0).getLegsList().get(0).getStepsList());

            saveToDb(response.getRouteList().get(0).getLegsList().get(0));
            // TODO: Save to DB
          }
        }, new Response.ErrorListener() {
          @Override public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
            Log.d("error", "" + volleyError.toString());
          }
        });
    progressDialog.show();
    BaseApplication.getInstance().addToRequestQueue(request, "tag");
  }

  /**
   * save to db
   */
  private void saveToDb(Legs legs) {
    realm.beginTransaction();
    LocationModel locationModel = realm.createObject(LocationModel.class);
    locationModel.setId(Utils.getCurrentTimestamp());
    locationModel.setAsal(legs.getStart_address());
    locationModel.setTujuan(tujuan);
    locationModel.setDistance(legs.getDistance().getText());
    locationModel.setDuration(legs.getDuration().getText());

    for (Steps data : legs.getStepsList()) {
      Log.d("tag", "save to db ---");
      // start loc
      LatLngModel latlngStartLoc = realm.createObject(LatLngModel.class);
      latlngStartLoc.setLat(data.getStartLocation().getLat());
      latlngStartLoc.setLng(data.getStartLocation().getLng());
      // add to realmList
      locationModel.getLatLngRealmList().add(latlngStartLoc);

      // polyline
      List<LatLng> decodedPoly = Utils.decodePoly(data.getPolyline().getPoints());
      for (LatLng point : decodedPoly) {
        LatLngModel latlngPoly = realm.createObject(LatLngModel.class);

        latlngPoly.setLat(point.latitude);
        latlngPoly.setLng(point.longitude);
        // add to realmList
        locationModel.getLatLngRealmList().add(latlngPoly);
      }

      // end loc
      LatLngModel latlngEndLoc = realm.createObject(LatLngModel.class);
      latlngEndLoc.setLat(data.getEndLocation().getLat());
      latlngEndLoc.setLng(data.getEndLocation().getLng());
    }

    realm.commitTransaction();
  }

  private void fetchDirection(List<Steps> stepsList) {
    Log.d("debug", "size list awal: " + latLngList.size());
    for (Steps data : stepsList) {
      // add start
      latLngList.add(
          new LatLng(data.getStartLocation().getLat(), data.getStartLocation().getLng()));

      // decode poly
      List<LatLng> decodedPoly = Utils.decodePoly(data.getPolyline().getPoints());
      for (LatLng point : decodedPoly)
        latLngList.add(new LatLng(point.latitude, point.longitude));

      latLngList.add(new LatLng(data.getEndLocation().getLat(), data.getEndLocation().getLng()));
      // add end
    }
    Log.d("debug", "size list akhir : " + latLngList.size());

    drawDirectionToMap(latLngList);
  }

  private void drawDirectionToMap(List<LatLng> latLngList) {
    PolylineOptions line = new PolylineOptions().width(3).color(Color.BLUE);
    for (int i = 0; i < latLngList.size(); i++) {
      line.add(latLngList.get(i));
    }
    mMap.addPolyline(line);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
        new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude), 14));

    // add marker diawal
    mMap.addMarker(new MarkerOptions().position(
        new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude))
        .title("starting point")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

    // add marker di akhir
    int index = latLngList.size() - 1;
    mMap.addMarker(new MarkerOptions().position(
        new LatLng(latLngList.get(index).latitude, latLngList.get(index).longitude))
        .title("end point")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

    Toast.makeText(this, "Jalur berhasil digambar kak, hati2 dijalan pelan2 saja,,, ",
        Toast.LENGTH_SHORT).show();
  }

  @Override protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
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
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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
    mMap.setMyLocationEnabled(true);
    mMap.getUiSettings().setZoomControlsEnabled(true);
  }
}
