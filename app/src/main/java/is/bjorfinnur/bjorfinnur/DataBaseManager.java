package is.bjorfinnur.bjorfinnur;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    private static DataBaseManager databaseManagerInstance;
    // Default system data path
    private final String dataBasePath;
    // Name of database to be used
    private static final String DB_NAME = "BeerFinder.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     */
    public DataBaseManager(Context context) {
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
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
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

        Cursor cursor = myDataBase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                beerName = cursor.getString(cursor.getColumnIndex("name"));
                manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                type = cursor.getString(cursor.getColumnIndex("type"));

                beerList.add(new Beer(beerName, manufacturer, type));

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

        Cursor cursor = myDataBase.rawQuery(query, parameters);

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                barName = cursor.getString(cursor.getColumnIndex("name"));
                address = cursor.getString(cursor.getColumnIndex("address"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                longitude = cursor.getString(cursor.getColumnIndex("longitude"));

                barList.add(new Bar(barName, address, description, latitude, longitude));

                cursor.moveToNext();
            }
            cursor.close();
        }
        return barList;
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

        Cursor cursor = myDataBase.rawQuery(query, parameters);

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

        Cursor cursor = myDataBase.rawQuery(query, parameters);

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




    public static DataBaseManager getDatabaseManager(Context context) {
        if(databaseManagerInstance == null){
            initializeDatabaseManager(context);
        }
        return databaseManagerInstance;
    }

    private static void initializeDatabaseManager(Context context) {
        databaseManagerInstance = new DataBaseManager(context);
    }


}