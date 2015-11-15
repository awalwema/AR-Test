package com.example.andrew.ar_test.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archie on 11/10/2015.
 */
public class OUDatabase  extends SQLiteOpenHelper
{
    static final String ID = "_id";
    public static final String TITLE = "title";
    static final String MESSAGE = "message";
    static final String DATE = "date";
    static final String TIME = "time";
    static final String XCOORDS = "xcoords";
    static final String YCOORDS = "ycoords";
    static final String RADIUS = "radius";
    static final String TABLE = "reminders";
    static final String LOCATION_NAME = "locationName";
    public static final String DELIVER = "delivered";

    private static final String DATABASE_NAME = "db";

    static final int DATABASE_VERSION = 1;
    Cursor cursor;

    SQLiteDatabase db;

    private static OUDatabase sInstance;

    private OUDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("+ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT, "+ MESSAGE + " TEXT, " + DATE + " TEXT, "+ TIME + " TEXT, "
                + XCOORDS + " TEXT, " + YCOORDS + " TEXT, " + RADIUS + " TEXT, " + LOCATION_NAME
                + " TEXT, " + DELIVER + " TEXT);");
    }


    public long addData(ContentValues cv){
        db = this.getWritableDatabase();
        return  db.insert(TABLE, TITLE, cv);
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


    public void deleteData(long dataItem){
        this.getWritableDatabase().delete(TABLE, "_id = " + dataItem, null);
    }

    public static OUDatabase getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new OUDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public Cursor loadReminders(){
        cursor = (SQLiteCursor) this.getReadableDatabase().rawQuery("SELECT " + OUDatabase.ID
                + ", " + OUDatabase.TITLE + ", " + OUDatabase.MESSAGE + ", "
                + OUDatabase.DATE + ", " + OUDatabase.TIME + ", "
                + OUDatabase.XCOORDS + ", " + OUDatabase.YCOORDS + ", "
                + OUDatabase.RADIUS + ", " + OUDatabase.LOCATION_NAME + ", "
                + OUDatabase.DELIVER + " FROM " + OUDatabase.TABLE + " ORDER BY "
                + OUDatabase.ID + " DESC", null);
        return cursor;
    }

    public Cursor loadReminderDetails(long id){
        cursor = this.getWritableDatabase().query(OUDatabase.TABLE, new String[]{OUDatabase.ID,
                        OUDatabase.TITLE, OUDatabase.MESSAGE, OUDatabase.DATE,
                        OUDatabase.TIME, OUDatabase.XCOORDS, OUDatabase.YCOORDS,
                        OUDatabase.RADIUS,OUDatabase.LOCATION_NAME, OUDatabase.DELIVER},
                        OUDatabase.ID + "=?", new String[] { String.valueOf(id) }, null, null,
                        null, null);
        return cursor;
    }

    public List<String> loadTitlesForNotification(String ids[]) {
        List<String> lstIds = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Select " + TITLE + " from " + TABLE + " where _id  in (");
        for (String str : ids) {
            buffer.append("'").append(str).append("'").append(",");
        }

        buffer.append("'").append("null").append("')");
        System.out.println(buffer.toString());

        Cursor cursor = this.getReadableDatabase().rawQuery(buffer.toString(), null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            lstIds.add(cursor.getString(cursor.getColumnIndex(TITLE)));
            cursor.moveToNext();
        }
        return lstIds;
    }

    public String[] getAddresses(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE, null);
        if(cursor.getCount() > 0)
        {
            String[] str = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()){
                str[i] = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
                i++;
            }
            return str;
        }
        else
        {
            return new String[] {};
        }
    }
}
