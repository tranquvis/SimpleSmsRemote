package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.ExecSequentially;

import static org.junit.Assert.*;

/**
 * Created by Andi on 04.10.2016.
 */
public class WifiHelperTest extends AppContextTest
{
    @Test
    @ExecSequentially("wifi")
    public void testSetHotspotStateEnabled() throws Exception {
        //enabled wifi must be turned of. So turn on wifi ...
        WifiManager wifimanager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);

        WifiHelper.SetHotspotState(appContext, true);
        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(WifiHelper.IsHotspotEnabled(appContext))
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetHotspotStateDisabled() throws Exception {
        WifiHelper.SetHotspotState(appContext, false);
        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(!WifiHelper.IsHotspotEnabled(appContext))
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    @ExecSequentially("wifi")
    public void testIsHotspotEnabled() throws Exception
    {
        WifiHelper.IsHotspotEnabled(appContext);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetWifiStateEnabled() throws Exception
    {
        WifiHelper.SetWifiState(appContext, true);
        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(WifiHelper.IsWifiEnabled(appContext))
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetWifiStateDisabled() throws Exception
    {
        WifiHelper.SetWifiState(appContext, false);
        boolean success = false;
        for(int i  = 0; i < 100; i++)
        {
            if(!WifiHelper.IsWifiEnabled(appContext))
            {
                success = true;
                break;
            }
        }

        assertTrue(success);
    }

    @Test
    @ExecSequentially("wifi")
    public void testIsWifiEnabled() throws Exception
    {
        WifiHelper.IsWifiEnabled(appContext);
    }
}