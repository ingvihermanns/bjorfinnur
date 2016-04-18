package is.bjorfinnur.bjorfinnur.barlist;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ParentViewHolder;
import is.bjorfinnur.bjorfinnur.util.MapBarOnClickListener;

import static is.bjorfinnur.bjorfinnur.util.MapUtil.getMyLocation;

public class BarParentViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    public TextView titleTextView;
    public ImageButton mParentDropDownArrow;

    public BarParentViewHolder(View itemView) {
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

    public void setUp(Bar bar) {
        Button mapButton = (Button) itemView.findViewById(R.id.parent_list_item_title_button);
        MapBarOnClickListener listener = new MapBarOnClickListener(itemView.getContext(), Arrays.asList(new Bar[]{bar}));
        mapButton.setOnClickListener(listener);

        String addon = "";
        try {
            double distanceInMeters = bar.calculateDistanceToInMeters(getMyLocation(itemView.getContext()));
            addon = " " + (int)distanceInMeters + " m.";
        }catch (Exception e){
            e.printStackTrace();
        }
        titleTextView.setText(bar.getName() + addon);
    }
}