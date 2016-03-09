package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

public class StartScreenActivity extends AppCompatActivity {

    SearchView mySearchView;
    Button mySearchAllBeersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        mySearchAllBeersButton = (Button) findViewById(R.id.search_all_beers_button);

        mySearchAllBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mySearchView.getQuery().toString();
                StartScreenActivity.this.callSearch(query);
            }
        });

        mySearchView = (SearchView) findViewById(R.id.search_view);
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StartScreenActivity.this.callSearch(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            private void callSearch(String query) {
                Intent mainIntent = new Intent(StartScreenActivity.this, BeerListScreenActivity.class);
                mainIntent.putExtra("query", query);
                StartScreenActivity.this.startActivity(mainIntent);
            }
        });
    }

    private void callSearch(String query) {
        Intent mainIntent = new Intent(StartScreenActivity.this, BeerListScreenActivity.class);
        mainIntent.putExtra("query", query);
        StartScreenActivity.this.startActivity(mainIntent);
    }
}
