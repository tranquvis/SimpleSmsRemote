package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Utils.Device.BatteryUtils;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */
public class BatteryUtilsTest extends AppContextTest
{
    @Test
    public void getBatteryLevel() throws Exception
    {
        float batteryLevel = BatteryUtils.GetBatteryLevel(appContext);
        assertTrue(batteryLevel <= 100 && batteryLevel > 0);
    }

    @Test
    public void isBatteryCharging() throws Exception
    {
        BatteryUtils.IsBatteryCharging(appContext);
    }
}