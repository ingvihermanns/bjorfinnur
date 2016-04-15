package is.bjorfinnur.bjorfinnur.tabs;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.GpsCoordinates;
import is.bjorfinnur.bjorfinnur.database.DataBaseManager;
import is.bjorfinnur.bjorfinnur.util.MapUtil;

public class SecondTab extends Activity {
    /** Called when the activity is first created. */

    private DataBaseManager dataBaseManager;
    ListView listView;
    private float[] distance;
    private String[] names;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBaseManager = DataBaseManager.getDatabaseManager(this);
        List<Beer> barList = dataBaseManager.searchBeers("");

/* Second Tab Content */
        TextView textView = new TextView(this);
        textView.setText("Second Tab");
        setContentView(textView);
    }

    public void search(String query){
        Log.i("Info", "Query recieved: " + query);
        List<Bar> barList = dataBaseManager.searchBars(query);
        //newBarList(barList);
    }


    public List<GpsCoordinates> populateDistance(String query){
        List<GpsCoordinates> gpscordlist = dataBaseManager.getBarCoordinates(query);
        getDistance(gpscordlist);
        return gpscordlist;
    }

    public float[] getDistance(List<GpsCoordinates> gpscordlist){
        float[] results = calculateDistance(gpscordlist);
        for(int i = 0; i < results.length; i++){
            Log.i("Grilli", "fongum hnitin: " + results[i]);
        }
        distance = results;
        return results;
    }

    public String[] getNames(String query){
        ArrayList<String> namelist = dataBaseManager.getBarNames(query);
        String[] results = new String[namelist.size()];
        for(int i = 0; i<results.length; i++){
            results[i] = namelist.get(i);
            Log.i("Grilli", "fongum nafnid: " + results[i]);
        }
        names = results;
        sort();
        return results;
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

    public float[] calculateDistance(List<GpsCoordinates> gpslist) {

        float[] results = new float[gpslist.size()];

        Location mylocation = getMyLocation();
        Location loc1 = new Location("");
        loc1.setLatitude(mylocation.getLatitude());
        loc1.setLongitude(mylocation.getLongitude());

        for (int i = 0; i < gpslist.size(); i++) {
            GpsCoordinates gpscord = gpslist.get(i);
            Location loc2 = new Location("");
            loc2.setLatitude(gpscord.getLatitude());
            loc2.setLongitude(gpscord.getLongtitude());
            float distanceInMeters = loc1.distanceTo(loc2);
            results[i] = distanceInMeters;
        }

        return results;

    }

    public void sort(){
        Map<Float,String> byDist = new TreeMap<>();
        Log.i("Grilli","dist: " + distance.length + " name: " + names.length);
        for(int i=0;i<distance.length;i++) {
            byDist.put(distance[i], names[i]);
        }
        byDist = MapUtil.sortByValue(byDist);
        for(int i = 0;i<distance.length;i++){
            //Log.i("Grilli", byDist.get(i));
        }
    }

    public void sortHigh2Low(){

    }

    public void sortLow2High(){

    }

}