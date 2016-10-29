package tranquvis.simplesmsremote.CommandManagement.Commands;

import static org.junit.Assert.*;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */
public class CommandGetBatteryLevelTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("\n get  Battery level \r").matches();
        assertThat("get battery value").matches();
        assertThat("fetch battery value").matches();
        assertThat("retrieve battery level").matches();
        assertThat("get battery charge").matches();
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("get battery level").executes();
    }
}