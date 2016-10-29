package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */
public class CommandGetBluetoothStateTest extends CommandTest {

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n is  Bluetooth enabled \r").matches();
        assertThat("is bluetooth on").matches();
        assertThat("is bluetooth disabled").matches();
        assertThat("is bluetooth off").matches();
        assertThat("is bluetooth disabled").matches();
        assertThat("bluetooth enabled?").matches();
        assertThat("bluetooth disabled?").matches();
        assertThat("get bluetooth state").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("is bluetooth enabled").executes();
    }
}