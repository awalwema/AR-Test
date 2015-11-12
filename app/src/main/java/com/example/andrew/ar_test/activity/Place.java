package com.example.andrew.ar_test.activity;

import org.json.JSONObject;

/**
 * Created by Andrew on 11/10/2015.
 */
public class Place {

    JSONObject geo = null;
    JSONObject jo = null;
    JSONObject coordinates;
    String name;
    String user;
    Double lat = null;
    Double lon = null;
    int id;


    // Empty constructor
    public Place(){

    }

    public Place(int id,String name, Double lat, Double lon){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;

    }

    public Place(JSONObject loc){

        try {
            jo = loc;
            if (!loc.isNull("geometry")) {
                geo = loc.getJSONObject("geometry");
                coordinates = geo.getJSONObject("location");
                lat = Double.parseDouble(coordinates.getString("lat"));
                lon = Double.parseDouble(coordinates.getString("lng"));
            }

            if (lat != null) {
                name = loc.getString("name");
                user = name;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // getting ID
    public Double getlat(){
        return this.lat;
    }

    public Double getlng(){
        return this.lon;
    }

    public String getName(){
        return this.name;
    }

    public String getUser(){
        return this.user;
    }

    public int getID()
    {
        return this.id;
    }

    public void selat(Double latitude)
    {
        this.lat =latitude;
    }

    public void setlng(Double longitude)
    {
        this.lon = longitude;
    }

    public void setName( String name)
    {
        this.name = name;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public JSONObject getJSON()
    {
        return this.jo;
    }
}
