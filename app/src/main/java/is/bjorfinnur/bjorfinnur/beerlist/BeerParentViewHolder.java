package is.bjorfinnur.bjorfinnur.beerlist;

import android.os.Build;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.database.DatabaseManager;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ParentViewHolder;
import is.bjorfinnur.bjorfinnur.util.MapBarOnClickListener;


public class BeerParentViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    public TextView titleTextView;
    public ImageButton mParentDropDownArrow;

    public BeerParentViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_title_text_view);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (!HONEYCOMB_AND_ABOVE) {
            return;
        }

        if (expanded) {
            mParentDropDownArrow.setRotation(ROTATED_POSITION);
        } else {
            mParentDropDownArrow.setRotation(INITIAL_POSITION);
        }
    }

    public void setUp(Beer beer) {

        Button mapButton = (Button) itemView.findViewById(R.id.parent_list_item_title_button);
        List<Pair<Bar, Price>> barsPrices = DatabaseManager.getInstance(itemView.getContext()).getBarsFromBeer(beer);
        List<Bar> bars = new ArrayList<>();
        for(Pair<Bar, Price> pair: barsPrices){
            bars.add(pair.first);
        }
        MapBarOnClickListener listener = new MapBarOnClickListener(itemView.getContext(), bars);
        mapButton.setOnClickListener(listener);


        titleTextView.setText(beer.getName());
    }
}