package is.bjorfinnur.bjorfinnur.activities;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import is.bjorfinnur.bjorfinnur.barlist.BarListActivity;
import is.bjorfinnur.bjorfinnur.beerlist.BeerListActivity;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.tabs.BarTab;
import is.bjorfinnur.bjorfinnur.tabs.BeerTab;

public class MainScreenActivity extends TabActivity {

    private String mostRecentQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        /** TabHost will have Tabs */
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        /** TabSpec used to create a new tab.
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */

        /** tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec beerListSpec = tabHost.newTabSpec("idBeerList");
        TabSpec barListSpec = tabHost.newTabSpec("idBarList");

        /** TabSpec setIndicator() is used to set name for the tab. */
        /** TabSpec setContent() is used to set content for a particular tab. */
        beerListSpec.setIndicator("Beers").setContent(new Intent(this, BeerListActivity.class));
        barListSpec.setIndicator("Bars").setContent(new Intent(this, BarListActivity.class));

        /** Add tabSpec to the TabHost to display. */
        //tabHost.addTab(firstTabSpec);
        tabHost.addTab(beerListSpec);
        tabHost.addTab(barListSpec);

        setUpMapButton();
        setUpSortButtons();

        /* Called when the activity is first created. */
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mostRecentQuery = query;
                MainScreenActivity.this.callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("querywin", newText);
                if(newText.equals(" ")){
                    mostRecentQuery = "";
                    MainScreenActivity.this.callSearch(mostRecentQuery);
                }
                return true;
            }

        });


        searchView.clearFocus();

    }

    private void setUpSortButtons() {
        setUpSortByNameButton();
        setUpSortByPriceButton();
    }

    private void setUpSortByPriceButton() {
        Button button = (Button) findViewById(R.id.sortButtonPrice);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainScreenActivity.this.sortByPrice(MainScreenActivity.this.mostRecentQuery);
            }
        });
    }

    private void setUpSortByNameButton() {
        Button button = (Button) findViewById(R.id.sortButtonName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainScreenActivity.this.sortByName(MainScreenActivity.this.mostRecentQuery);
            }
        });

    }


    private void sortByPrice(String query) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity instanceof BeerListActivity) {
            ((BeerListActivity) currentActivity).sortByPrice(query);
        } else if (currentActivity instanceof BarListActivity) {
            ((BarListActivity) currentActivity).sortByPrice(query);
        }
    }

    private void sortByName(String query) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity instanceof BeerListActivity) {
            ((BeerListActivity) currentActivity).sortByName(query);
        } else if (currentActivity instanceof BarListActivity) {
            ((BarListActivity) currentActivity).sortByName(query);
        }
    }

    private void callSearch(String query) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity instanceof BeerListActivity) {
            Log.e("Info", "Query sent: " + query);
            ((BeerListActivity) currentActivity).search(query);
        } else if (currentActivity instanceof BarListActivity) {
            Log.e("Info", "Query sent: " + query);
            ((BarListActivity) currentActivity).search(query);
        }
    }


    private void setUpMapButton() {
        Button mapButton = (Button) findViewById(R.id.mapbutton);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bar> bars =  getBarNames();
                ArrayList<String> barNames = new ArrayList<>();
                ArrayList<String> barLatitudes = new ArrayList<>();
                ArrayList<String> barLongitudes = new ArrayList<>();
                for(Bar bar: bars){
                    barNames.add(bar.getName());
                    barLatitudes.add(bar.getLatitude());
                    barLongitudes.add(bar.getLongitude());
                }
                MapActivity.launchIntent(MainScreenActivity.this, barNames, barLatitudes, barLongitudes);
            }

            private List<Bar> getBarNames() {
                DatabaseManager databaseManager = DatabaseManager.getInstance(MainScreenActivity.this);
                Activity currentActivity = getCurrentActivity();
                List<Bar> barList = new ArrayList<>();
                if (currentActivity instanceof BeerTab || currentActivity instanceof BeerListActivity) {
                    List<Beer> beers = databaseManager.searchBeers2(mostRecentQuery);
                    Set<Bar> bars = new TreeSet<>();
                    for(Beer beer: beers){
                        for(Pair<Bar, Price> pair: databaseManager.getBarsFromBeer(beer)){
                            bars.add(pair.first);
                        }
                    }
                    barList.addAll(bars);
                } else if (currentActivity instanceof BarTab) {
                    barList.addAll(databaseManager.searchBars2(mostRecentQuery));
                }
                return barList;
            }
        });
    }


}