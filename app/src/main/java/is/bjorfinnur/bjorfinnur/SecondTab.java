package is.bjorfinnur.bjorfinnur;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SecondTab extends Activity {
    /** Called when the activity is first created. */

    private DataBaseManager dataBaseManager;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/* Second Tab Content */
        TextView textView = new TextView(this);
        textView.setText("Second Tab");
        setContentView(textView);
    }


    public void search(String query){
        Log.e("Info", "Query recieved: " + query);
        //List<Bar> barList = dataBaseManager.searchBars(query);
        //newBarList(barList);
    }

    public List<Gpscordinates> populateDistance(String query){
        //List<Gpscordinates> gpscordlist = dataBaseManager.getBarCoordinates(query);
        //getDistance(gpscordlist);
    }

    public float[] getDistance(List<Gpscordinates> gpscordlist){
        float[] results = CalculateCoordinates.calculateDistance(gpscordlist);
        return results;
    }


}