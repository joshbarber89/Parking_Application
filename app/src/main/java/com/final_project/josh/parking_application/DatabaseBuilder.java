package com.final_project.josh.parking_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.final_project.josh.parking_application.models.User_Settings;

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

    public boolean updateData(String mori, String radius, String longitude, String latitude,String auto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,mori);
        contentValues.put(COL3,radius);
        contentValues.put(COL4,longitude);
        contentValues.put(COL5,latitude);
        contentValues.put(COL6,auto);

        long result = -1;

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLENAME+" ORDER BY "+COL1+" DESC LIMIT 1",null);
        if(cursor.moveToFirst()) {
            result = db.update(TABLENAME,contentValues,COL1+"=?",new String[]{cursor.getString(cursor.getColumnIndex("ID"))});
        }
        else{
            result = db.insert(TABLENAME,null,contentValues);
        }

        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public User_Settings getData () {
        User_Settings user = new User_Settings();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLENAME+" ORDER BY "+COL1+" DESC LIMIT 1",null);
        if(cursor.moveToFirst()){
            String mori= cursor.getString(cursor.getColumnIndex("MORI"));
            String radius= cursor.getString(cursor.getColumnIndex("RADIUS"));
            String longitude= cursor.getString(cursor.getColumnIndex("LONGITUDE"));
            String latitude= cursor.getString(cursor.getColumnIndex("LATITUDE"));
            String auto= cursor.getString(cursor.getColumnIndex("AUTO"));
            user.setMori(mori);
            user.setRadius(radius);
            user.setLongitude(longitude);
            user.setLatitude(latitude);
            user.setAuto(auto);
        }else{
            return null;
        }

        return user;
    }


}
