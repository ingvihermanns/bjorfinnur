package is.bjorfinnur.bjorfinnur;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScreenActivity extends TabActivity {
    private static final int FIRST_TAB_POSITION = 0;
    private static final int SECOND_TAB_POSITION = 1;
    /** Called when the activity is first created. */
    private SearchView searchView;
    private String lastQuery = "";
    private Button searchButton;
    private TabSpec firstTabSpec, thirdTabSpec;
    private TabHost tabHost;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        
        /** TabHost will have Tabs */
        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        /** TabSpec used to create a new tab.
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */

        /** tid1 is firstTabSpec Id. Its used to access outside. */
        firstTabSpec = tabHost.newTabSpec("tid1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tid2");
        thirdTabSpec = tabHost.newTabSpec("tid3");

        /** TabSpec setIndicator() is used to set name for the tab. */
        /** TabSpec setContent() is used to set content for a particular tab. */
        firstTabSpec.setIndicator("Bjórar").setContent(new Intent(this,FirstTab.class));
        secondTabSpec.setIndicator("Staðir").setContent(new Intent(this, SecondTab.class));
        thirdTabSpec.setIndicator("Test").setContent(new Intent(this, ThirdTab.class));

        /** Add tabSpec to the TabHost to display. */
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);
        tabHost.addTab(thirdTabSpec);

        setUpMapButton();

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lastQuery = query;
                MainScreenActivity.this.callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


    }

    private void callSearch(String query) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity instanceof FirstTab) {
            Log.e("Info", "Query sent: " + query);
            ((FirstTab) currentActivity).search(query);
        }
        else if(currentActivity instanceof SecondTab){
            Log.e("Info", "fongum hnitin: " + query);
            ((SecondTab) currentActivity).search(query);
            ((SecondTab) currentActivity).populateDistance(query);
        }
    }

    private List<Gpscordinates> callMap(String query) {
        Activity currentActivity = getCurrentActivity();
        List<Gpscordinates> results = new ArrayList<Gpscordinates>();
        if (currentActivity instanceof FirstTab) {
            Log.e("Info", "Query sent: " + query);
            results = ((FirstTab) currentActivity).populateMap(query);
        }
        return results;
    }

    private void setUpMapButton() {
        Button MapButton = (Button) findViewById(R.id.mapbutton);
        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Gpscordinates> gpscord = callMap(lastQuery);
                ArrayList<String> latitude = new ArrayList<String>();
                ArrayList<String> longitude = new ArrayList<String>();
                for (int i = 0; i < gpscord.size(); i++) {
                    Gpscordinates gpscordinates = gpscord.get(i);
                    latitude.add(gpscordinates.getLatitude() + "");
                    longitude.add(gpscordinates.getLongtitude() + "");
                }
                Intent i = new Intent(MainScreenActivity.this, MapsActivity.class);
                i.putStringArrayListExtra("latitude",latitude);
                i.putStringArrayListExtra("longitude",longitude);
                startActivity(i);
            }
        });
    }


}