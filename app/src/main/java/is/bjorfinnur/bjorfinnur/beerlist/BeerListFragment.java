package is.bjorfinnur.bjorfinnur.beerlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class BeerListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private BeerExpandableAdapter beerExpandableAdapter;
    private DatabaseManager databaseManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        databaseManager = DatabaseManager.getInstance(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        beerExpandableAdapter = new BeerExpandableAdapter(getActivity(), getBeers(""));
        beerExpandableAdapter.onRestoreInstanceState(savedInstanceState);

        mRecyclerView.setAdapter(beerExpandableAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((BeerExpandableAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    private List<ParentListItem> getBeers(String query) {
        List<Beer> beers = databaseManager.searchBeers2(query);

        List<ParentListItem> parentListItems = new ArrayList<>();
        for (Beer beer: beers) {
            BeerListItem beerListItem = new BeerListItem(beer);
            List<Pair<Bar, Price>> beerPriceList = databaseManager.getBarsFromBeer(beer);
            List<Pair<Beer, List<Pair<Bar, Price>>>> childItemList = new ArrayList<>();
            childItemList.add(new Pair<>(beer, beerPriceList));
            beerListItem.setChildItemList(childItemList);
            parentListItems.add(beerListItem);
        }
        return parentListItems;
    }


    public void search(String query){
        Log.e("Info", "Query recieved in beerTab: " + query);
        refreshAdapter(query);
    }

    private void refreshAdapter(String query){

        beerExpandableAdapter = new BeerExpandableAdapter(getActivity(), getBeers(query));
        mRecyclerView.setAdapter(beerExpandableAdapter);
    }
}
