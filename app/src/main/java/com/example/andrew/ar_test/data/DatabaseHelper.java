package com.example.andrew.ar_test.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archie on 11/18/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db";
    static final int DATABASE_VERSION = 1;
    static final String ID = "_id";
    static final String TABLE = "places";
    public static final String PLACE_NAME = "name";
    static final String PLACE_LONG = "longitude";
    static final String PLACE_LATI = "latitude";
    static final String PLACE_TYPE = "type";

    Cursor cursor;
    SQLiteDatabase db;
    private static DatabaseHelper sInstance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLACE_NAME + " TEXT, " + PLACE_LONG + " TEXT, " + PLACE_LATI + " TEXT, " +
                PLACE_TYPE + " TEXT);");
    }


    public long addPlace(ContentValues cv) {
        db = this.getWritableDatabase();
        Log.e("ADDING PLACE:", "-----****SUCCESSFUL ON DBHELPER!****------");
        return db.insert(TABLE, PLACE_NAME, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.w(TABLE, "Upgrading database, which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    public void update(long id, ContentValues cv) {
        this.getWritableDatabase().update(TABLE, cv, "_id=" + id, null);
    }


    public void deleteData(long dataItem) {
        this.getWritableDatabase().delete(TABLE, "_id = " + dataItem, null);
    }

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    public Cursor getPlaces() {
        cursor = this.getReadableDatabase().rawQuery("SELECT " + DatabaseHelper.ID
                + ", " + DatabaseHelper.PLACE_NAME + ", " + DatabaseHelper.PLACE_LONG + ", "
                + DatabaseHelper.PLACE_LATI + ", " + DatabaseHelper.PLACE_TYPE + " FROM "
                + DatabaseHelper.TABLE + " ORDER BY " + DatabaseHelper.ID + " DESC", null);
        return cursor;
    }

    public Cursor getPlacesById(long id) {
        cursor = this.getWritableDatabase().query(DatabaseHelper.TABLE, new String[]{
                DatabaseHelper.ID, DatabaseHelper.PLACE_NAME, DatabaseHelper.PLACE_LONG,
                DatabaseHelper.PLACE_LATI, DatabaseHelper.PLACE_TYPE}, DatabaseHelper.ID
                + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        return cursor;
    }

    public List<String> loadTitlesForNotification(String ids[]) {
        List<String> lstIds = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Select " + PLACE_NAME + " from " + TABLE + " where _id  in (");
        for (String str : ids) {
            buffer.append("'").append(str).append("'").append(",");
        }

        buffer.append("'").append("null").append("')");
        System.out.println(buffer.toString());

        Cursor cursor = this.getReadableDatabase().rawQuery(buffer.toString(), null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            lstIds.add(cursor.getString(cursor.getColumnIndex(PLACE_NAME)));
            cursor.moveToNext();
        }
        return lstIds;
    }

    public String[] getPlaceName() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.getCount() > 0) {
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                str[i] = cursor.getString(cursor.getColumnIndex(PLACE_NAME));
                i++;
            }
            return str;
        } else {
            return new String[]{};
        }
    }
}
