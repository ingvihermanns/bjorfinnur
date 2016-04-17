package is.bjorfinnur.bjorfinnur.expandablerecyclerview.linear.vertical;

import android.view.View;
import android.widget.TextView;

import is.bjorfinnur.bjorfinnur.R;
import is.bjorfinnur.bjorfinnur.expandablerecyclerview.ViewHolder.ChildViewHolder;

public class IngredientViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
    }

    public void bind(Ingredient ingredient) {
        mIngredientTextView.setText(ingredient.getName());
    }
}
