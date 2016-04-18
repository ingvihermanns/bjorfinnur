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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        databaseManager = DatabaseManager.getInstance(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
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

        return beersToParents(beers);
    }


    public void search(String query){
        Log.e("Info", "Query recieved in beerTab: " + query);
        refreshAdapter(query);
    }

    private void refreshAdapter(String query){

        beerExpandableAdapter = new BeerExpandableAdapter(getActivity(), getBeers(query));
        mRecyclerView.setAdapter(beerExpandableAdapter);
    }

    public void sortByName(String query, boolean order) {

        List<Beer> beers = databaseManager.searchBeers2(query);

        if(order) {
            Collections.sort(beers);
        }else{
            Collections.sort(beers, Collections.reverseOrder());
        }

        beerExpandableAdapter = new BeerExpandableAdapter(getActivity(), beersToParents(beers));


        mRecyclerView.setAdapter(beerExpandableAdapter);
    }



    public void sortByPrice(String query, boolean order) {



        List<Beer> contenderBeers = databaseManager.searchBeers2(query);

        List<Pair<Beer, List<Pair<Bar, Price>>>> beerMapAsList = new ArrayList<>();

        for(Beer beer: contenderBeers){
            List<Pair<Bar, Price>> barPriceList = databaseManager.getBarsFromBeer(beer);
            Pair<Beer, List<Pair<Bar, Price>>> beerPair = new Pair<>(beer, barPriceList);
            beerMapAsList.add(beerPair);
        }


        Comparator<Pair<Beer, List<Pair<Bar, Price>>>> comparator = new Comparator<Pair<Beer, List<Pair<Bar, Price>>>>() {
            @Override
            public int compare(Pair<Beer, List<Pair<Bar, Price>>> lhs, Pair<Beer, List<Pair<Bar, Price>>> rhs) {
                Integer minl = minPrice(lhs);
                Integer minr = minPrice(rhs);
                return minl.compareTo(minr);
            }

            private Integer minPrice(Pair<Beer, List<Pair<Bar, Price>>> lhs) {
                List<Pair<Bar, Price>> pairList = lhs.second;
                Integer minPrice = Integer.MAX_VALUE;
                for(Pair<Bar, Price> pair: pairList){
                    Price price = pair.second;
                    if(price.getUnits() < minPrice){
                        minPrice = price.getUnits();
                    }
                }
                return minPrice;
            }
        };

        if(order) {
            Collections.sort(beerMapAsList, comparator);
        }else{
            Collections.sort(beerMapAsList, Collections.reverseOrder(comparator));
        }

        List<Beer> beers = new ArrayList<>();

        for(Pair<Beer, List<Pair<Bar, Price>>> pair: beerMapAsList){
            beers.add(pair.first);
        }

        beerExpandableAdapter = new BeerExpandableAdapter(getActivity(), beersToParents(beers));


        mRecyclerView.setAdapter(beerExpandableAdapter);
    }

    private List<ParentListItem> beersToParents(List<Beer> beers) {

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
}
