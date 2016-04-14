package is.bjorfinnur.bjorfinnur;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.Objects;

/**
 * Created by Notandi on 14.4.2016.
 */
public class JsonDemo {

    public static JSONParser parser = new JSONParser();
    public static String s = "[0,{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}]";

    public static String testMethod() {

        try{
            Object obj = parser.parse(s);
            JSONArray array = (JSONArray)obj;
            JSONObject obj2 = (JSONObject)array.get(1);

            Log.i("Grimbill", obj2.get("1").toString());
        }catch (ParseException pe){
            Log.i("Grimbill", "Ekki virka ónei");
        }

        return s;
    }
}
