package com.example.andrew.ar_test.activity;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.ar_test.data.ARData;
import com.example.andrew.ar_test.data.DatabaseHandler;
import com.example.andrew.ar_test.data.GooglePlacesDataSource;
import com.example.andrew.ar_test.data.LocalDataSource;
import com.example.andrew.ar_test.data.NetworkDataSource;
import com.example.andrew.ar_test.data.Place;
import com.example.andrew.ar_test.ui.Marker;
import com.example.andrew.ar_test.widget.VerticalTextView;
import com.jwetherell.augmented_reality.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class extends the AugmentedReality and is designed to be an example on
 * how to extends the AugmentedReality class to show multiple data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class Demo extends AugmentedReality implements AdapterView.OnItemClickListener {
	
    private static final String TAG = "Demo";
    private static final String locale = Locale.getDefault().getLanguage();
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
    private static final ThreadPoolExecutor exeService =
            new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, queue);
    private static final Map<String, NetworkDataSource> sources =
            new ConcurrentHashMap<String, NetworkDataSource>();

    private static Toast myToast = null;
    private static VerticalTextView text = null;

    private DrawerLayout drawerLayout;
    private ListView listView;

    @SuppressWarnings("deprecation")
    private android.support.v4.app.ActionBarDrawerToggle drawerListener;
    private MyAdapter myAdapter;



    DatabaseHandler db = new DatabaseHandler(this);
    public static String info;
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings( "deprecation" )
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create toast
        myToast = new Toast(getApplicationContext());
        myToast.setGravity(Gravity.CENTER, 0, 0);
        // Creating our custom text view, and setting text/rotation
        text = new VerticalTextView(getApplicationContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        text.setLayoutParams(params);
        text.setBackgroundResource(android.R.drawable.toast_frame);
        text.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Small);
        text.setShadowLayer(2.75f, 0f, 0f, Color.parseColor("#BB000000"));
        myToast.setView(text);
        // Setting duration and displaying the toast
        myToast.setDuration(Toast.LENGTH_SHORT);

        // Local
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());

        NetworkDataSource googlePlaces = new GooglePlacesDataSource(this.getResources());
        sources.put("googlePlaces", googlePlaces);

        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);


        listView =(ListView) findViewById(R.id.drawerList);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(this);
        drawerListener = new android.support.v4.app.ActionBarDrawerToggle(this, drawerLayout, R.mipmap.ic_action_drawer_icon, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(Demo.this, " Drawer Opened",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(Demo.this, " Drawer Closed",
                Toast.LENGTH_SHORT).show();
            }
        };
        drawerLayout.setDrawerListener(drawerListener);


    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        selectItem(position);
    }

    private void selectItem(int position)
    {
        listView.setItemChecked(position, true);

    }

    public void setTitle(String title) //Shows selected title in action bar.
    {
        //getSupportActionBar().setTitle(title);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        Location last = ARData.getCurrentLocation();
        //initialUpdateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected() item=" + item);
        switch (item.getItemId()) {
            case R.id.showRadar:
                showRadar = !showRadar;
                item.setTitle(((showRadar) ? "Hide" : "Show") + " Radar");
                break;
            case R.id.showZoomBar:
                showZoomBar = !showZoomBar;
                item.setTitle(((showZoomBar) ? "Hide" : "Show") + " Zoom Bar");
                zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE : LinearLayout.GONE);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        updateData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void markerTouched(Marker marker) {
        text.setText(marker.getName());
        myToast.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateDataOnZoom() {
        super.updateDataOnZoom();
        Location last = ARData.getCurrentLocation();
        updateData();
    }

    private void initialUpdateData(final double lat, final double lon, final double alt) {
        if(db.getPlacesCount()==0) {

            try {
                exeService.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (NetworkDataSource source : sources.values())
                            saveToDB(source, lat, lon, alt);
                    }
                });
            } catch (RejectedExecutionException rej) {
                Log.w(TAG, "Not running new download Runnable, queue is full.");
            } catch (Exception e) {
                Log.e(TAG, "Exception running download Runnable.", e);
            }
        }
        else {
            Toast.makeText(this, "Lock and load bithc", Toast.LENGTH_LONG);
        }
    }

    private void updateDataOriginal() {
        try {
            exeService.execute(new Runnable() {
                @Override
                public void run() {
                    for (NetworkDataSource source : sources.values())
                        download(source);
                }
            });
        } catch (RejectedExecutionException rej) {
            Log.w(TAG, "Not running new download Runnable, queue is full.");
        } catch (Exception e) {
            Log.e(TAG, "Exception running download Runnable.", e);
        }
    }

    private void updateData() {
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());
    }

    private boolean download(NetworkDataSource source) {
        if (source == null) return false;

       /* String url = null;
        try {
            url = source.createRequestURL(lat, lon, alt, ARData.getRadius(), locale);
        } catch (NullPointerException e) {
            return false;
        }*/

        List<Marker> markers = null;
        ArrayList<Place> placeList;
        try {
            placeList = (ArrayList)db.getAllPlaces();

            for(int i= 0 ; i<placeList.size(); i++){
                markers = source.parse(placeList.get(i));
                ARData.addMarkers(markers);
            }
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    private boolean saveToDB(NetworkDataSource source, double lat, double lon, double alt) {
        if (source == null) return false;

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Human Health Building", "ChIJUcxP8pHqJIgRZ1BhRCCmw4k");
        map.put("Kresge Library", "ChIJ1RzjhWLqJIgRfGY78wBl-Mk");
        map.put("North Foundation Hall", "ChIJQ2tJ24_qJIgR3bajTTz9ktY");
        map.put("Graham Health Center", "ChIJUdsCGY7qJIgR3fN0KGMZGCM");
        map.put("Meadow Brook Theatre", "ChIJ--0PMo7qJIgRnu4MUriTErQ");
        map.put("Oakland Center", "ChIJhfKkvo_qJIgRUO1NGq8EzpQ");
        map.put("South Foundation Hall", "ChIJ28H074_qJIgRKa9H-XBjNQw");
        map.put("O'Dowd Hall", "ChIJbx5qCY_qJIgRkUYydXg_y_c");
        map.put("Engineering Center", "ChIJR2vhkIXqJIgRKuKNq_8yXWs");
        map.put("Hannah Hall", "ChIJ0_zqN4XqJIgRDrkUi23ayZI");
        map.put("Dodge Hall", "ChIJWVE1E4XqJIgRQc9LBgl_NP8");
        map.put("Police and Support Services Building", "ChIJsdc6xZrqJIgReA9MsWXcc6s");
        map.put("Belgian Barn", "ChIJsZTY2ZrqJIgREOWfM0AYpgg");
        map.put("Elliott Hall", "ChIJGXbs5oXqJIgRE-Zjms68Ogw");
        map.put("Pawley Hall", "ChIJ4ynfSYbqJIgRUcbCnbHy3B8");
        map.put("Athletics Center O'Rena", "ChIJwa1XNY_qJIgRWYmP0fx7gpA");
        map.put("Central Heating Plant", "ChIJuWCS1YjqJIgR0RImzb4LgUI");
        map.put("Varner Hall", "ChIJy9WBDIbqJIgRxfOuhiVLAxA");

        Iterator it = map.entrySet().iterator();
        List<Marker> markers = null;

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            //place_id = pair.getValue().toString();


            String url = null;
            try {
                url = source.createRequestURL2(pair.getValue().toString(), locale);
            } catch (NullPointerException e) {
                return false;
            }

            try {
                markers = source.parse(url);
            } catch (NullPointerException e) {
                return false;
            }

            //ARData.addMarkers(markers);
            db.addPlace(new Place(source.getInfo()));
            it.remove();
        }
        return true;
    }
}

class MyAdapter extends BaseAdapter
{
    private Context context;
    String[] categorySites;
    int[] images = {R.mipmap.ic_search,R.mipmap.ic_library, R.mipmap.ic_utilities,R.mipmap.ic_sports, R.mipmap.ic_sight_seeing,
            R.mipmap.ic_admin, R.mipmap.ic_education, R.mipmap.ic_lab, R.mipmap.ic_houses,R.mipmap.ic_entertainment};

    public MyAdapter(Context context)
    {
        this.context=context;
        categorySites=context.getResources().getStringArray(R.array.categories);
    }
    @Override
    public int getCount()
    {
        return categorySites.length;
    }

    @Override
    public Object getItem(int position)
    {
        return categorySites[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View row = null;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, parent, false);
        }
        else
        {
            row=convertView;
        }
        TextView titleTextView = (TextView) row.findViewById(R.id.textView1);
        ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView1);
        titleTextView.setText(categorySites[position]);
        titleImageView.setImageResource(images[position]);

        return row;
    }

}
