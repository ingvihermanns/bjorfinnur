package is.bjorfinnur.bjorfinnur.database;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by arnar on 4/15/16.
 */

public class JsonDatabaseDownloader extends AsyncTask<String, Void, Void> {


    protected Void doInBackground(String... urls) {
        downloadFile();
        return null;
    }

    protected void onPostExecute(Void param) {

        // TODO: do something with the feed
    }

    public void downloadFile() {
        String urlstring = "https://notendur.hi.is/athg17/bjorfinnur/db.json";
        try {
            URL url = new URL(urlstring);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                readStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readStream(InputStream in) {
        String inputStr;
        StringBuilder responseStrBuilder = new StringBuilder();
        try {
            JSONParser parser = new JSONParser();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonobject = new JSONObject(responseStrBuilder.toString());
            Log.e("Info", "Database json: " + jsonobject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}