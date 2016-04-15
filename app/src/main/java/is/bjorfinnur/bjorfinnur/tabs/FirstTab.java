package is.bjorfinnur.bjorfinnur.tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.GpsCoordinates;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;

public class FirstTab extends Activity {
    /** Called when the activity is first created. */
    private DatabaseManager databaseManager;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_tab);

        databaseManager = DatabaseManager.getInstance(this);
        List<Beer> beerList = databaseManager.searchBeers("");

        listView = (ListView) findViewById(R.id.listView);
        setAdapter(new BeerListArrayAdapter(this, R.layout.beer_list_row, beerList));


    }

    public void search(String query){
        Log.e("Info", "Query recieved: " + query);
        List<Beer> beerList = databaseManager.searchBeers(query);
        newBeerList(beerList);
    }

    /*public List<GpsCoordinates> populateMap(String query){
        List<GpsCoordinates> gpscordList = databaseManager.getBeerCoordinates(query);
        return gpscordList;
    }*/

  /*  public ArrayList<String> populateBarNames(String query){
        ArrayList<String> barNames = databaseManager.getBarName(query);
        return barNames;
    }*/

    private void newBeerList(List<Beer> beerList){
        setAdapter(new BeerListArrayAdapter(this, R.layout.beer_list_row, beerList));
    }

    private void setAdapter(BeerListArrayAdapter adapter){
        listView.setAdapter(adapter);
    }

/*    private void resetListView(){
        listView.deferNotifyDataSetChanged();
    }*/

    public class BeerListArrayAdapter extends ArrayAdapter<Beer> {
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public BeerListArrayAdapter(Context ctx, int resourceId, List<Beer> objects) {
            super(ctx, resourceId, objects);
            resource = resourceId;
            inflater = LayoutInflater.from( ctx );
            context = ctx;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent ) {
            convertView = inflater.inflate(resource, null);
            Beer beer = getItem( position );
            TextView beerName = (TextView) convertView.findViewById(R.id.beerName);
            beerName.setText(beer.getName());
            TextView beerType = (TextView) convertView.findViewById(R.id.beerType);
            beerType.setText(beer.getType());
            TextView beerManufactorer = (TextView) convertView.findViewById(R.id.beerManufactorer);
            beerManufactorer.setText(beer.getManufacturer());

            return convertView;
        }
    }
}