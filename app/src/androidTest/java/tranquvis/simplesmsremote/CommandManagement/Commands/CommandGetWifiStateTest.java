package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
 */
public class CommandGetWifiStateTest extends CommandTest {

    public CommandGetWifiStateTest() {
        super(Commands.GET_WIFI_STATE);
    }

    @Override
    protected void testPattern() throws Exception {
        assertThat("is wifi enabled").matches();
        assertThat("is wifi disabled").matches();
        assertThat("wifi enabled?").matches();
        assertThat("\rwifi disabled?").matches();
        assertThat("get   wifi state\n").matches();
    }

    @Override
    protected void testExecution() throws Exception {
        assertThat("is wifi enabled").executes();

    }
}