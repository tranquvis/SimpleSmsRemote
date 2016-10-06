package tranquvis.simplesmsremote.Helper;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */
public class BatteryHelperTest extends AppContextTest
{
    @Test
    public void getBatteryLevel() throws Exception
    {
        float batteryLevel = BatteryHelper.GetBatteryLevel(appContext);
        assertTrue(batteryLevel <= 100 && batteryLevel > 0);
    }

    @Test
    public void isBatteryCharging() throws Exception
    {
        BatteryHelper.IsBatteryCharging(appContext);
    }
}