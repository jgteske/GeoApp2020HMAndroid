package com.example.geoapp2020.ui.data;
/**
 * source: 6ter_Lehrbrief_GeoApp
 * adapted to fit the Fragment management from jteske 03.07.2020
 *
 * Manages all functions to access the Database and Tables
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class DBAccess extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private String tableSQL;
    private String table;


    /**
     * Create DBAccess
     *
     * @param activity
     * @param dbName
     * @param tableSQL
     */
    public DBAccess(Context activity, String dbName, String tableSQL) {
        super(activity, dbName, null, 1);
        this.tableSQL = tableSQL;
        getTableName();

        // creating a database with getWritableDatabase(), if it doesn't exist already
        db = this.getWritableDatabase();
    }

    /**
     * Create DB Access
     *
     * @param activity
     * @param dbName
     */
    public DBAccess(Context activity, String dbName) {
        super(activity, dbName, null, 1);
        table="table_1";

        // creating a database with getWritableDatabase(), if it doesn't exist already
        db = this.getWritableDatabase();
    }

    /**
     * determine table name
     */
    private void getTableName() {
        String sql = tableSQL.toUpperCase();
        StringTokenizer tokenizer = new StringTokenizer(sql);
        // search for table name
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if(token.equals("TABLE")) {
                table = tokenizer.nextToken();
                break;
            }
        }
    }

    /**
     * Creates a table within the database, if it doesn't already exist
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // create table
            db.execSQL(tableSQL);
            int d = Log.d("database", "table created");
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
    }

    /**
     * If the table within the database does exist, calls this function to update it
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    /**
     * Closes database
     */
    @Override
    public synchronized void close() {
        if(db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    /**
     * Adds a row with a Dataset to the table
     *
     * @param dataset
     * @return
     */
    public long addDataset(Dataset dataset) {
        try {
            ContentValues data = createDataObject(dataset);

            // checking if table exists
            if(checkForTableExists(db, table) == false){
                onCreate(db); // if Dataset can't be inserted tries to create the table - prevents a problem when access had not been granted before writing the Dataset
            }
            return db.insert(table, null, data); // id is being created automatically
        }
        catch(Exception ex) {
            Log.d("database", ex.getMessage());
            return -1;
        }
    }

    /**
     * Create a request of Cursor, orderBy LocationName
     *
     * @return
     */
    public Cursor createListViewCursor() {
        String[] columns = new String[]{"_id", "LocationName", "LocationLatitude", "LocationLongitude", "RecordingDate"};
        // requesting DB with .query
        return db.query(table, columns, null, null, null, null, "LocationName");
    }

    /**
     * Get List<Dataset> array with all datasets
     *
     * @return
     */
    public List<Dataset> readDataset() {
        List<Dataset> result = new ArrayList<Dataset>();
        Cursor cursor = null;
        try {
            cursor = db.query(table, null, null, null, null, null, null);
            int count = cursor.getCount();
            cursor.moveToFirst();
            for(int i = 0; i < count; i++) {
                Dataset ds = createDataset(cursor);
                result.add(ds);
                cursor.moveToNext();
            }
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
        finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Update a Dataset
     *
     * @param ds
     */
    public void updateDataset(Dataset ds) {
        try {
            ContentValues data = createDataObject(ds);
            db.update(table, data, "id = " + ds.id, null);
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
    }


    /**
     * Delete an exciting Dataset
     *
     * @param ds
     */
    public void deletDataset(Dataset ds) {
        try {
            db.delete(table, "id = " + ds.id, null);
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
    }

    /**
     * Create a Dataset
     *
     * @param cursor
     * @return
     */
    private Dataset createDataset(Cursor cursor) {
        Dataset ds = new Dataset();
        ds.id = cursor.getLong(0);
        ds.LocationName = cursor.getString(1);
        ds.LocationLatitude = cursor.getDouble(2);
        ds.LocationLongitude = cursor.getDouble(3);
        try { //
            long today = cursor.getLong(4);
            ds.RecordingDate = new Date(today);
        }
        catch(Exception ex) {
            ds.RecordingDate = null;
        }
        return ds;
    }

    /**
     * Create a DataObject
     *
     * @param ds
     * @return
     */
    private ContentValues createDataObject(Dataset ds) {
        ContentValues data = new ContentValues();
        data.put("LocationName", ds.LocationName);
        data.put("LocationLatitude", ds.LocationLatitude);
        data.put("LocationLongitude", ds.LocationLongitude);
        if(ds.RecordingDate != null) {
            data.put("RecordingDate", ds.RecordingDate.getTime());
        }
        return data;
    }

    /**
     * Checks if a table does exist in the Database
     *
     * https://readyandroid.wordpress.com/how-do-i-check-in-sqlite-whether-a-table-exists/
     * @param db
     * @param table
     * @return
     */
    private boolean checkForTableExists(SQLiteDatabase db, String table) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }
}
