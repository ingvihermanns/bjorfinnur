package is.bjorfinnur.bjorfinnur;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;

public class StartScreenActivity extends AppCompatActivity {

    SearchView mySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);



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
                Intent mainIntent = new Intent(StartScreenActivity.this, BeerListScreenActivity.class);
                mainIntent.putExtra("query", query);
                StartScreenActivity.this.startActivity(mainIntent);
                StartScreenActivity.this.finish();
            }
        });
    }
}
