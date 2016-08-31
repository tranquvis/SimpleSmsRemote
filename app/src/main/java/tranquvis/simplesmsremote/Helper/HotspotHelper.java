package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class HotspotHelper
{
    public static boolean isHotspotEnabled(Context context) throws Exception
    {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
        method.setAccessible(true);
        return (Boolean) method.invoke(wifimanager);
    }

    /**
     * set state of hotspot to enabled to disabled
     * @param context
     * @param state true if hotspot should be enabled, false if disabled
     */
    public static void setHotspotState(Context context, boolean state) throws Exception {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(state);

        Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        method.invoke(wifimanager, null, state);
    }
}
