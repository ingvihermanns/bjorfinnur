package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

public class StartScreenActivity extends AppCompatActivity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        setUpSearchAllButton();

        setUpSearchView();
    }

    private void setUpSearchView() {
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StartScreenActivity.this.callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void setUpSearchAllButton() {
        Button SearchAllBeersButton = (Button) findViewById(R.id.search_all_beers_button);

        SearchAllBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchView.getQuery().toString();
                StartScreenActivity.this.callSearch(query);
            }
        });
    }

    private void callSearch(String query) {
        Intent mainIntent = new Intent(StartScreenActivity.this, BeerListScreenActivity.class);
        mainIntent.putExtra("query", query);
        StartScreenActivity.this.startActivity(mainIntent);
    }
}
