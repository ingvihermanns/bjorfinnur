package is.bjorfinnur.bjorfinnur.beerlist;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
        View view = mInflater.inflate(R.layout.list_item_crime_parent, viewGroup, false);
        return new BeerParentViewHolder(view);
    }

    @Override
    public BeerChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_beer_card_child_header, viewGroup, false);
        return new BeerChildViewHolder(view, mInflater, viewGroup);
    }

    @Override
    public void onBindParentViewHolder(BeerParentViewHolder crimeParentViewHolder, int i, ParentListItem parentListItem) {
        Beer beer = ((BeerListItem)parentListItem).getBeer();
        crimeParentViewHolder.setUp(beer);
    }

    @Override
    public void onBindChildViewHolder(BeerChildViewHolder crimeChildViewHolder, int i, Object childListItem) {
        Pair<Beer, List<Pair<Bar, Price>>> pair = (Pair<Beer, List<Pair<Bar, Price>>>) childListItem;


        crimeChildViewHolder.setUp(pair);
    }
}
