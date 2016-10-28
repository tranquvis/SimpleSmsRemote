package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetWifiState.PARAM_WIFI_STATE;


/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandSetWifiStateTest extends CommandTest
{
    @Override
    public void testPattern() throws Exception
    {
        assertThat("\n enable Wifi \r").matches().has(PARAM_WIFI_STATE, true);
        assertThat("turn wifi on").matches().has(PARAM_WIFI_STATE, true);
        assertThat("turn on wlan").matches().has(PARAM_WIFI_STATE, true);
        assertThat("set wifi state to on").matches()
                .has(PARAM_WIFI_STATE, true);

        assertThat("disable wifi").matches().has(PARAM_WIFI_STATE, false);
        assertThat("turn wifi off").matches().has(PARAM_WIFI_STATE, false);
        assertThat("turn off wlan").matches().has(PARAM_WIFI_STATE, false);
        assertThat("set wifi state to off").matches()
                .has(PARAM_WIFI_STATE, false);
    }

    @Override
    public void testExecution() throws Exception
    {
        assertThat("enable wifi").executes();
        assertThat("disable wifi").executes();
    }
}