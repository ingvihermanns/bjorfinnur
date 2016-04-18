package is.bjorfinnur.bjorfinnur.beerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
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
        View view = mInflater.inflate(R.layout.list_item_crime_child, viewGroup, false);
        return new BeerChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(BeerParentViewHolder crimeParentViewHolder, int i, ParentListItem parentListItem) {
        crimeParentViewHolder.mCrimeTitleTextView.setText(((StringListItem)parentListItem).getString());
    }

    @Override
    public void onBindChildViewHolder(BeerChildViewHolder crimeChildViewHolder, int i, Object childListItem) {
        crimeChildViewHolder.mCrimeDateText.setText(((String)childListItem));
    }
}
