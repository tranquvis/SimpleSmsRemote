package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetBluetoothState.PARAM_BLUETOOTH_STATE;

/**
 * Created by Andreas Kaltenleitner on 28.10.2016.
 */
public class CommandSetBluetoothStateTest extends CommandTest {
    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n enable  Bluetooth \r").matches().has(PARAM_BLUETOOTH_STATE, true);
        assertThat("turn bluetooth on").matches().has(PARAM_BLUETOOTH_STATE, true);
        assertThat("turn on bluetooth").matches().has(PARAM_BLUETOOTH_STATE, true);
        assertThat("set bluetooth to on").matches()
                .has(PARAM_BLUETOOTH_STATE, true);

        assertThat("disable bluetooth").matches().has(PARAM_BLUETOOTH_STATE, false);
        assertThat("turn bluetooth off").matches().has(PARAM_BLUETOOTH_STATE, false);
        assertThat("turn off bluetooth").matches().has(PARAM_BLUETOOTH_STATE, false);
        assertThat("set bluetooth state to off").matches()
                .has(PARAM_BLUETOOTH_STATE, false);
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("enable bluetooth").matches().executes();
        assertThat("disable bluetooth").matches().executes();
    }
}