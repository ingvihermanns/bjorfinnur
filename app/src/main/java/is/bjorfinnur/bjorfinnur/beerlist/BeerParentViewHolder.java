package is.bjorfinnur.bjorfinnur.beerlist;

import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ParentViewHolder;


public class BeerParentViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    public TextView titleTextView;
    public ImageButton mParentDropDownArrow;

    public BeerParentViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_beer_title_text_view);
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
        titleTextView.setText(beer.getName());
    }
}