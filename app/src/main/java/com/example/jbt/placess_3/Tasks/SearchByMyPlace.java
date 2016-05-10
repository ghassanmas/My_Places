package com.example.jbt.placess_3.Tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.jbt.placess_3.Database.DB_Handler;
import com.example.jbt.placess_3.MainActivity;
import com.example.jbt.placess_3.Place;
import com.example.jbt.placess_3.provider.PlaceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;

/**
 * Created by Toshiba on 2/22/2016.
 */
public class SearchByMyPlace extends AsyncTask<String,Void,String> {

    private Context innerContext;
    private String table_name="history";

public SearchByMyPlace(Context context)
{
    this.innerContext=context;
}

    DB_Handler db_handler=new DB_Handler(innerContext);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(innerContext, "bedo yero7 3al net hasa ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection connection=null;
        StringBuilder response=new StringBuilder();
        BufferedReader input=null;
        try {
             url=new URL(strings[0]);
            connection= (HttpURLConnection) url.openConnection();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            while((line=input.readLine())!=null){
                response.append(line+"\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(connection!=null){
                    connection.disconnect();
                }

            }
        }

        return response.toString();


    }


    @Override
    protected void onPostExecute(String s) {

        JSONObject jsonObject=null;
        JSONArray result=null;
       String statuss;


        try {
             jsonObject=new JSONObject(s);
            result=jsonObject.getJSONArray("results");
           statuss=jsonObject.getString("status");
            if(statuss.equals("OK")){
                MainActivity.reultss = true;
            }else{
                MainActivity.reultss=false;
            }
          //  Toast.makeText(innerContext, "Reulste is " + statuss, Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(result.length()>=1){
            innerContext.getContentResolver().delete(PlaceProvider.CONTENT_URI,null,null);
        }

     Toast.makeText(innerContext, result.length() +" sfdfdfd", Toast.LENGTH_SHORT).show();
        for(int i=0;i<result.length();i++){

            JSONObject placeJson= null;


            try {

                 placeJson=result.getJSONObject(i);
                String imageurl=placeJson.getString("icon");
                byte [] image=new GetImage().execute(imageurl).get();

                String name=placeJson.getString("name");
                String idd=placeJson.getString("id");
                String city=placeJson.optString("vicinity");
                JSONObject jeo=placeJson.getJSONObject("geometry");
                JSONObject location=jeo.getJSONObject("location");
                String lat=location.getString("lat");
                String lng=location.getString("lng");
                ContentValues cv=new ContentValues();
                cv.put("name",name);
                cv.put("idd",idd);
                cv.put("image",image);
                cv.put("city", city);
                cv.put("lat",lat);
                cv.put("lang",lng);
                innerContext.getContentResolver().insert(PlaceProvider.CONTENT_URI,cv);

                Place newPlace=new Place(idd,name,city,lat,lng,image);
//                db_handler.insert(newPlace,table_name);


                //String name=


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }




       
        MainActivity.doIt();


    }


}
