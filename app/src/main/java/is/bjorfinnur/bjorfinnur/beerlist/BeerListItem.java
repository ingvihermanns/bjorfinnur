package is.bjorfinnur.bjorfinnur.beerlist;

import android.util.Pair;

import java.util.List;

import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class BeerListItem implements ParentListItem {
    private List<Pair<Beer, List<Pair<Bar, Price>>>> childItemList;
    private Beer beer;

    public BeerListItem(Beer beer){
        this.beer = beer;
    }

    @Override
    public List<Pair<Beer, List<Pair<Bar, Price>>>> getChildItemList() {
        return this.childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void setChildItemList(List<Pair<Beer, List<Pair<Bar, Price>>>> childItemList) {
        this.childItemList = childItemList;
    }

    public Beer getBeer() {
        return beer;
    }
}
