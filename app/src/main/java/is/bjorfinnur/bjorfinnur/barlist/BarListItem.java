package is.bjorfinnur.bjorfinnur.barlist;

import android.util.Pair;

import java.util.List;

import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class BarListItem implements ParentListItem {
    private List<Pair<Bar, List<Pair<Beer, Price>>>> childItemList;
    private Bar bar;

    public BarListItem(Bar bar){
        this.bar = bar;
    }

    @Override
    public List<Pair<Bar, List<Pair<Beer, Price>>>> getChildItemList() {
        return this.childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void setChildItemList(List<Pair<Bar, List<Pair<Beer, Price>>>> childItemList) {
        this.childItemList = childItemList;
    }

    public Bar getBar() {
        return bar;
    }
}
