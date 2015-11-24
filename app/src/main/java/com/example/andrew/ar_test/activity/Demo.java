package com.example.andrew.ar_test.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.andrew.ar_test.data.ARData;
import com.example.andrew.ar_test.data.LocalDataSource;
import com.example.andrew.ar_test.ui.Marker;
import com.example.andrew.ar_test.widget.VerticalTextView;
import com.jwetherell.augmented_reality.R;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class extends the AugmentedReality and is designed to be an example on
 * how to extends the AugmentedReality class to show multiple data sources.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class Demo extends AugmentedReality {
	
    private static final String TAG = "Demo";
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);

    private static Toast myToast = null;
    private static VerticalTextView text = null;

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
        updateData();
    }

    private void updateData() {
        LocalDataSource localData = new LocalDataSource(this.getResources());
        ARData.addMarkers(localData.getMarkers());
    }
}
