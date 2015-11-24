package com.example.andrew.ar_test.activity;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.andrew.ar_test.common.LowPassFilter;
import com.example.andrew.ar_test.common.Matrix;
import com.example.andrew.ar_test.common.Navigation;
import com.example.andrew.ar_test.common.Orientation;
import com.example.andrew.ar_test.data.ARData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class extends Activity and processes sensor data and location data.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class SensorsActivity extends Activity implements SensorEventListener, LocationListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SensorsActivity";
    private static final AtomicBoolean computing = new AtomicBoolean(false);
    private static final int MIN_TIME = 3000;
    private static final int MIN_DISTANCE = 1;

    private static final float temp[] = new float[9]; // Temporary rotation matrix in Android format
    private static final float rotation[] = new float[9]; // Final rotation matrix in Android format
    private static final float grav[] = new float[3]; // Gravity (a.k.a accelerometer data)
    private static final float mag[] = new float[3]; // Magnetic

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    /*
     * Using Matrix operations instead. This was way too inaccurate, private
     * static final float apr[] = new float[3]; //Azimuth, pitch, roll
     */

    private static final Matrix worldCoord = new Matrix();
    private static final Matrix magneticCompensatedCoord = new Matrix();
    private static final Matrix xAxisRotation = new Matrix();
    private static final Matrix yAxisRotation = new Matrix();
    private static final Matrix mageticNorthCompensation = new Matrix();

    private static GeomagneticField gmf = null;
    private static float smooth[] = new float[3];
    private static SensorManager sensorMgr = null;
    private static List<Sensor> sensors = null;
    private static Sensor sensorGrav = null;
    private static Sensor sensorMag = null;
    private static LocationManager locationMgr = null;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();
        //New location testing for accuracy
        mGoogleApiClient.connect();

        float neg90rads = (float)Math.toRadians(-90);

        // Counter-clockwise rotation at -90 degrees around the x-axis
        // [ 1, 0, 0 ]
        // [ 0, cos, -sin ]
        // [ 0, sin, cos ]
        xAxisRotation.set(1f, 0f,                       0f, 
                          0f, (float)Math.cos(neg90rads), -(float)Math.sin(neg90rads),
                          0f, (float)Math.sin(neg90rads), (float)Math.cos(neg90rads));

        // Counter-clockwise rotation at -90 degrees around the y-axis
        // [ cos,  0,   sin ]
        // [ 0,    1,   0   ]
        // [ -sin, 0,   cos ]
        yAxisRotation.set((float) Math.cos(neg90rads), 0f, (float) Math.sin(neg90rads),
                0f, 1f, 0f,
                -(float) Math.sin(neg90rads), 0f, (float) Math.cos(neg90rads));

        try {
            sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

            sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0)
                sensorGrav = sensors.get(0);

            sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
            if (sensors.size() > 0)
                sensorMag = sensors.get(0);

            sensorMgr.registerListener(this, sensorGrav, SensorManager.SENSOR_DELAY_UI);
            sensorMgr.registerListener(this, sensorMag, SensorManager.SENSOR_DELAY_UI);

            //locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationMgr.requestLocationUpdates(mGoogleApiClient, mCurrentLocation, mLocationRequest );


            try {

                try {
                    Location gps = locationMgr.getLastKnownLocation(mCurrentLocation.getProvider());
                    Location network = locationMgr.getLastKnownLocation(mCurrentLocation.getProvider());
                    if (gps != null) onLocationChanged(gps);
                    else if (network != null) onLocationChanged(network);
                    else onLocationChanged(ARData.hardFix);
                } catch (Exception ex2) {
                    onLocationChanged(ARData.hardFix);
                }

                gmf = new GeomagneticField((float) ARData.getCurrentLocation().getLatitude(), 
                                           (float) ARData.getCurrentLocation().getLongitude(),
                                           (float) ARData.getCurrentLocation().getAltitude(), 
                                           System.currentTimeMillis());

                float dec = (float)Math.toRadians(-gmf.getDeclination());

                synchronized (mageticNorthCompensation) {
                    // Identity matrix
                    // [ 1, 0, 0 ]
                    // [ 0, 1, 0 ]
                    // [ 0, 0, 1 ]
                    mageticNorthCompensation.toIdentity();

                    // Counter-clockwise rotation at negative declination around
                    // the y-axis
                    // note: declination of the horizontal component of the
                    // magnetic field
                    // from true north, in degrees (i.e. positive means the
                    // magnetic
                    // field is rotated east that much from true north).
                    // note2: declination is the difference between true north
                    // and magnetic north
                    // [ cos, 0, sin ]
                    // [ 0, 1, 0 ]
                    // [ -sin, 0, cos ]
                    mageticNorthCompensation.set((float)Math.cos(dec),     0f, (float)Math.sin(dec),
                                                 0f,                     1f, 0f, 
                                                 -(float)Math.sin(dec), 0f, (float)Math.cos(dec));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex1) {
            try {
                if (sensorMgr != null) {
                    sensorMgr.unregisterListener(this, sensorGrav);
                    sensorMgr.unregisterListener(this, sensorMag);
                    sensorMgr = null;
                }
                if (locationMgr != null) {
                    //locationMgr.removeUpdates(this);
                    locationMgr = null;
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        try {
            try {
                sensorMgr.unregisterListener(this, sensorGrav);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sensorMgr.unregisterListener(this, sensorMag);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            sensorMgr = null;

            try {
                //locationMgr.removeUpdates(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            locationMgr = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates
                (mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSensorChanged(SensorEvent evt) {
        if (!computing.compareAndSet(false, true)) return;

        if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        	if (AugmentedReality.useDataSmoothing) { 
	            smooth = LowPassFilter.filter(0.5f, 1.0f, evt.values, grav);
	            grav[0] = smooth[0];
	            grav[1] = smooth[1];
	            grav[2] = smooth[2];
        	} else {
	            grav[0] = evt.values[0];
	            grav[1] = evt.values[1];
	            grav[2] = evt.values[2];
        	}
        	Orientation.calcOrientation(grav);
        	ARData.setDeviceOrientation(Orientation.getDeviceOrientation());
        	ARData.setDeviceOrientationAngle(Orientation.getDeviceAngle());
        } else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
        	if (AugmentedReality.useDataSmoothing) { 
	            smooth = LowPassFilter.filter(2.0f, 4.0f, evt.values, mag);
	            mag[0] = smooth[0];
	            mag[1] = smooth[1];
	            mag[2] = smooth[2];
        	} else {
	            mag[0] = evt.values[0];
	            mag[1] = evt.values[1];
	            mag[2] = evt.values[2];
        	}
        }

        //// Find real world position relative to phone location ////
        // Get rotation matrix given the gravity and geomagnetic matrices
        SensorManager.getRotationMatrix(temp, null, grav, mag);

        SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z, rotation);

        /*
         * Using Matrix operations instead. This was way too inaccurate, 
         * //Get the azimuth, pitch, roll 
         * SensorManager.getOrientation(rotation,apr);
         * float floatAzimuth = (float)Math.toDegrees(apr[0]); 
         * if (floatAzimuth<0) floatAzimuth+=360; 
         * ARData.setAzimuth(floatAzimuth);
         * ARData.setPitch((float)Math.toDegrees(apr[1]));
         * ARData.setRoll((float)Math.toDegrees(apr[2]));
         */

        // Convert from float[9] to Matrix
        worldCoord.set(rotation[0], rotation[1], rotation[2], 
                       rotation[3], rotation[4], rotation[5], 
                       rotation[6], rotation[7], rotation[8]);

        //// Find position relative to magnetic north ////
        // Identity matrix
        // [ 1, 0, 0 ]
        // [ 0, 1, 0 ]
        // [ 0, 0, 1 ]
        magneticCompensatedCoord.toIdentity();

        synchronized (mageticNorthCompensation) {
            // Cross product the matrix with the magnetic north compensation
            magneticCompensatedCoord.prod(mageticNorthCompensation);
        }

        // The compass assumes the screen is parallel to the ground with the screen pointing
        // to the sky, rotate to compensate.
        magneticCompensatedCoord.prod(xAxisRotation);

        // Cross product with the world coordinates to get a mag north compensated coords
        magneticCompensatedCoord.prod(worldCoord);

        // Y axis
        magneticCompensatedCoord.prod(yAxisRotation);

        // Invert the matrix since up-down and left-right are reversed in landscape mode
        magneticCompensatedCoord.invert();

        // Set the rotation matrix (used to translate all object from lat/lon to x/y/z)
        ARData.setRotationMatrix(magneticCompensatedCoord);

        // Update the pitch and bearing using the phone's rotation matrix
        Navigation.calcPitchBearing(magneticCompensatedCoord);
        ARData.setAzimuth(Navigation.getAzimuth());

        computing.set(false);
    }

    /**
     * {@inheritDoc}
     */

    public void onProviderDisabled(String provider) {
        // Ignore
    }

    /**
     * {@inheritDoc}
     */

    public void onProviderEnabled(String provider) {
        // Ignore
    }

    /**
     * {@inheritDoc}
     */

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        ARData.setCurrentLocation(mCurrentLocation);
        gmf = new GeomagneticField((float) ARData.getCurrentLocation().getLatitude(),
                                   (float) ARData.getCurrentLocation().getLongitude(), 
                                   (float) ARData.getCurrentLocation().getAltitude(), System.currentTimeMillis());

        float dec = (float)Math.toRadians(-gmf.getDeclination());

        synchronized (mageticNorthCompensation) {
            mageticNorthCompensation.toIdentity();

            mageticNorthCompensation.set((float)Math.cos(dec), 0f, (float)Math.sin(dec),
                                         0f,                 1f, 0f, 
                                         -(float)Math.sin(dec), 0f, (float)Math.cos(dec));
        }
    }

    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == null) throw new NullPointerException();

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            Log.e(TAG, "Compass data unreliable");
        }
    }
}
