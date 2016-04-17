package is.bjorfinnur.bjorfinnur.expandablerecyclerview.linear.horizontal;

import android.view.View;
import android.widget.TextView;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ChildViewHolder;

/**
 * Custom child ViewHolder. Any views should be found and set to public variables here to be
 * referenced in your custom ExpandableRecyclerAdapter later.
 *
 * Must extend ChildViewHolder.
 */
public class HorizontalChildViewHolder extends ChildViewHolder {

    public TextView mDataTextView;

    /**
     * Public constructor for the custom child ViewHolder
     *
     * @param itemView the child ViewHolder's view
     */
    public HorizontalChildViewHolder(View itemView) {
        super(itemView);

        mDataTextView = (TextView) itemView.findViewById(R.id.list_item_horizontal_child_textView);
    }

    public void bind(String childText) {
        mDataTextView.setText(childText);
    }
}
