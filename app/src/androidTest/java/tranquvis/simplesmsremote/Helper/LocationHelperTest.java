package tranquvis.simplesmsremote.Helper;

import android.location.Location;
import android.util.Log;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

/**
 * Created by Andreas Kaltenleitner on 03.10.2016.
 */
public class LocationHelperTest extends AppContextTest
{
    @Test
    public void fetchLocation() throws Exception
    {
        Location location = LocationHelper.FetchNewLocation(appContext, 500);
        assert location != null;
        Log.i("UnitTest", location.toString());
    }
}