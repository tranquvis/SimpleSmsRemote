package tranquvis.simplesmsremote;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TestActivity extends AppCompatActivity implements LocationListener {
    private final String TAG = getClass().getName();

    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);

        startTime = System.currentTimeMillis();
        try {
            locationManager.requestSingleUpdate(criteria, this, null);
        } catch (SecurityException e) {
            Log.e(TAG, "permission not granted");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, location.toString());
        Log.i(TAG, String.format("ellapsed time: %d", System.currentTimeMillis() - startTime));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.w(TAG, s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.w(TAG, s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.w(TAG, s);
    }
}
