package is.bjorfinnur.bjorfinnur.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import is.bjorfinnur.bjorfinnur.data.Price;
import is.bjorfinnur.bjorfinnur.data.Bar;
import is.bjorfinnur.bjorfinnur.data.Beer;
import is.bjorfinnur.bjorfinnur.data.GpsCoordinates;

public class DataBaseManager extends SQLiteOpenHelper {

    private static DataBaseManager databaseManagerInstance;
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
    private DataBaseManager(Context context) {
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

        setUpMaps();
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    private void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

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
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
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
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }


    public List<Beer> searchBeers(String searchString) {

        List<Beer> beerList = new ArrayList<>();
        String beerName;
        String manufacturer;
        String type;

        String query = "SELECT * FROM Beers WHERE name LIKE ? OR manufacturer LIKE ? OR type LIKE ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                beerName = cursor.getString(cursor.getColumnIndex("name"));
                manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                type = cursor.getString(cursor.getColumnIndex("type"));

                beerList.add(new Beer(0, beerName, manufacturer, type, ""));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return beerList;
    }

    public List<Bar> searchBars(String searchString) {

        List<Bar> barList = new ArrayList<>();
        String barName;
        String address;
        String description;
        String latitude;
        String longitude;

        String query = "SELECT * FROM Bars WHERE name LIKE ? OR address LIKE ?";

        Log.i("Grimbill", query);

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                barName = cursor.getString(cursor.getColumnIndex("name"));
                address = cursor.getString(cursor.getColumnIndex("address"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                longitude = cursor.getString(cursor.getColumnIndex("longitude"));

                barList.add(new Bar(0, barName, address, description, latitude, longitude));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return barList;
    }


    public ArrayList<String> getBarNames(String searchString) {

        ArrayList<String> barNames = new ArrayList<>();


        String query = "SELECT DISTINCT Bars.name FROM Bars WHERE Bars.name LIKE ? OR Bars.address = ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                String bname = cursor.getString(cursor.getColumnIndex("name"));
                barNames.add(bname);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return barNames;
    }


    public List<GpsCoordinates> getBarCoordinates(String searchString) {

        List<GpsCoordinates> coordinatesList = new ArrayList<>();

        float latitude;
        float longitude;

        String query = "SELECT DISTINCT Bars.latitude,Bars.longitude FROM Bars WHERE Bars.name LIKE ? OR Bars.address = ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                latitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex("latitude")));
                longitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex("longitude")));
                coordinatesList.add(new GpsCoordinates(latitude,longitude));

                cursor.moveToNext();
            }
            cursor.close();
        }

        return coordinatesList;
    }

    public List<GpsCoordinates> getBeerCoordinates(String searchString) {

        List<GpsCoordinates> coordinatesList = new ArrayList<>();

        float latitude;
        float longitude;

        String query = "SELECT DISTINCT Bars.latitude,Bars.longitude FROM Beers,Bars,BeersBars WHERE Beers.id == BeersBars.beer_id AND Bars.id == BeersBars.bar_id AND Beers.name LIKE ? OR Beers.manufacturer LIKE ? OR Beers.type LIKE ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                latitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex("latitude")));
                longitude = Float.parseFloat(cursor.getString(cursor.getColumnIndex("longitude")));
                coordinatesList.add(new GpsCoordinates(latitude,longitude));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return coordinatesList;
    }

    public ArrayList getBarName(String searchString) {

        ArrayList barName = new ArrayList<>();

        String query = "SELECT DISTINCT Bars.name FROM Beers,Bars,BeersBars WHERE Beers.id == BeersBars.beer_id AND Bars.id == BeersBars.bar_id AND Beers.name LIKE ? OR Beers.manufacturer LIKE ? OR Beers.type LIKE ?";

        String[] parameters = new String[]{
                "%" + searchString + "%",
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor cursor = myDatabase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                String name = cursor.getString(cursor.getColumnIndex("name"));
                barName.add(name);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return barName;
    }


    public static DataBaseManager getDatabaseManager(Context context) {
        if(databaseManagerInstance == null){
            initializeDatabaseManager(context);
        }
        return databaseManagerInstance;
    }

    private static void initializeDatabaseManager(Context context) {
        databaseManagerInstance = new DataBaseManager(context);
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
            for(String col: cursor.getColumnNames()){
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
            for(String col: cursor.getColumnNames()){
                Log.i("Info", "Cols in beers " + col);
            }
            Log.i("Info/Rowcount: ", "" + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String manufactorer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                Beer beer = new Beer(id, name, manufactorer, type, description);
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
            for(String col: cursor.getColumnNames()){
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


    public List<Pair<Beer,Price>> getBeersFromBar(Bar bar) {
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

}