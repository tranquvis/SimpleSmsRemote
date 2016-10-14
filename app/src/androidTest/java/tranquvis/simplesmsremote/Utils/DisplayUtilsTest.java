package tranquvis.simplesmsremote.Utils;

import android.provider.Settings;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 14.10.2016.
 */
public class DisplayUtilsTest extends AppContextTest
{
    @Test
    public void setBrightness() throws Exception
    {
        DisplayUtils.SetBrightness(appContext, 50);
        float actualBrightness = (int)DisplayUtils.GetBrightness(appContext);
        assertTrue(Math.round(actualBrightness) == 50);
    }
    @Test
    public void getBrightness() throws Exception
    {
        float brightness = DisplayUtils.GetBrightness(appContext);
        assertTrue(brightness >= 0 && brightness <= 100);
    }

    @Test
    public void setBrightnessMode() throws Exception
    {
        DisplayUtils.SetBrightnessMode(appContext, DisplayUtils.BrightnessMode.AUTO);
        assertTrue(DisplayUtils.GetBrightnessMode(appContext) == DisplayUtils.BrightnessMode.AUTO);
    }
    @Test
    public void getBrightnessMode() throws Exception
    {
        DisplayUtils.GetBrightnessMode(appContext);
    }

    @Test
    public void setScreenTimeout() throws Exception
    {
        Settings.System.putInt(appContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
        Thread.sleep(1000);
        Settings.System.putInt(appContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 30 * 60 * 1000);
    }
}