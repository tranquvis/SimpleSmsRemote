package tranquvis.simplesmsremote.Utils;

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
}