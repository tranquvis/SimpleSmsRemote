package tranquvis.simplesmsremote.Utils.Device;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Created by Andreas Kaltenleitner on 14.10.2016.
 */

public class DisplayUtils
{
    /**
     * Set brightness of display. (brightness mode is set to manual
     * @param context app context
     * @param brightnessPercentage as percentage between 0 and 100
     * @throws Exception
     */
    public static void SetBrightness(Context context, float brightnessPercentage) throws Exception
    {
        if(brightnessPercentage < 0 || brightnessPercentage > 100)
            throw new IllegalArgumentException("brightnessPercentage must be a number between 0 and 100");

        int brightness = Math.round(brightnessPercentage / 100f * 255f);
        SetBrightnessMode(context, BrightnessMode.MANUAL);
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                brightness);
    }

    /**
     * Get brightness of display
     * @param context app context
     * @return brightness as percentage between 0 and 100
     * @throws Exception
     */
    public static float GetBrightness(Context context) throws Exception
    {
        return (float)Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS) / 255f * 100f;
    }

    /**
     * Get current brightness mode of device
     * @param context app context
     * @param brightnessMode see {@code BrightnessMode}
     * @throws Exception
     */
    public static void SetBrightnessMode(Context context, BrightnessMode brightnessMode)
            throws Exception
    {
        int brightnessModeInt = brightnessMode == BrightnessMode.AUTO
                ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                brightnessModeInt);
    }

    /**
     * Get current brightness mode of device
     * @param context app context
     * @return brightness mode (see {@code BrightnessMode})
     * @throws Exception
     */
    public static BrightnessMode GetBrightnessMode(Context context) throws Exception
    {
        int brightnessModeInt = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE);
        return brightnessModeInt == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                ? BrightnessMode.AUTO : BrightnessMode.MANUAL;
    }

    /**
     * Set screen off timeout
     * @param context app context
     * @param timeout timeout to set in milliseconds
     */
    public static void SetScreenOffTimeout(Context context, int timeout)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                timeout);
    }

    /**
     * Get screen off timeout
     * @param context app context
     * @return timeout in milliseconds
     * @throws Exception
     */
    public static int GetScreenOffTimeout(Context context) throws Exception
    {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT);
    }

    /**
     * Turn screen off. <br/>
     * This is a workaround by setting screen off timeout to a small value
     * and resetting it after the screen is off. </br>
     * The execution may take up to 4 seconds, because it is required to wait until screen is
     * turned off in order to preserve the current screen off timeout.
     * @param context app context
     */
    public static void TurnScreenOff(Context context) throws Exception
    {
        // workaround:
        // The screen off timeout is set to 0 so it will turn off in a few milliseconds.
        // After that the screen off timeout is set to its previous value.
        int previousTimeout = GetScreenOffTimeout(context);

        SetScreenOffTimeout(context, 0);

        //wait until screen is turned off
        final int maxWaitTime = 10000; //milliseconds
        final int checkingTimeout = 200;

        long startTime = System.currentTimeMillis();
        boolean success = true;
        while(IsScreenOn(context)
                && (success = (System.currentTimeMillis() - startTime) < maxWaitTime))
        {
            Thread.sleep(checkingTimeout);
        }

        SetScreenOffTimeout(context, previousTimeout);

        if(!success)
            throw new Exception("Failed to turn screen off. The screen was on anyhow after trying to turn it off.");
    }

    /**
     * check if screen is on
     * @param context app context
     * @return true if the screen is on
     */
    public static boolean IsScreenOn(Context context)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= 20)
        {
            return pm.isInteractive();
        }
        else
        {
            return pm.isScreenOn();
        }
    }

    public enum BrightnessMode
    {
        AUTO, MANUAL
    }
}
