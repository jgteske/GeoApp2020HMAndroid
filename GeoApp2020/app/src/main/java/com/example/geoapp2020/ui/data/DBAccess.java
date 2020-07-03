package com.example.geoapp2020.ui.data;
// source: 6ter_Lehrbrief_GeoApp
// adapted to fit the Fragment management from jteske 03.07.2020


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



    public DBAccess(Context activity, String dbName, String tableSQL) {
        super(activity, dbName, null, 1);
        this.tableSQL = tableSQL;
        getTableName();

        // creating a database with getWritableDatabase(), if it doesn't exist already
        db = this.getWritableDatabase();
    }

    // determine table name
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

    @Override
    // if the table within the database doesn't exist on first access, calls this function to create it
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

    @Override
    // if the table within the database does exist, calls this function to update it
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    @Override
    // closes database
    public synchronized void close() {
        if(db != null) {
            db.close();
            db = null;
        }
        super.close();
    }

    // adds a row with a Dataset to the table
    public long addDataset(Dataset dataset) {
        try {
            ContentValues data = createDataObject(dataset);
            return db.insert(table, null, data); // id is being created automatically
        }
        catch(Exception ex) {
            Log.d("database", ex.getMessage());
            return -1;
        }
    }

    // creates request of Cursor, orderBy LocationName
    public Cursor createListViewCursor() {
        String[] columns = new String[]{"_id", "LocationName", "LocationLatitude", "LocationLongitude", "RecordingDate"};
        // requesting DB with .query
        return db.query(table, columns, null, null, null, null, "LocationName");
    }

    // get list with all datasets
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

    public void updateDataset(Dataset ds) {
        try {
            ContentValues data = createDataObject(ds);
            db.update(table, data, "id = " + ds.id, null);
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
    }


    public void deletDataset(Dataset ds) {
        try {
            db.delete(table, "id = " + ds.id, null);
        }
        catch(Exception ex) {
            Log.e("database", ex.getMessage());
        }
    }

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
}
