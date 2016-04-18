package is.bjorfinnur.bjorfinnur.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;

public class DatabaseManager extends SQLiteOpenHelper {

    private static DatabaseManager databaseManagerInstance;
    // Default system data path
    private final String dataBasePath;
    // Name of database to be used
    private static final String DB_NAME = "BeerFinder.db";

    private SQLiteDatabase myDatabase;

    private final Context myContext;

    private Map<Beer, List<Pair<Bar, Price>>> beerMap = new TreeMap<>();
    private Map<Bar, List<Pair<Beer, Price>>> barMap = new TreeMap<>();
    private Map<Integer, Beer> beerIds = new TreeMap<>();
    private Map<Integer, Bar> barIds = new TreeMap<>();

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     */
    private DatabaseManager(Context context) {
        super(context, DB_NAME, null, 1);

        myContext = context;
        dataBasePath = myContext.getDatabasePath(DB_NAME).getPath();

        try {
            this.createDataBase();
        } catch (Exception e) {
            throw new Error("Unable to create database");
        }

        try {
            this.openDataBase();
        } catch (Exception e) {
            throw new Error("Unable to open database");
        }


        JsonDatabaseDownloader testDownloader = new JsonDatabaseDownloader();
        JSONObject databaseObject = null;
        try {
            databaseObject = testDownloader.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recreateDatabase();
        fillNewDatabase(databaseObject);

        setUpMaps();
    }

    public List<Pair<Bar, List<Pair<Beer, Price>>>> getBarMapAsList(){
        List<Pair<Bar, List<Pair<Beer, Price>>>> list = new ArrayList<>();
        for(Bar bar: barMap.keySet()){
            Pair<Bar, List<Pair<Beer, Price>>> pair = new Pair<>(bar, barMap.get(bar));
            list.add(pair);
        }
        return list;
    }

    public List<Pair<Beer, List<Pair<Bar, Price>>>> getBeerMapAsList(){
        List<Pair<Beer, List<Pair<Bar, Price>>>> list = new ArrayList<>();
        for(Beer beer: beerMap.keySet()){
            Pair<Beer, List<Pair<Bar, Price>>> pair = new Pair<>(beer, beerMap.get(beer));
            list.add(pair);
        }
        return list;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * returns true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = dataBasePath + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            //database doesn't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = dataBasePath + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private void openDataBase() {
        //Open the database
        String myPath = dataBasePath + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static DatabaseManager getInstance(Context context) {
        if (databaseManagerInstance == null) {
            initializeDatabaseManager(context);
        }
        return databaseManagerInstance;
    }

    private static void initializeDatabaseManager(Context context) {
        databaseManagerInstance = new DatabaseManager(context);
    }

    private void setUpMaps() {
        addBeersToBeerMap();
        addBarsToBarMap();
        addBeerBarTableToMaps();
    }

    private void addBarsToBarMap() {

        // Table schema:
        //        CREATE TABLE "Bars" (
        //        `id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        //        `name`	TEXT NOT NULL UNIQUE,
        //        `address`	TEXT NOT NULL,
        //        `latitude`	TEXT NOT NULL,
        //        `longitude`	TEXT NOT NULL,
        //        `description`	TEXT NOT NULL
        //        );

        String query = "SELECT * FROM Bars";
        String[] parameters = new String[]{};
        Cursor cursor = myDatabase.rawQuery(query, parameters);
        try {
            for (String col : cursor.getColumnNames()) {
                Log.i("Info", "Cols in bars " + col);
            }
            Log.i("Info: ", "Rowcount " + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                Bar bar = new Bar(id, name, address, latitude, longitude, description);
                List<Pair<Beer, Price>> list = new ArrayList<>();
                barMap.put(bar, list);
                barIds.put(bar.getId(), bar);
            }
        } finally {
            cursor.close();
        }

    }

    private void addBeersToBeerMap() {
        // TODO add imagenames to the database

        // Table schema:
        //        CREATE TABLE "Beers" (
        //        `id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        //        `name`	TEXT NOT NULL UNIQUE,
        //        `manufacturer`	TEXT NOT NULL,
        //        `type`	TEXT NOT NULL,
        //        `description`	TEXT NOT NULL
        //        );

        String query = "SELECT * FROM Beers";
        String[] parameters = new String[]{};
        Cursor cursor = myDatabase.rawQuery(query, parameters);
        try {
            for (String col : cursor.getColumnNames()) {
                Log.i("Info", "Cols in beers " + col);
            }
            Log.i("Info/Rowcount: ", "" + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String manufactorer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                //Beer beer = new Beer(id, name, manufactorer, type, description);

                // TODO add imagenames to the database                            imagename
                Beer beer = new Beer(id, name, manufactorer, type, description, imageName);
                List<Pair<Bar, Price>> list = new ArrayList<>();
                beerMap.put(beer, list);
                beerIds.put(beer.getId(), beer);
            }
        } finally {
            cursor.close();
        }

    }

    private void addBeerBarTableToMaps() {

        // Table schema:
        //        CREATE TABLE "BeersBars" (
        //        `id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        //        `bar_id`	INTEGER NOT NULL,
        //        `beer_id`	INTEGER NOT NULL,
        //        `price`	INTEGER NOT NULL
        //        );


        String query = "SELECT * FROM BeersBars";
        String[] parameters = new String[]{};
        Cursor cursor = myDatabase.rawQuery(query, parameters);
        try {
            for (String col : cursor.getColumnNames()) {
                Log.i("Info", "Cols in BeersBars " + col);
            }
            Log.i("Info/BeersBars: ", "" + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int bar_id = cursor.getInt(cursor.getColumnIndex("bar_id"));
                int beer_id = cursor.getInt(cursor.getColumnIndex("beer_id"));
                int pricekr = cursor.getInt(cursor.getColumnIndex("price"));
                Price price = new Price("ISK", pricekr);
                Bar bar = barIds.get(bar_id);
                Beer beer = beerIds.get(beer_id);
                addBeerToBarMap(beer, bar, price);
                addBarToBeerMap(bar, beer, price);
            }
        } finally {
            cursor.close();
        }
    }

    private void addBeerToBarMap(Beer beer, Bar bar, Price price) {
        barMap.get(bar).add(new Pair<>(beer, price));
    }

    private void addBarToBeerMap(Bar bar, Beer beer, Price price) {
        beerMap.get(beer).add(new Pair<>(bar, price));
    }


    public List<Pair<Bar, Price>> getBarsFromBeer(Beer beer) {
        return beerMap.get(beer);
    }


    public List<Pair<Beer, Price>> getBeersFromBar(Bar bar) {
        return barMap.get(bar);
    }

    public List<Beer> searchBeers2(String searchString) {
        String query = "SELECT id FROM Beers WHERE name LIKE ? OR manufacturer LIKE ? OR type LIKE ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        List<Beer> beers = new ArrayList<>();
        Cursor cursor = myDatabase.rawQuery(query, parameters);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Beer beer = beerIds.get(id);
                beers.add(beer);
            }
        } finally {
            cursor.close();
        }

        return beers;
    }

