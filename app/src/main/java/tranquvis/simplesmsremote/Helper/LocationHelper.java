package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.security.Provider;

/**
 * Created by Andreas Kaltenleitner on 03.10.2016.
 */

public class LocationHelper
{
    private static Location lastLocation;

    /**
     * listen on location changed and save the new location to LocationHelper.lastLocation
     * use getLastLocation to get the new location
     * @param context app context
     * @throws SecurityException
     */
    public static void FetchNewLocation(Context context)
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
                Log.e("LocationHelper", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {
                Log.e("LocationHelper", s);
            }

            @Override
            public void onProviderEnabled(String s)
            {
                Log.e("LocationHelper", s);
            }

            @Override
            public void onProviderDisabled(String s)
            {
                Log.e("LocationHelper", s);
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
    public static Location FetchNewLocation(Context context, int maxTime)
    {
        int timeout = 5;
        LocationHelper.FetchNewLocation(context);
        for(int i = 0; timeout * i < maxTime; i++)
        {
            try
            {
                Thread.sleep(timeout);
            } catch (InterruptedException e)
            {
                return null;
            }
            if(getLastLocation() != null)
            {
                return getLastLocation();
            }
        }

        return null;
    }

    public static Location getLastLocation()
    {
        return lastLocation;
    }
}
