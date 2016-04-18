package is.bjorfinnur.bjorfinnur.beerlist;

import android.support.v4.app.Fragment;

public class BeerTestActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new BeerListFragment();
    }

}
