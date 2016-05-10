package com.example.jbt.placess_3;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jbt.placess_3.Database.DB_Handler;
import com.example.jbt.placess_3.Tasks.SearchByMyPlace;
import com.example.jbt.placess_3.provider.PlaceProvider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends Activity implements SearchLocationEvent, ListFragment.onClickedjad, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ItemFragment.callfavorite  {



    SharedPreferences sharedPreferences;
   public GoogleApiClient googleApiClient=null;
    public static MapFragment mapFragment;
    static ProgressDialog loadingDialog;
    public static String type_select;

    public static boolean fa=false;
    public static boolean reultss=false;
 public static   ListFragment itemFragment;
    public static  boolean land_first=false;
    public  static boolean port=false;
    DB_Handler db_handler=new DB_Handler(this);
    PowerConnectionReceiver power;

    ItemFragment itemFragment2;


    public static boolean someOne =false;
   public static double location_lat;
    public  static double  location_lang;
    public static String location_name;


    Location mLastLocation;
    PlaceProvider placeProvider=new PlaceProvider();
    private Menu menu;
    private MenuItem delete_all;
    private MenuItem  back;
    private MenuItem setting;
    private MenuItem go_faviorte;
    public static boolean faviorteState=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        power= new PowerConnectionReceiver();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);


         mapFragment=new MapFragment();
        itemFragment2=new ItemFragment();

         itemFragment= new ListFragment();
        if(landScape()){

            if(savedInstanceState==null || port ) {
                getFragmentManager().beginTransaction().add(R.id.frame_list, itemFragment).commit();
                getFragmentManager().beginTransaction().add(R.id.frame_map, mapFragment).commit();

                    if(savedInstanceState==null){

                        land_first=true;
                    }else{
                        port=false;
                        land_first=false;

                    }



            }
        }else {

            if(savedInstanceState==null || land_first) {
                getFragmentManager().beginTransaction().add(R.id.container, itemFragment).commit();



                if(savedInstanceState==null){

                    port=true;
                }else{

                    port=false;
                    land_first=false;

                }




            }
        }

        googleApiClient =  new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();








    }

    private void checkApi(GoogleApiClient googleApiClient) {

        if (googleApiClient == null) {
            googleApiClient =  new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        googleApiClient.connect();

    }

    private boolean landScape() {
        View view =findViewById(R.id.landsacpe);
        if(view!=null){
            return true;
        }
        else {
            return false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu=menu;

        getMenuInflater().inflate(R.menu.menu_main, menu);
        delete_all=menu.findItem(R.id.delete_favorite);
        back=menu.findItem(R.id.back);
        setting=menu.findItem(R.id.action_settings);
        go_faviorte=menu.findItem(R.id.favorite);
        delete_all.setVisible(false);
        back.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){

            case R.id.action_settings:
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivityForResult(intent, 1);
                return  true;


            case R.id.favorite:
                setting.setVisible(false);
                go_faviorte.setVisible(false);
                delete_all.setVisible(true);
                back.setVisible(true);


                if(landScape()){

                    getFragmentManager().beginTransaction().replace(R.id.frame_list,itemFragment2).addToBackStack(null).commit();
                }
                else
                {
                    getFragmentManager().beginTransaction().replace(R.id.container,itemFragment2).addToBackStack(null).commit();

                }

                return true;

            case R.id.delete_favorite:

                getContentResolver().delete(PlaceProvider.CONTENT_URI_F,null,null);
                itemFragment2.faviorteAdapter.swapCursor(itemFragment2.lastCursorFav);
                itemFragment2.faviorteAdapter.notifyDataSetChanged();
                getContentResolver().notifyChange(PlaceProvider.CONTENT_URI_F,null);
                return true;

            case R.id.back:

                setting.setVisible(true);
                go_faviorte.setVisible(true);
                delete_all.setVisible(false);
                back.setVisible(false);

                getFragmentManager().popBackStack();


                return true;

        }



        return super.onOptionsItemSelected(item);
    }

    public void callMe(){


    }

    public void finishMe(){

    }


    @Override
    public void buttonLocation() {
        loadingDialog= ProgressDialog.show(MainActivity.this,"","Loading Location near you ",true);
        mapFragment.searchLoc();


    }

    public static void doIt(){

       // Toast.makeText(this,"Finished",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void dooooIt() {
      //  loadingDialog= ProgressDialog.show(MainActivity.this,"","Loading Location near you ",true);


           int radius=Integer.parseInt(sharedPreferences.getString("radians","500"));
        StringBuilder stringBuilder=new StringBuilder();
        if(mLastLocation!=null) {
            stringBuilder.append("https://maps.googleapis.com/maps/api/place/search/json?location=");
            stringBuilder.append(mLastLocation.getLatitude());
            stringBuilder.append(",");
            stringBuilder.append(mLastLocation.getLongitude());
            if(MainActivity.type_select!=null){
                stringBuilder.append("&type="+MainActivity.type_select);
            }

            stringBuilder.append("&radius="+radius);
            stringBuilder.append("&sensor=true&key=AIzaSyAbKXsZwjraDcZBWwqGdrghSkF9YtVqDlE");

            SearchByMyPlace myPlace = new SearchByMyPlace(this);

            myPlace.execute(stringBuilder.toString());
        }
        else{
            Toast.makeText(this,"Location Seravices Not Avaiable !",Toast.LENGTH_SHORT).show();
        }





    }

    @Override
    public void kabas(long l) {


        String [] projection={"_id","name","image","city","lat","lang"};
        String [] selection={"_id"};

     //  Cursor curosr= this.getContentResolver().query(PlaceProvider.CONTENT_URI,projection,l+"", selection,null);
        //curosr.moveToNext();
        Cursor cursor=db_handler.takeRow(l);
        cursor.moveToNext();
         location_lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lat")));
         location_lang=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lang")));
        location_name=cursor.getString(cursor.getColumnIndex("name"));
        if(landScape()){
         mapFragment.landSet();
        }
        else
        {
            getFragmentManager().beginTransaction().replace(R.id.container,mapFragment).addToBackStack(null).commit();
            someOne=true;
        }

    }

    @Override
    public void searchIT() {

       EditText naee= (EditText) itemFragment.getView().findViewById(R.id.editText);
       String searchbutton= naee.getText().toString();
        String finalString=searchbutton.replace(' ', '+');



        StringBuilder stringBuilder=new StringBuilder();
        int radius=Integer.parseInt(sharedPreferences.getString("radians","500"));
            stringBuilder.append("https://maps.googleapis.com/maps/api/place/textsearch/json?query=");

            stringBuilder.append(finalString);
        if(MainActivity.type_select!=null){
            stringBuilder.append("&type="+MainActivity.type_select);
        }
        stringBuilder.append("&radius="+radius);
            stringBuilder.append("&key=AIzaSyAbKXsZwjraDcZBWwqGdrghSkF9YtVqDlE");

            SearchByMyPlace myPlace = new SearchByMyPlace(this);

            myPlace.execute(stringBuilder.toString());


    }

    @Override
    public void saveIT(long id) {
        Cursor cursor=db_handler.takeRow(id);
        cursor.moveToNext();
        ContentValues cv =new ContentValues();
        cv.put("name",cursor.getString(cursor.getColumnIndex("name")));
        cv.put("idd",cursor.getString(cursor.getColumnIndex("idd")));
        cv.put("image",cursor.getBlob(cursor.getColumnIndex("image")));
        cv.put("city", cursor.getString(cursor.getColumnIndex("city")));
        cv.put("lat",cursor.getString(cursor.getColumnIndex("lat")));
        cv.put("lang",cursor.getString(cursor.getColumnIndex("lang")));
        getContentResolver().insert(PlaceProvider.CONTENT_URI_F,cv);
    }


    public void  makeTost(){
      Toast.makeText(this,"Reulste is "+reultss,Toast.LENGTH_SHORT).show();
  }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Toast.makeText(this,"Cooonceted",Toast.LENGTH_SHORT).show();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this,"Faileeeee",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();


        checkApi(googleApiClient);
    }

    public static void changeOccurs() {

        itemFragment.searhAdapter.swapCursor(itemFragment.lastcursor);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        itemFragment.searhAdapter.swapCursor(itemFragment.lastcursor);
       itemFragment.searhAdapter.notifyDataSetChanged();

    }

    @Override
    public void showMap(long i) {

        //  Cursor curosr= this.getContentResolver().query(PlaceProvider.CONTENT_URI,projection,l+"", selection,null);
        //curosr.moveToNext();

        faviorteState=false;
        Cursor cursor =db_handler.takeRow_f(i);
        cursor.moveToNext();
        location_lat=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lat")));
        location_lang=Double.parseDouble(cursor.getString(cursor.getColumnIndex("lang")));
        location_name=cursor.getString(cursor.getColumnIndex("name"));
        if(landScape()){
            mapFragment.landSet();
        }
        else
        {
            getFragmentManager().beginTransaction().replace(R.id.container,mapFragment).addToBackStack(null).commit();
            someOne=true;
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK  && faviorteState){

            setting.setVisible(true);
            go_faviorte.setVisible(true);
            delete_all.setVisible(false);
            back.setVisible(false);


        }
        faviorteState=true;

        return super.onKeyDown(keyCode, event);
    }
}
