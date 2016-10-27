package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandGetHotspotStateTest extends CommandTest
{
    public CommandGetHotspotStateTest()
    {
        super(Commands.GET_HOTSPOT_STATE);
    }

    @Override
    protected void testPattern() throws Exception
    {
        assertThat("\n is  Hotspot enabled \r").matches();
        assertThat("is hotspot on").matches();
        assertThat("is hotspot disabled").matches();
        assertThat("is hotspot off").matches();
        assertThat("is wifi hotspot disabled").matches();
        assertThat("wlan hotspot enabled?").matches();
        assertThat("hotspot disabled?").matches();
        assertThat("get hotspot state").matches();
    }

    @Override
    protected void testExecution() throws Exception
    {
        assertThat("is hotspot enabled").executes();
    }
}