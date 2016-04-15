package is.bjorfinnur.bjorfinnur.activities;

import android.content.Context;
import android.content.Intent;
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

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap;

    private ArrayList<Float> latitudes;
    private ArrayList<Float> longitudes;
    private ArrayList<String> barNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        ArrayList<String> stringLatitudes = getIntent().getStringArrayListExtra("barLatitudes");
        ArrayList<String> stringLongitudes = getIntent().getStringArrayListExtra("barLongitudes");
        barNames = getIntent().getStringArrayListExtra("barNames");

        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();

        for(String lat: stringLatitudes){
            latitudes.add(Float.parseFloat(lat));
        }
        for(String lon: stringLongitudes){
            longitudes.add(Float.parseFloat(lon));
        }

        Log.e("Info", "Invoking map with:");
        for(int i=0; i<barNames.size(); i++){
            Log.e("Info", "BarName: " + barNames.get(i) + " Latidue: " + longitudes.get(i) + " Longitude: " + longitudes.get(i));
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


        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bjorfinnur);

        for (int i = 0; i < barNames.size(); i++) {
            String barName = barNames.get(i);
            float latitude = latitudes.get(i);
            float longitude = longitudes.get(i);
            LatLng latlng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title(barName).icon(icon);
            mMap.addMarker(markerOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));//Moves the camera to users current longitude and latitude
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,(float) 14));


    }

    public static void launchIntent(Context context, ArrayList<String> barNames, ArrayList<String> barLatitudes, ArrayList<String> barLongitudes) {
        Intent i = new Intent(context, MapActivity.class);
        i.putStringArrayListExtra("barNames", barNames);
        i.putStringArrayListExtra("barLatitudes", barLatitudes);
        i.putStringArrayListExtra("barLongitudes", barLongitudes);
        context.startActivity(i);
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
