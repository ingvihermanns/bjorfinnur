package is.bjorfinnur.bjorfinnur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeerTab extends Activity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private DataBaseManager dataBaseManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_tab);

        dataBaseManager = DataBaseManager.getDatabaseManager(this);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // setting list adapter
        refreshAdapter("");
    }

    public List<Beer> search(String query){
        Log.e("Info", "Query recieved: " + query);
        List<Beer> beerList = dataBaseManager.searchBeers(query);
        return beerList;
    }

    private void refreshAdapter(String query){
        setUpData(query);
        setAdapter(new ExpandableListAdapter(this, listDataHeader, listDataChild));
    }

    private void setAdapter(ExpandableListAdapter adapter){
        expandableListView.setAdapter(adapter);
    }

    private void setUpData(String query){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        List<Beer> beerList = search(query);
        for(Beer beer: beerList){
            Map<String, Price> places = dataBaseManager.getBarsFromBeer(beer);
            addBeerCard(beer, listDataHeader, listDataChild, places);
        }
    }

    private static void addBeerCard(Beer beer, List<String> listDataHeader, HashMap<String, List<String>> listDataChild, Map<String, Price> places) {
        String beerName = beer.getBeerName();
        listDataHeader.add(beerName);
        List<String> locations = new ArrayList<>();
        for(String key: places.keySet()){
            int pricekr = places.get(key).getUnits();
            String loc = key;
            loc += " " + pricekr;
            locations.add(loc);
        }
        listDataChild.put(beerName, locations);
    }
}


