package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;

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
        wifimanager.setWifiEnabled(true);

        WifiHelper.SetHotspotState(appContext, true);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiHelper.IsHotspotEnabled(appContext);
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetHotspotStateDisabled() throws Exception {
        WifiHelper.SetHotspotState(appContext, false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiHelper.IsHotspotEnabled(appContext);
            }
        }, false, 10, 10000);
        assertFalse(enabled);
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

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiHelper.IsWifiEnabled(appContext);
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetWifiStateDisabled() throws Exception
    {
        WifiHelper.SetWifiState(appContext, false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiHelper.IsWifiEnabled(appContext);
            }
        }, false, 10, 10000);
        assertFalse(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testIsWifiEnabled() throws Exception
    {
        WifiHelper.IsWifiEnabled(appContext);
    }

}