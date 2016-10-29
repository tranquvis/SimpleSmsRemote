package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
 */
public class CommandGetWifiStateTest extends CommandTest {

    @Override
    @Test
    public void testPattern() throws Exception {
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
    @Test
    public void testExecution() throws Exception {
        assertThat("is wifi enabled").executes();
    }
}