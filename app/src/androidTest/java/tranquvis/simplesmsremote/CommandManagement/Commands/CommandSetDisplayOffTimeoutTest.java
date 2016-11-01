package tranquvis.simplesmsremote.CommandManagement.Commands;

import org.junit.Test;

import tranquvis.simplesmsremote.Utils.UnitTools.Unit;

import static tranquvis.simplesmsremote.CommandManagement.Commands.CommandSetDisplayOffTimeout.*;

/**
 * Created by Kaltenleitner Andreas on 01.11.2016.
 */
public class CommandSetDisplayOffTimeoutTest extends CommandTest
{

    @Override
    @Test
    public void testPattern() throws Exception {
        assertThat("\n set  screen Off timeout to 0 S").matches()
                .has(PARAM_TIMEOUT_VALUE, 0d)
                .has(PARAM_TIMEOUT_UNIT, Unit.SECONDS);

        assertThat("set display off timeout to 10000ms").matches()
                .has(PARAM_TIMEOUT_VALUE, 10000d)
                .has(PARAM_TIMEOUT_UNIT, Unit.MILLISECONDS);
        assertThat("set display off timeout to 10s").matches()
                .has(PARAM_TIMEOUT_VALUE, 10d)
                .has(PARAM_TIMEOUT_UNIT, Unit.SECONDS);
        assertThat("set display off timeout to 21.4min").matches()
                .has(PARAM_TIMEOUT_VALUE, 21.4d)
                .has(PARAM_TIMEOUT_UNIT, Unit.MINUTES);
        assertThat("set display off timeout to 1.444h").matches()
                .has(PARAM_TIMEOUT_VALUE, 1.444d)
                .has(PARAM_TIMEOUT_UNIT, Unit.HOURS);
    }

    @Override
    @Test
    public void testExecution() throws Exception {
        assertThat("set display off timeout to 10h").executes();
        assertThat("set display off timeout to 0ms").executes();
    }
}