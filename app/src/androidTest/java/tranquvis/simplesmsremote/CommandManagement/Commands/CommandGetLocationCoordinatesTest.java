package tranquvis.simplesmsremote.CommandManagement.Commands;

import static org.junit.Assert.*;

/**
 * Created by Kaltenleitner Andreas on 30.10.2016.
 */
public class CommandGetLocationCoordinatesTest extends CommandTest {

    @Override
    public void testPattern() throws Exception {
        assertThat("\n Get  location \r").matches();
        assertThat("get position").matches();
        assertThat("retrieve location coordinates").matches();
        assertThat("fetch coordinates").matches();
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("get location").executes();
    }
}