package com.example.jbt.placess_3.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jbt.placess_3.MapFragment;
import com.example.jbt.placess_3.R;

import java.util.prefs.Preferences;

/**
 * Created by Toshiba on 2/23/2016.
 */
public class SearhAdapter extends CursorAdapter {


    private LayoutInflater cursorInflator;
    private Context mcontext;
    SharedPreferences sharedPreferences;
    String unit;
    public SearhAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mcontext=context;
        cursorInflator= LayoutInflater.from(context);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


        return cursorInflator.inflate(R.layout.place_list,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        unit=sharedPreferences.getString("distanne_pref","Km");

        TextView nameText= (TextView) view.findViewById(R.id.name);
        TextView cityText= (TextView) view.findViewById(R.id.city);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageIcon);
        TextView distnace= (TextView) view.findViewById(R.id.distance);
        double lat;
        double lang;
        double location_lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lat")));
        double location_lang=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lang")));

        if(MapFragment.mLastLocation !=null){
            lat=MapFragment.mLastLocation.getLatitude();
            lang=MapFragment.mLastLocation.getLongitude();
            Location locationA= new Location("A");
            Location locationB= new Location("B");
            locationA.setLatitude(lat);
            locationA.setLongitude(lang);
            locationB.setLatitude(location_lat);
            locationB.setLongitude(location_lang);
            float disance_from=((locationA.distanceTo(locationB))/1000);

            if(unit.equals("Km")) {
                distnace.setText(String.format("  %.2f Km", disance_from));
            }
            else if(unit.equals("Mile")){
                disance_from=disance_from*0.621371f;
                distnace.setText(String.format("  %.2f Mile", disance_from));
            }


        }

        String name=cursor.getString(cursor.getColumnIndex("name"));
        String city=cursor.getString(cursor.getColumnIndex("city"));
        byte [] imageArray=cursor.getBlob(cursor.getColumnIndex("image"));

        nameText.setText(name);
        cityText.setText(city);
        imageView.setImageBitmap(FactoryImage.getImage(imageArray));



    }

    private double getDistance(double lat, double lang, double location_lat, double location_lang) {



        return 0.0;
    }


}
