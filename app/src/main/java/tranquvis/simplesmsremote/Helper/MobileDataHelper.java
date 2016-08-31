package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Andreas Kaltenleitner on 26.08.2016.
 */
public class MobileDataHelper
{
    public static void setMobileDataState(Context context, boolean enabled) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setMobileDataStateNew(context, enabled);
        else
            setMobileDataStateOld(context, enabled);
    }

    private static void setMobileDataStateOld(Context context, boolean enabled) throws Exception
    {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

    private static void setMobileDataStateNew(Context context, boolean enabled) throws Exception
    {
        //Only working on rooted devices");
        TelephonyManager telephonyService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method setMobileDataEnabledMethod = telephonyService.getClass()
                .getDeclaredMethod("setDataEnabled", boolean.class);

        if (setMobileDataEnabledMethod != null)
            setMobileDataEnabledMethod.invoke(telephonyService, enabled);
    }

    private static boolean getMobileDataStateNew(Context context) throws Exception
    {
        //Only working on rooted devices
        TelephonyManager telephonyService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method getMobileDataEnabledMethod = telephonyService.getClass().
                getDeclaredMethod("getDataEnabled");

        if (getMobileDataEnabledMethod != null)
            return (boolean) getMobileDataEnabledMethod.invoke(telephonyService);

        return false;
    }
}
