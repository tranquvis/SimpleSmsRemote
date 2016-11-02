package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

/**
 * Created by Kaltenleitner Andreas on 27.10.2016.
 */
public class CommandGetMobileDataStateTest extends CommandTest {

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n is  Mobile data enabled \r").matches();
        assertThat("is mobile internet connection on").matches();
        assertThat("is mobile internet disabled").matches();
        assertThat("is mobile data off").matches();
        assertThat("is mobile data disabled").matches();
        assertThat("mobile data enabled?").matches();
        assertThat("mobile data disabled?").matches();
        assertThat("get mobile data state").matches();
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("is mobile data enabled").matches().executes();
    }
}