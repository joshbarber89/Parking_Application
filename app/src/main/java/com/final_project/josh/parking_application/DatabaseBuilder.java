package com.final_project.josh.parking_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseBuilder extends SQLiteOpenHelper {

    public static final String DATABASENAME = "Settings.db";
    public static final String TABLENAME = "Settings_Table";
    public static final String COL1 = "ID";
    public static final String COL2 = "MORI";
    public static final String COL3 = "RADIUS";
    public static final String COL4 = "LONGITUDE";
    public static final String COL5 = "LATITUDE";
    public static final String COL6 = "AUTO";

    public DatabaseBuilder(Context context){
        super (context, DATABASENAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLENAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "MORI TEXT, RADIUS TEXT, LONGITUDE TEXT, LATITUDE TEXT,AUTO TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public boolean addData (String mori, String radius, String longitude, String latitude,String auto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,mori);
        contentValues.put(COL3,radius);
        contentValues.put(COL4,longitude);
        contentValues.put(COL5,latitude);
        contentValues.put(COL6,auto);

        long result = db.insert(TABLENAME,null,contentValues);

        if(result == -1){
            return false;
        }
        else {
            return true;
        }

    }

}
