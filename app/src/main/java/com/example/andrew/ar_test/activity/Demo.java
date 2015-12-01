package com.example.andrew.ar_test.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.ar_test.data.ARData;
import com.example.andrew.ar_test.data.LocalDataSource;
import com.example.andrew.ar_test.ui.Marker;
import com.example.andrew.ar_test.widget.VerticalTextView;
import com.jwetherell.augmented_reality.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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

    private static Toast myToast = null;
    private static VerticalTextView text = null;

    private DrawerLayout drawerLayout;
    private ListView listView;

    private LocalDataSource localData;

    @SuppressWarnings("deprecation")
    private android.support.v4.app.ActionBarDrawerToggle drawerListener;
    private MyAdapter myAdapter;

    public static String info;

    private int radartype = 1; // For all locations
    private String markerName = "";

    /*Search listview variables */
    // List view
    private ListView lv;


    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    /*End Search listview variables */
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
        localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.filterType(radartype));

        drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);

        listView =(ListView) findViewById(R.id.drawerList);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(this);

        drawerListener = new android.support.v4.app.ActionBarDrawerToggle(this, drawerLayout, R.mipmap.ic_action_drawer_icon, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                showRadar = !showRadar;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                showRadar = !showRadar;
            }
        };
        drawerLayout.setDrawerListener(drawerListener);


    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        selectItem(position);

        switch (position){
            case 0:
                final CharSequence places[] = new CharSequence[] {"Anibal House", "Ann V. Nicholson Student Apartments","Athletics Center O'Rena",
                        "Bear Lake",
                        "Belgian Barn",
                        "Buildings and Grounds Maintenance",
                        "Carriage House",
                        "Central Heating Plant",
                        "Danny's Cabin",
                        "Dodge Hall",
                        "Electrical Substation",
                        "Elliot Tower",
                        "Elliott Hall",
                        "Engineering Center",
                        "Facilities Management",
                        "Fitzgerald House",
                        "George T. Matthews Apartments",
                        "Golf Course Clubhouse and Pro Shop",
                        "Graham Health Center",
                        "Grizzly Oaks Disc Golf Course",
                        "Hamlin Hall",
                        "Hannah Hall",
                        "Hill House",
                        "Human Health Building",
                        "John Dodge House",
                        "Kettering Magnetics Lab",
                        "Kresge Library",
                        "Mathematics and Science Center",
                        "Meadow Brook Greenhouse",
                        "Meadow Brook Hall & Gardens",
                        "Meadow Brook Music Festival",
                        "Meadow Brook Theatre",
                        "North Foundation Hall",
                        "O'Dowd Hall",
                        "Oak View Hall",
                        "Oakland Baseball Field",
                        "Oakland Center",
                        "Observatory",
                        "Pawley Hall",
                        "Pioneer Field (Lower)",
                        "Police and Support Services Building",
                        "Pryale House",
                        "Shotwell-Gustafson Pavilion",
                        "South Foundation Hall",
                        "Storage Facility",
                        "Sunset Terrace",
                        "Rec Center",
                        "Recreation and Athletic Complex",
                        "Van Wagoner House",
                        "Vandenberg Hall",
                        "Varner Hall",
                }
                        ;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pick a location");
                builder.setItems(places, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        markerName = (String) places[which];
                        showOneMarker(markerName);

                    }
                });
                builder.show();
                break;
            case 1:
                radartype = 19;
                break;
            case 2:
                radartype = 13;
                break;
            case 3:
                radartype = 11;
                break;
            case 4:
                radartype = 3;
                break;
            case 5:
                radartype = 17;
                break;
            case 6:
                radartype = 2;
                break;
            case 7:
                radartype = 7;
                break;
            case 8:
                radartype = 5;
                break;
            case 9:
                radartype = 23;
                break;
            case 10:
                radartype = 1;
                break;
            default:
                Log.e("Filter out of range: ", "check invoking method.");
        }

        ARData.addMarkers(localData.filterType(radartype));
        drawerLayout.closeDrawer(listView);
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
        showOneMarker(marker.getName());


    }

    protected void showOneMarker(String name) {
        markerName = name;
        ARData.addMarkers(localData.filterByName(markerName));
        radartype=100;
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

    private void updateData() {
        LocalDataSource localData = new LocalDataSource(this.getResources());

        if (radartype == 100)
        {
            ARData.addMarkers(localData.filterByName(markerName));
        }

        else {
            ARData.addMarkers(localData.filterType(radartype));
        }
    }
}

class MyAdapter extends BaseAdapter
{
    private Context context;
    String[] categorySites;
    int[] images = {R.mipmap.ic_search,R.mipmap.ic_library, R.mipmap.ic_utilities,R.mipmap.ic_sports, R.mipmap.ic_sight_seeing,
            R.mipmap.ic_launcher, R.mipmap.ic_education, R.mipmap.ic_lab, R.mipmap.ic_houses,R.mipmap.ic_entertainment,R.mipmap.ic_action_campus};

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
