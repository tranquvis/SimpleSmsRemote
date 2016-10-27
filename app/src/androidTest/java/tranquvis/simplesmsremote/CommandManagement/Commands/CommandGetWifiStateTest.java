package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
 */
public class CommandGetWifiStateTest extends CommandTest {

    public CommandGetWifiStateTest() {
        super(Instances.WIFI.commandGetWifiState);
    }

    @Override
    protected void testPattern() throws Exception {
        assertThat("\n is  Wifi enabled \r").matches();
        assertThat("is wifi on").matches();
        assertThat("is wifi disabled").matches();
        assertThat("is wifi off").matches();
        assertThat("is wifi disabled").matches();
        assertThat("wifi enabled?").matches();
        assertThat("wifi disabled?").matches();
        assertThat("get wifi state").matches();
    }

    @Override
    protected void testExecution() throws Exception {
        assertThat("is wifi enabled").executes();
    }
}