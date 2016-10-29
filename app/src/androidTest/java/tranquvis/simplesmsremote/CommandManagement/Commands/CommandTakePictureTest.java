package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.CommandTest;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandTakePictureTest extends CommandTest {
    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n TAKE picture \r").matches();
        assertThat("take photo").matches();
        assertThat("capture photo").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("take picture").executes();
    }
}