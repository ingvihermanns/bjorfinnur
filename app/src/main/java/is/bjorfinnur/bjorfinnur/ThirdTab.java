package is.bjorfinnur.bjorfinnur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThirdTab extends Activity {

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
            addBeer(beer, listDataHeader, listDataChild);
        }
    }

    private static void addBeer(Beer beer, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        String beerName = beer.getBeerName();
        listDataHeader.add(beerName);
        List<String> locations = new ArrayList<>();
        locations.add("location 1");
        locations.add("location 2");

        listDataChild.put(beerName, locations);

    }
}


