package is.bjorfinnur.bjorfinnur.database;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is.bjorfinnur.bjorfinnur.data.Beer;

/**
 * Created by arnar on 4/15/16.
 */

public class JsonDatabaseDownloader extends AsyncTask<String, Void, JSONObject> {


    protected JSONObject doInBackground(String... urls) {
        JSONObject obj = downloadFile();
        return obj;
    }

    protected void onPostExecute(Void param) {

        // TODO: do something with the feed
    }

    public JSONObject downloadFile() {
        String urlstring = "https://notendur.hi.is/athg17/bjorfinnur/db.json";
        JSONObject obj = null;
        try {
            URL url = new URL(urlstring);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                obj = readStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private JSONObject readStream(InputStream in) {
        String inputStr;
        StringBuilder responseStrBuilder = new StringBuilder();
        JSONObject jsonobject = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //JSONArray testArray = new JSONArray(responseStrBuilder.toString());
            jsonobject = new JSONObject(responseStrBuilder.toString());
            Log.e("JSON", "Database json: " + jsonobject);
            //addToDatabase(jsonobject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonobject;

    }
}