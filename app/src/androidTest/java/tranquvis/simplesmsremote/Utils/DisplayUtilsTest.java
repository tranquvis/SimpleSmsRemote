package tranquvis.simplesmsremote.Utils;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.Aspects.ExecSequentially.ExecSequentially;
import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;

import static org.junit.Assert.assertTrue;

/**
 * Created by Andreas Kaltenleitner on 14.10.2016.
 */
public class DisplayUtilsTest extends AppContextTest {
    @Test
    @ExecSequentially("display-brightness")
    public void setBrightness() throws Exception {
        DisplayUtils.SetBrightness(appContext, 50);
        float actualBrightness = (int) DisplayUtils.GetBrightness(appContext);
        assertTrue(Math.round(actualBrightness) == 50);
    }

    @Test
    @ExecSequentially("display-brightness")
    public void getBrightness() throws Exception {
        float brightness = DisplayUtils.GetBrightness(appContext);
        assertTrue(brightness >= 0 && brightness <= 100);
    }

    @Test
    @ExecSequentially("display-brightness")
    public void setBrightnessMode() throws Exception {
        DisplayUtils.SetBrightnessMode(appContext, DisplayUtils.BrightnessMode.AUTO);
        assertTrue(DisplayUtils.GetBrightnessMode(appContext) == DisplayUtils.BrightnessMode.AUTO);
    }

    @Test
    @ExecSequentially("display-brightness")
    public void getBrightnessMode() throws Exception {
        DisplayUtils.GetBrightnessMode(appContext);
    }

    @Test
    @ExecSequentially("screen-state")
    public void setScreenOffTimeout() throws Exception {
        int timeout = 345334;
        DisplayUtils.SetScreenOffTimeout(appContext, timeout);
        int actualScreenOffTimeout = DisplayUtils.GetScreenOffTimeout(appContext);
        assertTrue(actualScreenOffTimeout == timeout);
    }

    @Test
    @ExecSequentially("screen-state")
    public void getScreenOffTimeout() throws Exception {
        DisplayUtils.GetScreenOffTimeout(appContext);
    }

    @Test
    @ExecSequentially("screen-state")
    public void turnScreenOff() throws Exception {
        //emulators do not turn off the screen
        if (isEmulator())
            return;

        DisplayUtils.TurnScreenOff(appContext);
        assertTrue(!DisplayUtils.IsScreenOn(appContext));
    }

    @Test
    @ExecSequentially("screen-state")
    public void isScreenOn() throws Exception {
        DisplayUtils.IsScreenOn(appContext);
    }
}