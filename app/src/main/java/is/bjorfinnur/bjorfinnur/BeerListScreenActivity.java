package is.bjorfinnur.bjorfinnur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BeerListScreenActivity extends AppCompatActivity {

    TextView queryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beer_list_screen);

        queryTextView = (TextView) findViewById(R.id.search_view);
        String query = getIntent().getStringExtra("query");

        queryTextView.setText(query);



    }
}
