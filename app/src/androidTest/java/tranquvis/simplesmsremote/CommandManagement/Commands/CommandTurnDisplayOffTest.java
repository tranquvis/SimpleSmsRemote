package tranquvis.simplesmsremote.CommandManagement.Commands;

/**
 * Created by Kaltenleitner Andreas on 01.11.2016.
 */
public class CommandTurnDisplayOffTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("\n turn  Display off \r").matches();
        assertThat("turn screen off").matches();
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("turn screen off").matches().executes();
    }
}