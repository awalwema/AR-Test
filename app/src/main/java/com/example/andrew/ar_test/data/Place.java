package com.example.andrew.ar_test.data;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    Integer type;
    int id;
    ContentValues container;
    Context cont;

    public static final  HashMap<String, Integer> map = new HashMap<String, Integer>();

    // Empty constructor
    public Place(){

    }

    public Place(Context cont){
        this.cont = cont;
    }

    public Place(int id,String name, Double lat, Double lon, Integer type){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
    }

    public Place(JSONObject loc){
        container = new ContentValues();

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

            if(name != null){
                type = filterType(name);
            }

            // Testing DatabaseHelper class instead of handler
            container.put(DatabaseHelper.PLACE_NAME, name);
            container.put(DatabaseHelper.PLACE_LONG, lon.toString());
            container.put(DatabaseHelper.PLACE_LATI, lat.toString());
            container.put(DatabaseHelper.PLACE_TYPE, type.toString());
            DatabaseHelper.getInstance(cont).addPlace(container);

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

    public Integer getType() { return filterType(name); }

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

    public void setType(Integer type){ this.type = type; }

    public void setID(int id)
    {
        this.id = id;
    }

    public JSONObject getJSON()
    {
        return this.jo;
    }

    private Integer filterType( String name ) {
        map.put("Human Health Building", 238);
        map.put("Kresge Library", 19);
        map.put("North Foundation Hall", 34);
        map.put("Graham Health Center", 17);
        map.put("Meadow Brook Theatre", 3);
        map.put("Oakland Center", 9);
        map.put("South Foundation Hall", 2);
        map.put("O'Dowd Hall", 952);
        map.put("Engineering Center", 476);
        map.put("Hannah Hall", 28);
        map.put("Dodge Hall", 4);
        map.put("Police and Support Services Building", 13);
        map.put("Belgian Barn", 7);
        map.put("Elliott Hall", 8);
        map.put("Pawley Hall", 16);
        map.put("Athletics Center O'Rena", 11);
        map.put("Central Heating Plant", 169);
        map.put("Varner Hall", 32);

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //place_id = pair.getValue().toString();

            if (pair.getKey().toString().equalsIgnoreCase(name)) {
                return (Integer) pair.getValue();
            }
        }

        return 0;
    }
}