    public List<Bar> searchBars2(String searchString) {
        String query = "SELECT id FROM Bars WHERE name LIKE ?";

        String[] parameters = new String[]{
                "%" + searchString + "%"
        };

        List<Bar> bars = new ArrayList<>();
        Cursor cursor = myDatabase.rawQuery(query, parameters);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Bar bar = barIds.get(id);
                bars.add(bar);
            }
        } finally {
            cursor.close();
        }

        return bars;
    }

    public void recreateDatabase() {

        //Beers
        myDatabase.delete("Beers", null, null);

        String beerCreateString = "CREATE TABLE IF NOT EXISTS Beers (" +
                "id INTEGER NOT NULL, " +
                "name TEXT NOT NULL UNIQUE, " +
                "manufacturer TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "imageName TEXT, " +
                "PRIMARY KEY(id))";
        myDatabase.execSQL(beerCreateString);

        //Bars
        myDatabase.delete("Bars", null, null);

        String barCreateString = "CREATE TABLE IF NOT EXISTS Bars (" +
                "id INTEGER NOT NULL, " +
                "name TEXT NOT NULL UNIQUE, " +
                "address TEXT NOT NULL, " +
                "latitude TEXT NOT NULL, " +
                "longitude TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "PRIMARY KEY(id))";
        myDatabase.execSQL(barCreateString);

        //Connections
        myDatabase.delete("BeersBars", null, null);

        String connCreateString = "CREATE TABLE IF NOT EXISTS BeersBars (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "bar_id INTEGER NOT NULL, " +
                "beer_id INTEGER NOT NULL, " +
                "price INTEGER NOT NULL)";
        myDatabase.execSQL(connCreateString);
    }

    public void fillNewDatabase(JSONObject obj) {

        try {

            // Beers
            JSONArray beersArray = obj.getJSONArray("beers");
            JSONObject beer;

            for(int i = 0; i<beersArray.length(); i++) {

                beer = (JSONObject) beersArray.get(i);
                ContentValues beerValues = new ContentValues();

                beerValues.put("id", beer.get("id").toString());
                beerValues.put("name", beer.get("name").toString());
                beerValues.put("manufacturer", beer.get("manufacturer").toString());
                beerValues.put("type", beer.get("type").toString());
                beerValues.put("description", beer.get("description").toString());
                beerValues.put("imageName", beer.get("imageName").toString());

                Log.i("JSON: ", beer.toString());

                myDatabase.insert("Beers", null, beerValues);
            }

            // Bars
            JSONArray barsArray = obj.getJSONArray("bars");
            JSONObject bar;

            for(int i = 0; i<barsArray.length(); i++) {

                bar = (JSONObject) barsArray.get(i);
                ContentValues barValues = new ContentValues();

                barValues.put("id", bar.get("id").toString());
                barValues.put("name", bar.get("name").toString());
                barValues.put("address", bar.get("address").toString());
                barValues.put("latitude", bar.get("latitude").toString());
                barValues.put("longitude", bar.get("longitude").toString());
                barValues.put("description", bar.get("description").toString());

                myDatabase.insert("Bars", null, barValues);
            }

            // Connections
            JSONArray connectionsArray = obj.getJSONArray("beersBars");
            JSONObject connection;

            for(int i = 0; i<connectionsArray.length(); i++) {

                connection = (JSONObject) connectionsArray.get(i);
                ContentValues connValues = new ContentValues();

                connValues.put("id", connection.get("id").toString());
                connValues.put("bar_id", connection.get("bar_id").toString());
                connValues.put("beer_id", connection.get("beer_id").toString());
                connValues.put("price", connection.get("price").toString());

                myDatabase.insert("BeersBars", null, connValues);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}