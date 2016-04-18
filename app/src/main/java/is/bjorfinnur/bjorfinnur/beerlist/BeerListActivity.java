package is.bjorfinnur.bjorfinnur.beerlist;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import is.bjorfinnur.bjorfinnur.R;

public class BeerListActivity extends FragmentActivity {

    BeerListFragment beerListFragment;
    boolean lastNameOrder = false;
    boolean lastPriceOrder = false;
    boolean lastDistanceOrder = false;

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

    public void sortByPrice(String query) {
        lastPriceOrder = !lastPriceOrder;
        beerListFragment.sortByPrice(query, lastPriceOrder);
    }

    public void sortByName(String query) {
        lastNameOrder = !lastNameOrder;
        beerListFragment.sortByName(query, lastNameOrder);
    }

    public void sortByDistance(String query) {
        lastDistanceOrder = !lastDistanceOrder;
        beerListFragment.sortByDistance(query, lastDistanceOrder);
    }

}
