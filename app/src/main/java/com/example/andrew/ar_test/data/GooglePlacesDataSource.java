package com.example.andrew.ar_test.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.andrew.ar_test.ui.IconMarker;
import com.example.andrew.ar_test.ui.Marker;
import com.jwetherell.augmented_reality.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends DataSource to fetch data from Google Places.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class GooglePlacesDataSource extends NetworkDataSource {

	private static final String URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	private static final String TYPES = "airport|amusement_park|aquarium|art_gallery|bus_station|" +
			"campground|car_rental|city_hall|embassy|establishment|hindu_temple|" +
			"local_governemnt_office|locality|mosque|museum|night_club|park|place_of_worship|" +
			"police|post_office|stadium|spa|subway_station|synagogue|taxi_stand|train_station|" +
			"travel_agency|University|zoo";
	private static String key = null;
	private static Bitmap icon = null;
    public String info;
    public JSONObject place;


	public GooglePlacesDataSource(Resources res) {
		if (res == null) throw new NullPointerException();

		key = res.getString(R.string.google_places_api_key);

		createIcon(res);
	}

	protected void createIcon(Resources res) {
		if (res == null) throw new NullPointerException();

		icon = BitmapFactory.decodeResource(res, R.drawable.buzz);
	}

	@Override
	public String createRequestURL(double lat, double lon, double alt, float radius, String locale) {
		try {

            return URL + "location="+lat+","+lon+"&radius="+(radius*1000.0f)+"&types="+TYPES+"&sensor=true&key="+key;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


    public String createRequestURL2(String place_id, String locale) {
        try {
            info = URL + "placeid=" + place_id + "&key=" + key;
            return  info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<Marker> parse(String URL) {
		if (URL == null) throw new NullPointerException();

		InputStream stream = null;
		stream = getHttpGETInputStream(URL);
		if (stream == null) throw new NullPointerException();

		String string = null;
		string = getHttpInputString(stream);
		if (string == null) throw new NullPointerException();

		JSONObject json = null;
		try {
			json = new JSONObject(string);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null) throw new NullPointerException();

		return parse(json);
	}

    @Override
    public List<Marker> parse(List<Place> placeList) {
        if (placeList == null) throw new NullPointerException();

        List<Marker> markers = new ArrayList<Marker>();

        for (int i = 0; i < placeList.size(); i++)
        {
            Marker ma = processJSONObject(placeList.get(i));
            if (ma != null) markers.add(ma);
        }

        return markers;
    }

	@Override
	public List<Marker> parse(JSONObject root) {
		if (root == null) throw new NullPointerException();

		JSONObject jo = null;
		List<Marker> markers = new ArrayList<Marker>();

		try {
			if (root.has("result"))//dataArray = root.getJSONArray("result");
			//if (dataArray == null) return markers;
			//int top = Math.min(MAX, dataArray.length());
			//for (int i = 0; i < top; i++) {
				jo = root.getJSONObject("result");

				Marker ma = processJSONObject(jo);
				if (ma != null) markers.add(ma);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return markers;
	}



    private Marker processJSONObject(Place place) {
        if (place == null) throw new NullPointerException();

        Marker ma = null;


        Double lat = place.getlat();
        Double lon = place.getlng();
        String user = place.getUser();
        String name = place.getName();

         ma = new IconMarker(user + ": " + name, lat, lon, 0, Color.RED, icon);
        return ma;
    }

	private Marker processJSONObject(JSONObject jo) {
		if (jo == null) throw new NullPointerException();
        setInfo(jo);
		Marker ma = null;
		try {
			Double lat = null, lon = null;

			if (!jo.isNull("geometry")) {
				JSONObject geo = jo.getJSONObject("geometry");
				JSONObject coordinates = geo.getJSONObject("location");
				lat = Double.parseDouble(coordinates.getString("lat"));
				lon = Double.parseDouble(coordinates.getString("lng"));
			}
			if (lat != null) {
				String user = jo.getString("name");

				ma = new IconMarker(user + ": " + jo.getString("name"), lat, lon, 0, Color.RED, icon);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ma;
	}

    private  void setInfo( JSONObject jo)
    {
        place = jo;
    }

    public JSONObject getInfo()
    {
        return  place;
    }


}