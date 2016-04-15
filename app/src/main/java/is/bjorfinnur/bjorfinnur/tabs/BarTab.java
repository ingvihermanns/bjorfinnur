package is.bjorfinnur.bjorfinnur.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.bjorfinnur.bjorfinnur.database.DataBaseManager;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;

public class BarTab extends Activity{

    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private Map<String, List<String>> listDataChild;
    private DataBaseManager dataBaseManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        dataBaseManager = DataBaseManager.getDatabaseManager(this);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        // setting list adapter
        refreshAdapter("");
    }

    public void search(String query){
        Log.e("Info", "Query recieved in barTab: " + query);
        refreshAdapter(query);
    }

    private List<Bar> searchForBars(String query) {
        return dataBaseManager.searchBars2(query);
    }

    private void refreshAdapter(String query){
        setUpData(query);
        setAdapter(new ExpandableListAdapter(this, listDataHeader, listDataChild));
    }

    private void setUpData(String query){
        clearData();

        List<Bar> barList = searchForBars(query);
        for(Bar bar: barList){
            List<Pair<Beer, Price>> beers = dataBaseManager.getBeersFromBar(bar);
            addBarCard(bar, beers);
        }
    }



    private void setAdapter(ExpandableListAdapter adapter){
        expandableListView.setAdapter(adapter);
    }

    private void clearData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
    }

    private void addBarCard(Bar bar, List<Pair<Beer, Price>> beers) {


        List<String> locations = new ArrayList<>();
        String text = "";
        text += "Description: \"" + bar.getDescription() + "\"\n";
        text += "Address: \"" + bar.getAddress() + "\"\n";
        text += "Lat: \"" + bar.getLatitude() + "\"\n";
        text += "Lon: \"" + bar.getLongitude() + "\"\n";
        text += "Beers on offer: \"" + "\n";
        for(Pair<Beer, Price> pair: beers) {
            Beer beer = pair.first;
            Price price = pair.second;
            text += "    " + beer.getName() + " " + price.getUnits() + " " + price.getCurrency() + "\n";
        }
        text += "\"";
        locations.add(text);

        listDataHeader.add(bar.getName());
        listDataChild.put(bar.getName(), locations);
    }
}


