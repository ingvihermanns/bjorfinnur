package is.bjorfinnur.bjorfinnur.beerlist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;

public class BeerListActivity extends FragmentActivity {

    BeerListFragment beerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        beerListFragment = (BeerListFragment)fm.findFragmentById(R.id.fragment_container);

        if (beerListFragment == null) {
            beerListFragment = new BeerListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, beerListFragment)
                    .commit();
        }
    }

    public void search(String query){
        beerListFragment.search(query);
    }

}
