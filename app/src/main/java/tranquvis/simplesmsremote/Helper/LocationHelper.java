package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * Created by Andreas Kaltenleitner on 03.10.2016.
 */

public class LocationHelper
{
    private static final String TAG = LocationHelper.class.getName();
    private static Location lastLocation;

    /**
     * Get the current location. <br/>
     * If no previously saved location was found a new is requested.
     * @param context app context
     * @param maxTimeMilliseconds max. time, which the location request may take
     * @return location
     * @throws SecurityException
     * @// TODO: use new google play services api (see https://developer.android.com/training/location/index.html)
     */
    public static Location GetLocation(Context context, int maxTimeMilliseconds) throws SecurityException
    {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(gpsLocation != null && (networkLocation == null
                || gpsLocation.getTime() > networkLocation.getTime()))
        {
            lastLocation = gpsLocation;
        }
        else if(networkLocation != null)
        {
            lastLocation = networkLocation;
        }
        else
        {
            lastLocation = RequestNewLocation(context, maxTimeMilliseconds);
        }

        return lastLocation;
    }

    /**
     * listen on location changed and save the new location to LocationHelper.lastLocation
     * @param context app context
     * @throws SecurityException
     */
    private static void RequestNewLocation(Context context)
            throws SecurityException
    {
        lastLocation = null;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                lastLocation = location;
                Log.i(TAG, location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {
                Log.e(TAG, s);
            }

            @Override
            public void onProviderEnabled(String s)
            {
                Log.e(TAG, s);
            }

            @Override
            public void onProviderDisabled(String s)
            {
                Log.e(TAG, s);
            }
        };

        locationManager.requestSingleUpdate(criteria, locationListener, Looper.getMainLooper());
    }

    /**
     * listen until location changed
     * @param context app context
     * @param maxTime max waiting time in milliseconds
     * @return new location or null if failed
     */
    private static Location RequestNewLocation(Context context, int maxTime)
    {
        int timeout = 5;
        LocationHelper.RequestNewLocation(context);
        for(int i = 0; timeout * (i+1) <= maxTime; i++)
        {
            try
            {
                Thread.sleep(timeout);
            } catch (InterruptedException e)
            {
                return null;
            }
            if(lastLocation != null)
            {
                return lastLocation;
            }
        }

        return null;
    }
}
