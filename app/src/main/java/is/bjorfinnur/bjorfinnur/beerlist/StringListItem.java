package is.bjorfinnur.bjorfinnur.beerlist;

import java.util.List;

import is.bjorfinnur.bjorfinnur.expandablerecyclerview.Model.ParentListItem;

public class StringListItem implements ParentListItem {
    private List<String> childItemList;
    private String string;

    public StringListItem(String string){
        this.string = string;
    }

    @Override
    public List<String> getChildItemList() {
        return this.childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public void setChildItemList(List<String> childItemList) {
        this.childItemList = childItemList;
    }

    public String getString() {
        return string;
    }
}
