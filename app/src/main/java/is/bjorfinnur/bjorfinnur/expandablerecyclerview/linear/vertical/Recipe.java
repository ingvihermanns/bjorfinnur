package is.bjorfinnur.bjorfinnur.expandablerecyclerview.linear.vertical;


import java.util.List;

import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class Recipe implements ParentListItem {

    private String mName;
    private List<Ingredient> mIngredients;

    public Recipe(String name, List<Ingredient> ingredients) {
        mName = name;
        mIngredients = ingredients;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<?> getChildItemList() {
        return mIngredients;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
