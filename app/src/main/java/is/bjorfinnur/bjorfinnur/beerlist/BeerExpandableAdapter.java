package is.bjorfinnur.bjorfinnur.beerlist;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;


public class BeerExpandableAdapter extends ExpandableRecyclerAdapter<BeerParentViewHolder, BeerChildViewHolder> {

    private LayoutInflater mInflater;

    public BeerExpandableAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BeerParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_parent, viewGroup, false);
        return new BeerParentViewHolder(view);
    }

    @Override
    public BeerChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_beer_card_child_header, viewGroup, false);
        return new BeerChildViewHolder(view, mInflater, viewGroup);
    }

    @Override
    public void onBindParentViewHolder(BeerParentViewHolder parentViewHolder, int i, ParentListItem parentListItem) {
        Beer beer = ((BeerListItem)parentListItem).getBeer();
        parentViewHolder.setUp(beer);
    }

    @Override
    public void onBindChildViewHolder(BeerChildViewHolder childViewHolder, int i, Object childListItem) {
        Pair<Beer, List<Pair<Bar, Price>>> pair = (Pair<Beer, List<Pair<Bar, Price>>>) childListItem;
        childViewHolder.setUp(pair);
    }
}
