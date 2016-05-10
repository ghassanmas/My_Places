package com.example.jbt.placess_3.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toshiba on 2/22/2016.
 */
public class OpenHelperDB extends SQLiteOpenHelper {


    private String table_name="history";
    String table_name2="faviorte";



    public OpenHelperDB(Context context , int version) {

        super(context, "places.db", null, version);

    }

    public void onCreate(SQLiteDatabase db) {

        String sql="CREATE TABLE "+table_name+" ("
                +"_id  INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"name" +" TEXT,"
                +"city" +" TEXT,"
                +"idd" +" TEXT,"
                +"image" +" BLOB,"
                +"lat" +" TEXT, "
                +"lang" +" TEXT);";

        String sql2="CREATE TABLE "+table_name2+" ("
                +"_id  INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"name" +" TEXT,"
                +"city" +" TEXT,"
                +"idd" +" TEXT,"
                +"image" +" BLOB,"
                +"lat" +" TEXT, "
                +"lang" +" TEXT);";

        db.execSQL(sql);
        db.execSQL(sql2);

    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql="DROP TABLE IF EXISTS "+table_name;
        db.execSQL(sql);
        onCreate(db);

    }
}
