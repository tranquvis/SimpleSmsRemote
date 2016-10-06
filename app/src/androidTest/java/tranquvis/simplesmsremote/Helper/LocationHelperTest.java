package tranquvis.simplesmsremote.Helper;

import android.location.Location;
import android.util.Log;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.assertTrue;

/**
 * Created by Andreas Kaltenleitner on 03.10.2016.
 */
public class LocationHelperTest extends AppContextTest
{
    @Test
    public void fetchLocation() throws Exception
    {
        Location location = LocationHelper.GetLocation(appContext, 4000);
        assertTrue(location != null);
    }
}