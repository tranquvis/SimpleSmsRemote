package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class HotspotHelper
{
    /**
     * check hotspot state
     * @param context app context
     * @return if hotspot is enabled
     * @throws Exception
     */
    public static boolean IsHotspotEnabled(Context context) throws Exception
    {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
        method.setAccessible(true);
        return (Boolean) method.invoke(wifimanager);
    }

    /**
     * set state of hotspot to enabled or disabled
     * @param context app context
     * @param enabled hotspot state
     */
    public static void SetHotspotState(Context context, boolean enabled) throws Exception {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(enabled)
            wifimanager.setWifiEnabled(false);
        Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
        method.invoke(wifimanager, null, enabled);
    }
}
