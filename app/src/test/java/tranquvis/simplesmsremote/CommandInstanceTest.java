package tranquvis.simplesmsremote;

import org.junit.Test;

import tranquvis.simplesmsremote.CommandManagement.CommandInstance;

import static org.junit.Assert.*;

/**
 * Created by Andi on 07.10.2016.
 */
public class CommandInstanceTest {
    @Test
    public void getFromCommand() throws Exception {
        CommandInstance cci =
                CommandInstance.CreateFromCommand("set   volume \"tt \n''t\"\n to \"10 0%");
        assertTrue(cci != null);
    }
}