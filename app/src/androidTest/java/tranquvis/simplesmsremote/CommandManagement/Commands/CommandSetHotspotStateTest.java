package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;

import static org.junit.Assert.*;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetHotspotState.PARAM_HOTSPOT_STATE;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandSetHotspotStateTest extends CommandTest
{
    @Override
    public void testPattern() throws Exception
    {
        assertThat("\n enable Wifi hotspot \r").matches().has(PARAM_HOTSPOT_STATE, true);
        assertThat("turn hotspot on").matches().has(PARAM_HOTSPOT_STATE, true);
        assertThat("turn on wlan hotspot").matches().has(PARAM_HOTSPOT_STATE, true);
        assertThat("set hotspot state to on").matches()
                .has(PARAM_HOTSPOT_STATE, true);

        assertThat("disable hotspot").matches().has(PARAM_HOTSPOT_STATE, false);
        assertThat("turn hotspot off").matches().has(PARAM_HOTSPOT_STATE, false);
        assertThat("turn off hotspot").matches().has(PARAM_HOTSPOT_STATE, false);
        assertThat("set hotspot state to off").matches()
                .has(PARAM_HOTSPOT_STATE, false);
    }

    @Override
    public void testExecution() throws Exception
    {
        assertThat("enable hotspot").executes();
        assertThat("disable hotspot").executes();
    }
}