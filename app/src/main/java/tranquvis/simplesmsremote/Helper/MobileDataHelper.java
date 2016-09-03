package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Andreas Kaltenleitner on 26.08.2016.
 */
public class MobileDataHelper
{
    public static void setMobileDataState(Context context, boolean enabled) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setMobileDataState2(context, enabled);
        else
            setMobileDataState1(context, enabled);
    }

    public static boolean getMobileDataState(Context context) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getMobileDataState2(context);
        else
            throw new NotImplementedException("only for android version 5 and above with root access");
    }

    /**
     * For android versions 2.3 to 4.4
     */
    private static void setMobileDataState1(Context context, boolean enabled) throws Exception
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

    /**
     * For android versions 5 and above
     * Needs root access!
     */
    private static void setMobileDataState2(Context context, boolean enabled) throws Exception
    {
        throw new NotImplementedException("not tested so far");
        /*
        TelephonyManager telephonyService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method setMobileDataEnabledMethod = telephonyService.getClass()
                .getDeclaredMethod("setDataEnabled", boolean.class);

        if (setMobileDataEnabledMethod != null)
            setMobileDataEnabledMethod.invoke(telephonyService, enabled);
            */
    }

    /**
     * For android versions 5 and above
     * Needs root access!
     */
    private static boolean getMobileDataState2(Context context) throws Exception
    {
        throw new NotImplementedException("not tested so far");
        /*
        TelephonyManager telephonyService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method getMobileDataEnabledMethod = telephonyService.getClass().
                getDeclaredMethod("getDataEnabled");

        if (getMobileDataEnabledMethod != null)
            return (boolean) getMobileDataEnabledMethod.invoke(telephonyService);

        return false;
        */
    }
}
