package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Before;
import org.junit.Test;

import tranquvis.simplesmsremote.Data.DataManager;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */
public class CommandTakePictureTest extends CommandTest {

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        DataManager.LoadUserData(appContext);
    }

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n Take  picture \r").matches();
        assertThat("take photo").matches();
        assertThat("capture photo").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("take picture").matches().executes();
    }
}