package is.bjorfinnur.bjorfinnur.util;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import is.bjorfinnur.bjorfinnur.activities.MapActivity;
import is.bjorfinnur.bjorfinnur.data.Bar;


public class MapBarOnClickListener implements View.OnClickListener {
    private Context context;
    private List<Bar> bars;

    public MapBarOnClickListener(Context context, List<Bar> bars){
        this.context = context;
        this.bars = bars;
    }

    @Override
    public void onClick(View v) {
        List<Bar> bars =  getBars();
        ArrayList<String> barNames = new ArrayList<>();
        ArrayList<String> barLatitudes = new ArrayList<>();
        ArrayList<String> barLongitudes = new ArrayList<>();
        for(Bar bar: bars){
            barNames.add(bar.getName());
            barLatitudes.add(bar.getLatitude());
            barLongitudes.add(bar.getLongitude());
        }
        MapActivity.launchIntent(context, barNames, barLatitudes, barLongitudes);
    }

    private List<Bar> getBars() {
        return this.bars;
    }
}
