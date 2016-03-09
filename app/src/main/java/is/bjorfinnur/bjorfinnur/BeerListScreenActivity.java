package is.bjorfinnur.bjorfinnur;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeerListScreenActivity extends AppCompatActivity {

    TextView queryTextView;
    ListView listView;
    DataBaseManager dataBaseManager;
    List<Beer> beerList;
    Button sortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beer_list_screen);

        String query = getIntent().getStringExtra("query");

        dataBaseManager = new DataBaseManager(this);
        beerList = dataBaseManager.searchBeers(query);
        ListView listView = (ListView) findViewById(R.id.listView);

        sortButton = (Button) findViewById(R.id.sortButton);

        Collections.sort(beerList, new Comparator<Beer>() {
            @Override
            public int compare(Beer beer1, Beer beer2) {

                return beer1.getBeerName().compareTo(beer2.getBeerName());
            }
        });

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        setAdapter(new BeerListArrayAdapter(this, R.layout.beer_list_row, beerList));

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortListByType();
            }
        });
    }

    private void sortListByType(){
        Collections.sort(beerList, new Comparator<Beer>() {
            @Override
            public int compare(Beer beer1, Beer beer2) {

                return beer1.getType().compareTo(beer2.getType());
            }
        });
        listView.setAdapter(new BeerListArrayAdapter(this, R.layout.beer_list_row, beerList));
        resetListView();
    }

    private void setAdapter(BeerListArrayAdapter adapter){
        listView.setAdapter(adapter);
    }

    private void resetListView(){
        listView.deferNotifyDataSetChanged();
    }

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
            convertView = inflater.inflate( resource, null );
            Beer beer = getItem( position );
            TextView beerName = (TextView) convertView.findViewById(R.id.beerName);
            beerName.setText(beer.getBeerName());
            TextView beerType = (TextView) convertView.findViewById(R.id.beerType);
            beerType.setText(beer.getType());
            TextView beerManufactorer = (TextView) convertView.findViewById(R.id.beerManufactorer);
            beerManufactorer.setText(beer.getManufacturer());

            return convertView;
        }
    }
}
