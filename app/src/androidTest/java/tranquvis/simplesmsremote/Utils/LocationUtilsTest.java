package tranquvis.simplesmsremote.Utils;

import android.location.Location;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.assertTrue;

/**
 * Created by Andreas Kaltenleitner on 03.10.2016.
 */
public class LocationUtilsTest extends AppContextTest
{
    @Test
    public void fetchLocation() throws Exception
    {
        Location location = LocationUtils.GetLocation(appContext, 4000);
        assertTrue(location != null);
    }
}