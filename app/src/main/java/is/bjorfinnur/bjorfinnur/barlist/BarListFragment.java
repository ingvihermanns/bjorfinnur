package is.bjorfinnur.bjorfinnur.barlist;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

import static is.bjorfinnur.bjorfinnur.util.MapUtil.getMyLocation;

public class BarListFragment extends Fragment {
    // todo implement distance stuff
    private RecyclerView mRecyclerView;
    private BarExpandableAdapter barExpandableAdapter;
    private DatabaseManager databaseManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        databaseManager = DatabaseManager.getInstance(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        barExpandableAdapter = new BarExpandableAdapter(getActivity(), getBars(""));
        barExpandableAdapter.onRestoreInstanceState(savedInstanceState);

        mRecyclerView.setAdapter(barExpandableAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((BarExpandableAdapter) mRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }


    private List<ParentListItem> getBars(String query) {
        List<Bar> bars = databaseManager.searchBars2(query);

        return barsToParents(bars);
    }

    private List<ParentListItem> barsToParents(List<Bar> bars) {

        List<ParentListItem> parentListItems = new ArrayList<>();
        for (Bar bar: bars) {
            BarListItem barListItem = new BarListItem(bar);
            List<Pair<Beer, Price>> beerPriceList = databaseManager.getBeersFromBar(bar);

            List<Pair<Bar, List<Pair<Beer, Price>>>> childItemList = new ArrayList<>();
            childItemList.add(new Pair<>(bar, beerPriceList));
            barListItem.setChildItemList(childItemList);
            parentListItems.add(barListItem);
        }
        return parentListItems;
    }


    public void search(String query){
        Log.e("Info", "Query recieved in barTab: " + query);
        refreshAdapter(query);
    }

    private void refreshAdapter(String query){

        barExpandableAdapter = new BarExpandableAdapter(getActivity(), getBars(query));
        mRecyclerView.setAdapter(barExpandableAdapter);
    }

    public void sortByName(String query, boolean order) {

        List<Bar> bars = databaseManager.searchBars2(query);
        if(order) {
            Collections.sort(bars);
        }else{
            Collections.sort(bars, Collections.reverseOrder());
        }

        barExpandableAdapter = new BarExpandableAdapter(getActivity(), barsToParents(bars));
        mRecyclerView.setAdapter(barExpandableAdapter);
    }

    public void sortByPrice(String query, boolean order) {

        List<Bar> contenderBars = databaseManager.searchBars2(query);

        List<Pair<Bar, List<Pair<Beer, Price>>>> barMapAsList = new ArrayList<>();

        for(Bar bar: contenderBars){
            List<Pair<Beer, Price>> beerPriceList = databaseManager.getBeersFromBar(bar);
            Pair<Bar, List<Pair<Beer, Price>>> barPair = new Pair<>(bar, beerPriceList);
            barMapAsList.add(barPair);
        }



        Comparator<Pair<Bar, List<Pair<Beer, Price>>>> comparator = new Comparator<Pair<Bar, List<Pair<Beer, Price>>>>() {
            @Override
            public int compare(Pair<Bar, List<Pair<Beer, Price>>> lhs, Pair<Bar, List<Pair<Beer, Price>>> rhs) {
                Integer minl = minPrice(lhs);
                Integer minr = minPrice(rhs);
                return minl.compareTo(minr);
            }

            private Integer minPrice(Pair<Bar, List<Pair<Beer, Price>>> lhs) {
                List<Pair<Beer, Price>> pairList = lhs.second;
                Integer minPrice = Integer.MAX_VALUE;
                for(Pair<Beer, Price> pair: pairList){
                    Price price = pair.second;
                    if(price.getUnits() < minPrice){
                        minPrice = price.getUnits();
                    }
                }
                return minPrice;
            }
        };

        if(order) {
            Collections.sort(barMapAsList, comparator);
        }else{
            Collections.sort(barMapAsList, Collections.reverseOrder(comparator));
        }

        List<Bar> bars = new ArrayList<>();

        for(Pair<Bar, List<Pair<Beer, Price>>> pair: barMapAsList){
            bars.add(pair.first);
        }

        barExpandableAdapter = new BarExpandableAdapter(getActivity(), barsToParents(bars));
        mRecyclerView.setAdapter(barExpandableAdapter);
    }


    public void sortByDistance(String query, boolean order) {

        List<Bar> contenderBars = databaseManager.searchBars2(query);

        List<Pair<Bar, List<Pair<Beer, Price>>>> barMapAsList = new ArrayList<>();

        for(Bar bar: contenderBars){
            List<Pair<Beer, Price>> beerPriceList = databaseManager.getBeersFromBar(bar);
            Pair<Bar, List<Pair<Beer, Price>>> barPair = new Pair<>(bar, beerPriceList);
            barMapAsList.add(barPair);
        }



        Comparator<Pair<Bar, List<Pair<Beer, Price>>>> comparator = new Comparator<Pair<Bar, List<Pair<Beer, Price>>>>() {
            @Override
            public int compare(Pair<Bar, List<Pair<Beer, Price>>> lhs, Pair<Bar, List<Pair<Beer, Price>>> rhs) {
                Double minl = minDistance(lhs);
                Double minr = minDistance(rhs);
                return minl.compareTo(minr);
            }

            private Double minDistance(Pair<Bar, List<Pair<Beer, Price>>> lhs) {
                return lhs.first.calculateDistanceToInMeters(getMyLocation(getContext()));
            }
        };

        if(order) {
            Collections.sort(barMapAsList, comparator);
        }else{
            Collections.sort(barMapAsList, Collections.reverseOrder(comparator));
        }

        List<Bar> bars = new ArrayList<>();

        for(Pair<Bar, List<Pair<Beer, Price>>> pair: barMapAsList){
            bars.add(pair.first);
        }

        barExpandableAdapter = new BarExpandableAdapter(getActivity(), barsToParents(bars));
        mRecyclerView.setAdapter(barExpandableAdapter);
    }


}
