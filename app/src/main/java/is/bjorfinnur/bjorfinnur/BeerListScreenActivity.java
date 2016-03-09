package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        queryTextView = (TextView) findViewById(R.id.queryTextView);
        String query = getIntent().getStringExtra("query");

        dataBaseManager = new DataBaseManager(this);
        List<Beer> beerResults = dataBaseManager.searchBeers(query);
        ListView listView = (ListView) findViewById(R.id.listView);

        queryTextView.setText(query);

    }
}
