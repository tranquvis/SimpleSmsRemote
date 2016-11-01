package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;

import static org.junit.Assert.*;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetDisplayBrightness.*;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandSetDisplayBrightnessTest extends CommandTest
{

    @Override
    @Test
    public void testPattern() throws Exception
    {
        assertThat("\n set  Display brightness to Auto \r").matches()
                .has(PARAM_BRIGHTNESS_MODE, DisplayUtils.BrightnessMode.AUTO);
        assertThat("set brightness to 10%").matches()
                .has(PARAM_BRIGHTNESS_VALUE, 10d);
        assertThat("set brightness of display to 100%").matches()
                .has(PARAM_BRIGHTNESS_VALUE, 100d);
        assertThat("set brightness to 0%").matches()
                .has(PARAM_BRIGHTNESS_VALUE, 0d);

    }

    @Override
    @Test
    public void testExecution() throws Exception
    {
        assertThat("set brightness to 0%").executes();
        assertThat("set brightness to auto").executes();
        assertThat("set brightness to 100%").executes();
        assertThat("set brightness to 50,4%").executes();
    }
}