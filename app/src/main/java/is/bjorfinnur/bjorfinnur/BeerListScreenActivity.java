package is.bjorfinnur.bjorfinnur;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class BeerListScreenActivity extends AppCompatActivity {

    TextView queryTextView;
    ListView listView;
    DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beer_list_screen);

        String query = getIntent().getStringExtra("query");

        dataBaseManager = new DataBaseManager(this);
        List<Beer> beerResults = dataBaseManager.searchBeers(query);
        ListView listView = (ListView) findViewById(R.id.listView);


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        listView.setAdapter( new BeerListArrayAdapter(this, R.layout.beer_list_row, beerResults));
    }


    public class BeerListArrayAdapter extends ArrayAdapter<Beer> {
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public BeerListArrayAdapter(Context ctx, int resourceId, List<Beer> objects) {
            super(ctx, resourceId, objects);
            resource = resourceId;
            inflater = LayoutInflater.from( ctx );
            context=ctx;
        }

        @Override
        public View getView ( int position, View convertView, ViewGroup parent ) {
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
