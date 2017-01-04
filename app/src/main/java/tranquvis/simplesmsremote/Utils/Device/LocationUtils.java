package tranquvis.simplesmsremote.Utils.Device;

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

public class LocationUtils {
    private static final String TAG = LocationUtils.class.getName();
    private static Location lastLocation;

    /**
     * Get the current location. <br/>
     * If no previously saved location was found a new is requested.
     *
     * @param context             app context
     * @param maxTimeMilliseconds max. time, which the location request may take
     * @return location
     * @throws SecurityException
     * @// TODO: use new google play services api (see https://developer.android.com/training/location/index.html)
     */
    public static Location GetLocation(Context context, int maxTimeMilliseconds) throws SecurityException {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        // Fetch cached gps and network location
        Location lastGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastNetworkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location bestCachedLocation = null;

        // Set last location from cached gps location
        if(lastGpsLocation != null)
        {
            bestCachedLocation = lastGpsLocation;
        }
        // Set last location from cached network location, if better than gps location.
        if(lastNetworkLocation != null && (lastGpsLocation == null
                || isBetterLocation(lastNetworkLocation, bestCachedLocation)))
        {
            bestCachedLocation = lastNetworkLocation;
        }

        // Try to request new location, if no cached location was found
        // or the cached location is older than 30s.
        if(bestCachedLocation == null || (System.currentTimeMillis() - bestCachedLocation.getTime() > 30E+3))
        {
            lastLocation = RequestNewLocation(context, maxTimeMilliseconds);
        }

        if(bestCachedLocation != null && (lastLocation == null
                || isBetterLocation(bestCachedLocation, lastLocation)))
            lastLocation = bestCachedLocation;

        return lastLocation;
    }

    /**
     * listen on location changed and save the new location to LocationUtils.lastLocation
     *
     * @param context app context
     * @throws SecurityException
     */
    private static void RequestNewLocation(Context context)
            throws SecurityException {
        lastLocation = null;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                Log.i(TAG, location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.i(TAG, "status changed: " + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.i(TAG, "provider enabled: " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e(TAG, "provider disabled: " + s);
            }
        };

        locationManager.requestSingleUpdate(criteria, locationListener, Looper.getMainLooper());
    }

    /**
     * listen until location changed
     *
     * @param context app context
     * @param maxTime max waiting time in milliseconds
     * @return new location or null if failed
     */
    private static Location RequestNewLocation(Context context, int maxTime) {
        int timeout = 5;
        LocationUtils.RequestNewLocation(context);
        for (int i = 0; timeout * (i + 1) <= maxTime; i++) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                return null;
            }
            if (lastLocation != null) {
                return lastLocation;
            }
        }

        return null;
    }


    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    private static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
