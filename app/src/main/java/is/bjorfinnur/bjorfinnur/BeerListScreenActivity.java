package is.bjorfinnur.bjorfinnur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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

        if(beerResults.size() > 0){
            query = beerResults.get(0).getBeerName();
        } else {
            query = "Grimbill";
        }

        queryTextView = (TextView) findViewById(R.id.queryTextView);
        queryTextView.setText(query);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        List<String> your_array_list = new ArrayList<String>();
        your_array_list.add("foo");
        your_array_list.add("bar");

        ArrayAdapter<Beer> arrayAdapter = new ArrayAdapter<Beer>(
                this,
                R.layout.simple_list_item_1,
                beerResults);

        listView.setAdapter(arrayAdapter);
    }


    /*public class CustomAdapterArrayAdapter extends ArrayAdapter{

        Activity activity;
        List<Beer> beerList;

        public CustomAdapterArrayAdapter(Activity context, List<Beer> beerList) {
            super(context, 0, beerList);
            this.activity = context;
            this.beerList = beerList;
        }

        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Beer currentBeer = (Beer) getItem(position);

            return new ;
        }
    }*/
}
