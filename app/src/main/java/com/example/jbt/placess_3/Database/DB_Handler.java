package com.example.jbt.placess_3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jbt.placess_3.Place;

/**
 * Created by Toshiba on 2/23/2016.
 */
public class DB_Handler  {


    private OpenHelperDB openHelperDB;

    public DB_Handler(Context context){

        openHelperDB=new OpenHelperDB(context, 2);
    }

    public Cursor returnAll(String table_name){

        SQLiteDatabase sqLiteDatabas= openHelperDB.getReadableDatabase();
        Cursor cursor=sqLiteDatabas.query(table_name,null,null,null,null,null,null);

        return cursor;
    }

    public void insert(Place place,String table_name){

        SQLiteDatabase sqLiteDatabase=openHelperDB.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name",place.getName());
        cv.put("idd",place.getId());
        cv.put("image",place.getImage());
        cv.put("city",place.getCity());
        cv.put("lat",place.getLatLng().latitude);
        cv.put("land",place.getLatLng().longitude);
        sqLiteDatabase.insertOrThrow(table_name,null,cv);

        sqLiteDatabase.close();



    }


    public  Cursor takeRow(long id){
        SQLiteDatabase db=openHelperDB.getReadableDatabase();

        Cursor cursor=db.query("history",null,"_id =?  ",new String [] {id+""},null,null,null);
        //db.close();
        return cursor;

    }


    public  Cursor takeRow_f(long id){
        SQLiteDatabase db=openHelperDB.getReadableDatabase();

        Cursor cursor=db.query("faviorte",null,"_id =?  ",new String [] {id+""},null,null,null);
        //db.close();
        return cursor;

    }




}
