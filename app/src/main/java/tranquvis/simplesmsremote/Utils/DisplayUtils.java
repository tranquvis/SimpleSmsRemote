package tranquvis.simplesmsremote.Utils;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

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

    public static BrightnessMode GetBrightnessMode(Context context) throws Settings.SettingNotFoundException
    {
        int brightnessModeInt = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE);
        return brightnessModeInt == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                ? BrightnessMode.AUTO : BrightnessMode.MANUAL;
    }


    public enum BrightnessMode
    {
        AUTO, MANUAL
    }
}
