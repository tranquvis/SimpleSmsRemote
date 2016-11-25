package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

/**
 * Created by Kaltenleitner Andreas on 01.11.2016.
 */
public class CommandGetDisplayOffTimeoutTest extends CommandTest {

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n get screen  Off timeout").matches();
        assertThat("fetch display off timeout").matches();
        assertThat("retrieve screen off timeout").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("fetch display off timeout").matches().executes();
    }
}