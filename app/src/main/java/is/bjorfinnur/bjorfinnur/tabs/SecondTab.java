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
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.util.MapUtil;

public class SecondTab extends Activity {
    /** Called when the activity is first created. */

    private DatabaseManager databaseManager;
    private float[] distance;
    private String[] names;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseManager = DatabaseManager.getInstance(this);
        List<Beer> barList = databaseManager.searchBeers("");

        /* Second Tab Content */
        TextView textView = new TextView(this);
        textView.setText("Second Tab");
        setContentView(textView);
    }

    public void search(String query){
        Log.i("Info", "Query recieved: " + query);
        List<Bar> barList = databaseManager.searchBars(query);
        //newBarList(barList);
    }


    public List<GpsCoordinates> populateDistance(String query){
        List<GpsCoordinates> gpscordlist = databaseManager.getBarCoordinates(query);
        getDistance(gpscordlist);
        return gpscordlist;
    }

    private float[] getDistance(List<GpsCoordinates> gpscordlist){
        float[] results = calculateDistance(gpscordlist);
        for(int i = 0; i < results.length; i++){
            Log.i("Grilli", "fongum hnitin: " + results[i]);
        }
        distance = results;
        return results;
    }

    public String[] getNames(String query){
        ArrayList<String> namelist = databaseManager.getBarNames(query);
        String[] results = new String[namelist.size()];
        for(int i = 0; i<results.length; i++){
            results[i] = namelist.get(i);
            Log.i("Grilli", "fongum nafnid: " + results[i]);
        }
        names = results;
        sort();
        return results;
    }


    private float[] calculateDistance(List<GpsCoordinates> gpslist) {

        float[] results = new float[gpslist.size()];

        Location mylocation = MapUtil.getMyLocation(this);

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

    private void sort(){
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
/*
    public void sortHigh2Low(){

    }

    public void sortLow2High(){

    }
*/
}