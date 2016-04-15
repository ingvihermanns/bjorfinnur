package is.bjorfinnur.bjorfinnur.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private float[] latitude;
    private float[] longtitude;
    private String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ArrayList<String> lat = getIntent().getStringArrayListExtra("latitude");
        ArrayList<String> lon = getIntent().getStringArrayListExtra("longitude");
        ArrayList<String> name = getIntent().getStringArrayListExtra("barname");

        latitude = new float[lat.size()];
        longtitude = new float[lon.size()];
        names = new String[name.size()];
        for (int i = 0; i < latitude.length; i++) {
            latitude[i] = Float.parseFloat(lat.get(i));
            longtitude[i] = Float.parseFloat(lon.get(i));
            names[i] = name.get(i);
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


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

    public Location getMyLocation() {

        // Get location from GPS if it's available
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Location wasn't found, check the next most accurate place for the current location
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            // Finds a provider that matches the criteria
            String provider = lm.getBestProvider(criteria, true);
            // Use the provider to get the last known location
            myLocation = lm.getLastKnownLocation(provider);
        }
        if(myLocation == null){
            myLocation = new Location(""); //64.14
            myLocation.setLatitude(64.14);
            myLocation.setLongitude(-21.93);
        }

        return myLocation;
    }





    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        Location location = getMyLocation();
        LatLng myLocation;
        if(location == null){
            myLocation = new LatLng(0, 0);
        }else{
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        }
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker"));

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bjorfinnur);
        for (int i = 0; i < latitude.length; i++) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(latitude[i], longtitude[i])).title("Marker").icon(icon).title(names[i]));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));//Moves the camera to users current longitude and latitude
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,(float) 14));


    }


    /*

    private static float[] calculateDistance(List<GpsCoordiantes> GpsList) {

        float[] results = new float[GpsList.size()];

        Location mylocation = getMyLocation();
        Location loc1 = new Location("");
        loc1.setLatitude(mylocation.getLatitude());
        loc1.setLongitude(mylocation.getLongitude());


        for (int i = 0; i < GpsList.size(); i++) {
            GpsCoordinates gpscord = GpsList.get(i);
            Location loc2 = new Location("");
            loc2.setLatitude();
            loc2.setLongitude();
            float distanceInMeters = loc1.distanceTo(loc2);
            results[i] = distanceInMeters;
        }

        return results;

    }
    */
}
