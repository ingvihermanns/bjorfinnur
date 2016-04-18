package is.bjorfinnur.bjorfinnur.beerlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class BeerListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        BeerExpandableAdapter crimeExpandableAdapter = new BeerExpandableAdapter(getActivity(), generateCrimes());
        crimeExpandableAdapter.onRestoreInstanceState(savedInstanceState);

        mCrimeRecyclerView.setAdapter(crimeExpandableAdapter);

        return view;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((BeerExpandableAdapter) mCrimeRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    private List<ParentListItem> generateCrimes() {
        List<String> strings = new ArrayList<>();
        strings.add("arnar");
        strings.add("gummi");
        strings.add("birgir");
        List<ParentListItem> parentListItems = new ArrayList<>();
        for (String s : strings) {
            StringListItem sli = new StringListItem(s);
            List<String> childItemList = new ArrayList<>();
            childItemList.add(s + " child1.");
            childItemList.add(s + " child2.");
            sli.setChildItemList(childItemList);
            parentListItems.add(sli);
        }
        return parentListItems;
    }
}
