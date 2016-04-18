package is.bjorfinnur.bjorfinnur.beerlist;

import android.view.View;
import android.widget.TextView;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ChildViewHolder;

public class BeerChildViewHolder extends ChildViewHolder {

    public TextView mCrimeDateText;
    //public CheckBox mCrimeSolvedCheckBox;

    public BeerChildViewHolder(View itemView) {
        super(itemView);

        mCrimeDateText = (TextView) itemView.findViewById(R.id.child_list_item_crime_date_text_view);
        //mCrimeSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.child_list_item_crime_solved_check_box);
    }
}
