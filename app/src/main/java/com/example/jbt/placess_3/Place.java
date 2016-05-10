package com.example.jbt.placess_3;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Toshiba on 2/23/2016.
 */
public class Place {

    private String name;
    private LatLng latLng;
    private String city;
    private byte[] image;
    private String id;


    public Place(String id,String name, String city, String lat, String lnd, byte [] image ){

        this.setId(id);
        this.setName(name);
        this.setCity(city);
        this.setLatLng(new LatLng(Double.parseDouble(lat),Double.parseDouble(lnd)));
        this.setImage(image);

     }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
