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
    public static void SetMobileDataState(Context context, boolean enabled) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            SetMobileDataState2(context, enabled);
        else
            SetMobileDataState1(context, enabled);
    }

    public static boolean IsMobileDataEnabled(Context context) throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return IsMobileDataEnabled2(context);
        else
            return IsMobileDataEnabled1(context);
    }

    /**
     * For android versions 2.3 to 4.4
     */
    private static void SetMobileDataState1(Context context, boolean enabled) throws Exception
    {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class cmClass = Class.forName(cm.getClass().getName());
        final Field iCmField = cmClass.getDeclaredField("mService");
        iCmField.setAccessible(true); // Make the method callable
        final Object iCm = iCmField.get(cm);
        final Class iCmClass = Class.forName(iCm.getClass().getName());
        final Method setMobileDataEnabledMethod =
                iCmClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iCm, enabled);
    }

    /**
     * For android versions 2.3 to 4.4
     */
    private static boolean IsMobileDataEnabled1(Context context) throws Exception
    {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = Class.forName(cm.getClass().getName());
        Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
        method.setAccessible(true); // Make the method callable
        return (Boolean) method.invoke(cm);
    }

    /**
     * For android versions 5 and above
     * Needs root access!
     */
    private static void SetMobileDataState2(Context context, boolean enabled) throws Exception
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
    private static boolean IsMobileDataEnabled2(Context context) throws Exception
    {
        TelephonyManager telephonyService =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method getMobileDataEnabledMethod = telephonyService.getClass().
                getDeclaredMethod("getDataEnabled");

        if (getMobileDataEnabledMethod != null)
            return (boolean) getMobileDataEnabledMethod.invoke(telephonyService);

        return false;
    }
}
