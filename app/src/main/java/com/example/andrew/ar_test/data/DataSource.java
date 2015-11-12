package com.example.andrew.ar_test.data;

import com.example.andrew.ar_test.ui.Marker;

import org.json.JSONObject;

import java.util.List;

/**
 * This abstract class should be extended for new data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public abstract class DataSource {

    public abstract List<Marker> getMarkers();

    public abstract JSONObject getInfo();
}
