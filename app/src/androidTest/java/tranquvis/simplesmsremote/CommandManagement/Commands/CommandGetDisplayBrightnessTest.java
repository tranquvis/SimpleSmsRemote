package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandGetDisplayBrightnessTest extends CommandTest {

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n get  Brightness \r").matches();
        assertThat("fetch brightness of display").matches();
        assertThat("retrieve display brightness").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("get brightness").matches().executes();
    }
}