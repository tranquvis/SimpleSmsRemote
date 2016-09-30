package tranquvis.simplesmsremote.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by Andreas Kaltenleitner on 30.09.2016.
 */
public class BatteryHelper
{
    private static Intent getBatteryStatusIntent(Context context)
    {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return context.registerReceiver(null, filter);
    }

    /**
     * get battery level from 0 to 1
     * @param context
     * @return level as decimal between 0 and 1
     */
    public static float GetBatteryLevel(Context context)
    {
        Intent batteryStatus = getBatteryStatusIntent(context);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level / (float)scale;
    }
}
