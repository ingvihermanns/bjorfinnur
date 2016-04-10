package is.bjorfinnur.bjorfinnur;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThirdTab extends Activity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_tab);

        setUpData();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);
    }

    private void setUpData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        ArrayList<String> kari = new ArrayList<String>(Arrays.asList("You idiot.", "#REKT"));
        ArrayList<String> arnar = new ArrayList<String>(Arrays.asList("Meistari.", "Haha"));
        listDataHeader.add("Skilaboð til Kára!");
        listDataHeader.add("Meistari!");
        listDataChild.put(listDataHeader.get(0), kari);
        listDataChild.put(listDataHeader.get(1), arnar);
    }
}