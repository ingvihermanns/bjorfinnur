package is.bjorfinnur.bjorfinnur.barlist;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import is.bjorfinnur.bjorfinnur.R;

public class BarListActivity extends FragmentActivity {

    BarListFragment barListFragment;
    boolean lastNameOrder = false;
    boolean lastPriceOrder = false;
    boolean lastDistanceOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        barListFragment = (BarListFragment)fm.findFragmentById(R.id.fragment_container);

        if (barListFragment == null) {
            barListFragment = new BarListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, barListFragment)
                    .commit();
        }
    }


    public void sortByPrice(String query) {
        lastPriceOrder = !lastPriceOrder;
        barListFragment.sortByPrice(query, lastPriceOrder);
    }

    public void sortByName(String query) {
        lastNameOrder = !lastNameOrder;
        barListFragment.sortByName(query, lastNameOrder);
    }


    public void search(String query){
        barListFragment.search(query);
    }

    public void sortByDistance(String query) {
        lastDistanceOrder = !lastDistanceOrder;
        barListFragment.sortByDistance(query, lastDistanceOrder);
    }
}
