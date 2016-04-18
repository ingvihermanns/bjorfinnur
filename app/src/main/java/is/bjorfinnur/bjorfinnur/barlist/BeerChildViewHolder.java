package is.bjorfinnur.bjorfinnur.barlist;

import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ChildViewHolder;
import is.bjorfinnur.bjorfinnur.util.MapBarOnClickListener;

public class BeerChildViewHolder extends ChildViewHolder {

    private final LayoutInflater mInflater;
    private final ViewGroup viewGroup;

    public BeerChildViewHolder(View itemView, LayoutInflater mInflater, ViewGroup viewGroup) {
        super(itemView);


        this.mInflater = mInflater;
        this.viewGroup = viewGroup;

    }

    public void setUp(Pair<Bar, List<Pair<Beer, Price>>> monsterPair) {
        Bar bar = monsterPair.first;
        List<Pair<Beer, Price>> pairList = monsterPair.second;

        ImageView imageView = (ImageView)itemView.findViewById(R.id.list_item_card_child_header_icon);
        imageView.setVisibility(View.GONE);

        TextView barDescriptionText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_description_text);
        TextView barAddressText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_type);
        TextView manufacturerText = (TextView)itemView.findViewById(R.id.list_item_card_child_header_manufacturer);

        setTitleText(barDescriptionText, "Description", bar.getDescription());
        setTitleText(barAddressText, "Address", bar.getAddress());
        manufacturerText.setText("");

        LinearLayout linearLayout = (LinearLayout)itemView.findViewById(R.id.price_row_linear_layout);

        for(Pair<Beer, Price> pair: pairList){
            addBeer(pair.first, pair.second, linearLayout);
        }


    }

    private void setTitleText(TextView textView, String title, String text) {
        textView.setText(Html.fromHtml("<b>" + title + ":</b> " + text));
    }

    private void addBeer(Beer beer, Price price, LinearLayout linearLayout) {

        RelativeLayout relativeLayout = (RelativeLayout)mInflater.inflate(R.layout.card_row, viewGroup, false);

        TextView barNameText = (TextView)relativeLayout.findViewById(R.id.beer_card_bar_row_bar_name_text);
        TextView beerPriceText = (TextView)relativeLayout.findViewById(R.id.beer_card_bar_row_price_text);
        Button mapButton = (Button) relativeLayout.findViewById(R.id.beer_card_bar_row_map_button);

        barNameText.setText(beer.getName());
        beerPriceText.setText(price.getUnits() + " " + price.getCurrency());
        setUpMapButton(mapButton, beer);

        linearLayout.addView(relativeLayout, 0);

    }

    private void setUpMapButton(Button mapButton, final Beer beer) {
        List<Pair<Bar, Price>> barsPrices = DatabaseManager.getInstance(itemView.getContext()).getBarsFromBeer(beer);
        List<Bar> bars = new ArrayList<>();
        for(Pair<Bar, Price> pair: barsPrices){
            bars.add(pair.first);
        }
        MapBarOnClickListener listener = new MapBarOnClickListener(itemView.getContext(), bars);
        mapButton.setOnClickListener(listener);
    }
}
