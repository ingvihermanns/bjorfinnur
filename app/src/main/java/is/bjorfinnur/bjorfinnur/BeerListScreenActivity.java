package is.bjorfinnur.bjorfinnur;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeerListScreenActivity extends AppCompatActivity {

    private ListView listView;
    private List<Beer> beerList;

    private enum Order {
        ascending, descending
    }

    private Order lastNameSortingOrder = Order.descending;
    private Order lastTypeSortingOrder = Order.descending;
    private Order lastManufacturerSortingOrder = Order.descending;

    private enum OrderType {
        name, type, manufacturer
    }

    private OrderType lastOrderingUsed = OrderType.name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list_screen);
        setUpAndQueryDatabase();
        setUpUiComponents();
        sortListByName(Order.ascending);
        lastNameSortingOrder = Order.ascending;
    }

    private void setUpAndQueryDatabase() {
        String query = getIntent().getStringExtra("query");
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        beerList = dataBaseManager.searchBeers(query);
    }

    private void setUpUiComponents() {
        setUpListView();
        setUpSortByNameButton();
        setUpSortByTypeButton();
        setUpSortByManufacturerButton();
    }

    private void setUpListView() {
        listView = (ListView) findViewById(R.id.listView);
        BeerListArrayAdapter beerListArrayAdapter = new BeerListArrayAdapter(this, beerList);
        setAdapter(beerListArrayAdapter);
    }

    private void setUpSortByNameButton() {
        Button sortByNameButton = (Button) findViewById(R.id.sortByNameButton);
        sortByNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order sortByOrder;
                if (lastOrderingUsed == OrderType.name) {
                    if (lastNameSortingOrder == Order.ascending) {
                        sortByOrder = Order.descending;
                    } else {
                        sortByOrder = Order.ascending;
                    }
                } else {
                    lastOrderingUsed = OrderType.name;
                    sortByOrder = Order.ascending;
                }
                sortListByName(sortByOrder);
                lastNameSortingOrder = sortByOrder;
            }
        });
    }

    private void setUpSortByTypeButton() {
        Button sortByTypeButton = (Button) findViewById(R.id.sortByTypeButton);
        sortByTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order sortByOrder;
                if (lastOrderingUsed == OrderType.type) {
                    if (lastTypeSortingOrder == Order.ascending) {
                        sortByOrder = Order.descending;
                    } else {
                        sortByOrder = Order.ascending;
                    }
                } else {
                    lastOrderingUsed = OrderType.type;
                    sortByOrder = Order.ascending;
                }
                sortListByType(sortByOrder);
                lastTypeSortingOrder = sortByOrder;
            }
        });
    }

    private void setUpSortByManufacturerButton() {
        Button sortByManufacturerButton = (Button) findViewById(R.id.sortByManufacturerButton);
        sortByManufacturerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order sortByOrder;
                if (lastOrderingUsed == OrderType.manufacturer) {
                    if (lastManufacturerSortingOrder == Order.ascending) {
                        sortByOrder = Order.descending;
                    } else {
                        sortByOrder = Order.ascending;
                    }
                } else {
                    lastOrderingUsed = OrderType.manufacturer;
                    sortByOrder = Order.ascending;
                }
                sortListByManufacturer(sortByOrder);
                lastManufacturerSortingOrder = sortByOrder;
            }
        });
    }

    private void sortListByName(final Order order) {
        Collections.sort(beerList, new Comparator<Beer>() {
            @Override
            public int compare(Beer beer1, Beer beer2) {
                if(order == Order.ascending){
                    return beer1.getBeerName().compareTo(beer2.getBeerName());
                }else{
                    return beer2.getBeerName().compareTo(beer1.getBeerName());
                }
            }
        });
        listView.setAdapter(new BeerListArrayAdapter(this, beerList));
        resetListView();
    }

    private void sortListByType(final Order order){
        Collections.sort(beerList, new Comparator<Beer>() {
            @Override
            public int compare(Beer beer1, Beer beer2) {
                if(order == Order.ascending){
                    return beer1.getType().compareTo(beer2.getType());
                }else{
                    return beer2.getType().compareTo(beer1.getType());
                }
            }
        });
        listView.setAdapter(new BeerListArrayAdapter(this, beerList));
        resetListView();
    }

    private void sortListByManufacturer(final Order order) {
        Collections.sort(beerList, new Comparator<Beer>() {
            @Override
            public int compare(Beer beer1, Beer beer2) {
                if(order == Order.ascending){
                    return beer1.getManufacturer().compareTo(beer2.getManufacturer());
                }else{
                    return beer2.getManufacturer().compareTo(beer1.getManufacturer());
                }
            }
        });
        listView.setAdapter(new BeerListArrayAdapter(this, beerList));
        resetListView();
    }

    private void setAdapter(BeerListArrayAdapter adapter){
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void resetListView(){
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public class BeerListArrayAdapter extends ArrayAdapter<Beer> {
        private final int resource;
        private final LayoutInflater inflater;

        public BeerListArrayAdapter(Context ctx, List<Beer> objects) {
            super(ctx, R.layout.beer_list_row, objects);
            resource = R.layout.beer_list_row;
            inflater = LayoutInflater.from( ctx );
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView (int position, View convertView, ViewGroup parent ) {
            convertView = inflater.inflate( resource, null );
            Beer beer = getItem( position );
            TextView beerName = (TextView) convertView.findViewById(R.id.beerName);
            beerName.setText(beer.getBeerName());
            TextView beerType = (TextView) convertView.findViewById(R.id.beerType);
            beerType.setText(beer.getType());
            TextView beerManufacturer = (TextView) convertView.findViewById(R.id.beerManufactorer);
            beerManufacturer.setText(beer.getManufacturer());

            return convertView;
        }
    }
}
