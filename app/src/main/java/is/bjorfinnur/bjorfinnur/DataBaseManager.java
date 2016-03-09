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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {

    // Default system data path
    private String dataBasePath;
    // Name of database to be used
    private static String DB_NAME = "BeerFinder.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     */
    public DataBaseManager(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        // Get the path from the current system
        dataBasePath = myContext.getDatabasePath(DB_NAME).getPath();


        try {
            this.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            this.openDataBase();
        }catch(SQLException sqle){
            throw new Error("Unable to open database");
        }

    }



    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){

        }else{

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
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = dataBasePath + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = dataBasePath + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = dataBasePath + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Method to test if the database helper works
    public String testData() {

        String select = "SELECT name FROM Beers WHERE _id = ?";

        Cursor cursor = myDataBase.rawQuery(select, new String[] {"2"});

        String data = "";

        if(cursor.moveToNext()) {
            do{
                data = cursor.getString(cursor.getColumnIndex("name"));
            }while(cursor.moveToNext());
        }

        cursor.close();

        return data;
    }

    public List<Beer> searchBeers(String searchString) {

        List<Beer> beerList = new ArrayList<>();
        String beerName;
        String manufacturer;
        String type;

        String query = "SELECT * FROM Beers WHERE name LIKE ? OR manufacturer LIKE ? OR type LIKE ?";

        Cursor cursor = myDataBase.rawQuery(query, new String[] {'%'+searchString+'%','%'+searchString+'%','%'+searchString+'%'});

        if(cursor != null){

            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++){

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

}