package is.bjorfinnur.bjorfinnur.barlist;

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


public class BarExpandableAdapter extends ExpandableRecyclerAdapter<BarParentViewHolder, BeerChildViewHolder> {

    private LayoutInflater mInflater;

    public BarExpandableAdapter(Context context, List<ParentListItem> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BarParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_parent, viewGroup, false);
        return new BarParentViewHolder(view);
    }

    @Override
    public BeerChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_beer_card_child_header, viewGroup, false);
        return new BeerChildViewHolder(view, mInflater, viewGroup);
    }

    @Override
    public void onBindParentViewHolder(BarParentViewHolder parentViewHolder, int i, ParentListItem parentListItem) {
        Bar bar = ((BarListItem)parentListItem).getBar();
        parentViewHolder.setUp(bar);
    }

    @Override
    public void onBindChildViewHolder(BeerChildViewHolder childViewHolder, int i, Object childListItem) {
        Pair<Bar, List<Pair<Beer, Price>>> pair = (Pair<Bar, List<Pair<Beer, Price>>>) childListItem;


        childViewHolder.setUp(pair);
    }
}
