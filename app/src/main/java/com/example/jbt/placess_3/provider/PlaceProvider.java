package com.example.jbt.placess_3.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.jbt.placess_3.Database.OpenHelperDB;

import java.net.URI;
import java.util.List;

/**
 * Created by Toshiba on 2/27/2016.
 */
public class PlaceProvider extends ContentProvider {


      public final static String table_name="history";
    public final static String table_name2="faviorte";
      public final static String authoriy="com.example.jbt.placess_3";
     public final static Uri CONTENT_URI=Uri.parse("content://"+authoriy+"/"+table_name);
    public final static Uri CONTENT_URI_F=Uri.parse("content://"+authoriy+"/"+table_name2);
    OpenHelperDB openHelperDB;
    @Override
    public boolean onCreate() {

        openHelperDB= new OpenHelperDB(getContext(),2);
        if(openHelperDB!=null){
           return true;
        }else
        {
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db=openHelperDB.getReadableDatabase();
        Cursor c=db.query(getTableName(uri),strings,s,strings1,null,null,s1);
        c.setNotificationUri(getContext().getContentResolver(),uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        SQLiteDatabase db=openHelperDB.getWritableDatabase();
        Long id=db.insertWithOnConflict(getTableName(uri),null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        getContext().getContentResolver().notifyChange(uri,null);
        if(id>0){
            return ContentUris.withAppendedId(uri,id);
        }
        else {

            return null;
        }
    }

    private String getTableName(Uri uri) {
        List<String> pathSements=uri.getPathSegments();
         return pathSements.get(0);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db=openHelperDB.getWritableDatabase();

        db.delete(getTableName(uri),s,strings);


        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public  Cursor takeRow(Uri uri,long id){
        SQLiteDatabase db=openHelperDB.getReadableDatabase();

        Cursor cursor=db.query(getTableName(uri),null,"_id =?  ",new String [] {id+""},null,null,null);
        return cursor;
    }
}
