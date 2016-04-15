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

public class BeerTab extends Activity{

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
        Log.e("Info", "Query recieved in beerTab: " + query);
        refreshAdapter(query);
    }

    private List<Beer> searchForBeers(String query){
        return dataBaseManager.searchBeers2(query);
    }

    private void refreshAdapter(String query){
        setUpData(query);
        setAdapter(new ExpandableListAdapter(this, listDataHeader, listDataChild));
    }

    private void setUpData(String query){
        clearData();

        List<Beer> beerList = searchForBeers(query);
        for(Beer beer: beerList){
            List<Pair<Bar, Price>> bars = dataBaseManager.getBarsFromBeer(beer);
            addBeerCard(beer, bars);
        }
    }

    private void setAdapter(ExpandableListAdapter adapter){
        expandableListView.setAdapter(adapter);
    }

    private void clearData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
    }

    private void addBeerCard(Beer beer, List<Pair<Bar, Price>> bars) {
        List<String> locations = new ArrayList<>();
        String text = "";
        text += "Type: \"" + beer.getType() + "\"\n";
        text += "Manufacturer: \"" + beer.getManufacturer() + "\"\n";
        text += "Description: \"" + beer.getDescription() + "\"\n";
        text += "Available at the following bars: \"" + "\n";
        for(Pair<Bar, Price> pair: bars) {
            Bar bar = pair.first;
            Price price = pair.second;
            text += "    " + bar.getName() + " " + price.getUnits() + " " + price.getCurrency() + "\n";
        }
        text += "\"";
        locations.add(text);

        listDataHeader.add(beer.getName());
        listDataChild.put(beer.getName(), locations);
    }
}


