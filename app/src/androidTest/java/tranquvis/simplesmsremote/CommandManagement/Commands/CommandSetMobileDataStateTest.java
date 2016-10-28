package tranquvis.simplesmsremote.CommandManagement.Commands;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandTest;
import tranquvis.simplesmsremote.CommandManagement.Module;
import tranquvis.simplesmsremote.CommandManagement.Modules.Instances;

import static org.junit.Assert.*;
import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetMobileDataState.PARAM_MOBILE_DATA_STATE;

/**
 * Created by Kaltenleitner Andreas on 27.10.2016.
 */
public class CommandSetMobileDataStateTest extends CommandTest
{
    @Override
    public void testPattern() throws Exception {
        assertThat("\n enable Mobile data \r").matches().has(PARAM_MOBILE_DATA_STATE, true);
        assertThat("turn mobile internet connection on").matches().has(PARAM_MOBILE_DATA_STATE, true);
        assertThat("turn on mobile internet").matches().has(PARAM_MOBILE_DATA_STATE, true);
        assertThat("set mobile data state to on").matches()
                .has(PARAM_MOBILE_DATA_STATE, true);

        assertThat("disable mobile data").matches().has(PARAM_MOBILE_DATA_STATE, false);
        assertThat("turn mobile data off").matches().has(PARAM_MOBILE_DATA_STATE, false);
        assertThat("turn off mobile data").matches().has(PARAM_MOBILE_DATA_STATE, false);
        assertThat("set mobile data state to off").matches()
                .has(PARAM_MOBILE_DATA_STATE, false);
    }

    @Override
    public void testExecution() throws Exception {
        assertThat("enable mobile data").executes();
        assertThat("disable mobile data").executes();
    }
}