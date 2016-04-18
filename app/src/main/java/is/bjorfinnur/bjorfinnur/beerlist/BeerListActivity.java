package is.bjorfinnur.bjorfinnur.beerlist;


import android.support.v4.app.Fragment;

public class BeerListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BeerListFragment();
    }
}
