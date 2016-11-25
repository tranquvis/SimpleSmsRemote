package tranquvis.simplesmsremote.CommandManagement.Params;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Andreas Kaltenleitner on 03.11.2016.
 */
public class CommandParamOnOffTest {
    @Test
    public void getValueFromInput() throws Exception {
        CommandParamOnOff param = new CommandParamOnOff("");
        assertTrue(param.getValueFromInput("  enable test "));
        assertTrue(param.getValueFromInput("on test "));
        assertFalse(param.getValueFromInput("  disable test "));
        assertFalse(param.getValueFromInput("disabled test "));
        assertFalse(param.getValueFromInput("  off test "));
    }
}