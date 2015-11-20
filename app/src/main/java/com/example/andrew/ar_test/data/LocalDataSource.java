package com.example.andrew.ar_test.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.andrew.ar_test.ui.IconMarker;
import com.example.andrew.ar_test.ui.Marker;
import com.jwetherell.augmented_reality.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class should be used as a example local data source. It is an example of
 * how to add data programatically. You can add data either programatically,
 * SQLite or through any other source.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class LocalDataSource extends DataSource {

    private List<Marker> cachedMarkers = new ArrayList<Marker>();
    private static Bitmap icon = null;

    public LocalDataSource(Resources res) {
        if (res == null) throw new NullPointerException();

        createIcon(res);
    }

    protected void createIcon(Resources res) {
        if (res == null) throw new NullPointerException();

        icon = BitmapFactory.decodeResource(res, R.drawable.oubear);
    }

    public List<Marker> getMarkers() {
        Marker SFH = new IconMarker("South Foundation Hall", 42.673604, -83.217716, 0, Color.YELLOW, icon);
        cachedMarkers.add(SFH);

        Marker EC = new IconMarker("Engineering Center", 42.671992, -83.214988, 0, Color.YELLOW, icon);
        cachedMarkers.add(EC);

        /*
         * Marker lon = new IconMarker(
         * "I am a really really long string which should wrap a number of times on the screen."
         * , 39.95335, -74.9223445, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon); Marker lon2 = new IconMarker(
         * "2: I am a really really long string which should wrap a number of times on the screen."
         * , 39.95334, -74.9223446, 0, Color.MAGENTA, icon);
         * cachedMarkers.add(lon2);
         */

        /*
         * float max = 10; for (float i=0; i<max; i++) { Marker marker = null;
         * float decimal = i/max; if (i%2==0) marker = new Marker("Test-"+i,
         * 39.99, -75.33+decimal, 0, Color.LTGRAY); marker = new
         * IconMarker("Test-"+i, 39.99+decimal, -75.33, 0, Color.LTGRAY, icon);
         * cachedMarkers.add(marker); }
         */

        return cachedMarkers;
    }

    public JSONObject getInfo()
    {
        return null;
    }
}
