package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;

import static org.junit.Assert.*;

/**
 * Created by Kaltenleitner Andreas on 27.10.2016.
 */
public class CommandGetMobileDataStateTest extends CommandTest {

    public CommandGetMobileDataStateTest() {
        super(Instances.MOBILE_DATA.commandGetMobileDataState);
    }

    @Override
    protected void testPattern() throws Exception {
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
    protected void testExecution() throws Exception {
        assertThat("is mobile data enabled").executes();
    }
}