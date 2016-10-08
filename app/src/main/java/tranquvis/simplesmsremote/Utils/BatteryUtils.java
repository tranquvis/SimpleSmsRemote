package tranquvis.simplesmsremote.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by Andreas Kaltenleitner on 30.09.2016.
 */
public class BatteryUtils
{
    private static Intent getBatteryStatusIntent(Context context) throws Exception
    {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = context.registerReceiver(null, filter);
        if(batteryIntent == null)
            throw new Exception("no battery intent found");
        return batteryIntent;
    }

    /**
     * get battery level
     * @param context app context
     * @return level as decimal between 0 and 1
     */
    public static float GetBatteryLevel(Context context) throws Exception
    {
        Intent batteryStatus = getBatteryStatusIntent(context);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if(level == -1)
            throw new Exception("failed to get battery level extra");
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if(scale == -1)
            throw new Exception("failed to get battery scale extra");

        return level / (float)scale;
    }

    /**
     * check if battery is charging
     * @param context app context
     * @return true if battery is charging
     * @throws Exception
     */
    public static boolean IsBatteryCharging(Context context) throws Exception
    {
        Intent batteryStatus = getBatteryStatusIntent(context);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if(status == -1)
            throw new Exception("failed to get battery status extra");

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }
}
