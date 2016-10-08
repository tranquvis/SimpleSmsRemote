package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;

import static org.junit.Assert.*;

/**
 * Created by Andi on 04.10.2016.
 */
public class WifiUtilsTest extends AppContextTest
{
    @Test
    @ExecSequentially("wifi")
    public void testSetHotspotStateEnabled() throws Exception {
        //enabled wifi must be turned of. So turn on wifi ...
        WifiManager wifimanager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);

        WifiUtils.SetHotspotState(appContext, true);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiUtils.IsHotspotEnabled(appContext);
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetHotspotStateDisabled() throws Exception {
        WifiUtils.SetHotspotState(appContext, false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiUtils.IsHotspotEnabled(appContext);
            }
        }, false, 10, 10000);
        assertFalse(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testIsHotspotEnabled() throws Exception
    {
        WifiUtils.IsHotspotEnabled(appContext);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetWifiStateEnabled() throws Exception
    {
        WifiUtils.SetWifiState(appContext, true);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiUtils.IsWifiEnabled(appContext);
            }
        }, true, 10, 10000);
        assertTrue(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testSetWifiStateDisabled() throws Exception
    {
        WifiUtils.SetWifiState(appContext, false);

        boolean enabled = TryUntil(new TryMethod<Boolean>() {
            @Override
            public Boolean run() throws Exception {
                return WifiUtils.IsWifiEnabled(appContext);
            }
        }, false, 10, 10000);
        assertFalse(enabled);
    }

    @Test
    @ExecSequentially("wifi")
    public void testIsWifiEnabled() throws Exception
    {
        WifiUtils.IsWifiEnabled(appContext);
    }

}