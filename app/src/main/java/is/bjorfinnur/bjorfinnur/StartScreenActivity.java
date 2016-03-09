package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;

public class StartScreenActivity extends AppCompatActivity {

    SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        mySearchView = (SearchView) findViewById(R.id.search_view);

        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            private void callSearch(String query) {
                Intent beerListIntent = new Intent(StartScreenActivity.this, BeerListScreenActivity.class);
                beerListIntent.putExtra("query", query);
                StartScreenActivity.this.startActivity(beerListIntent);
            }
        });
    }
}
