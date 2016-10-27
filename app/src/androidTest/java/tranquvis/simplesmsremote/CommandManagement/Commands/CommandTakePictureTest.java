package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandTest;

import static org.junit.Assert.*;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandTakePictureTest extends CommandTest
{

    public CommandTakePictureTest()
    {
        super(Commands.TAKE_PICTURE);
    }

    @Override
    protected void testPattern() throws Exception
    {
        assertThat("\n TAKE picture \r").matches();
        assertThat("take photo").matches();
        assertThat("capture photo").matches();
    }

    @Override
    protected void testExecution() throws Exception
    {
        assertThat("take picture").executes();
    }
}