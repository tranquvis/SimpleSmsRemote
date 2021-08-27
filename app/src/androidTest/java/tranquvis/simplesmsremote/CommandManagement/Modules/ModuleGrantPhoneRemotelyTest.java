package tranquvis.simplesmsremote.CommandManagement.Modules;

import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotely;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandGrantPhoneRemotelyTest;

public class ModuleGrantPhoneRemotelyTest extends ModuleTest {
    @Override
    public void testCommands() throws Exception {
        super.testCommands();
        CommandGrantPhoneRemotelyTest commandTest =
                getUnitTestFrom(getCommand(CommandGrantPhoneRemotely.class));
        commandTest.testExecutionGrantSpecific();
        commandTest.testExecutionWrongPassword();
        commandTest.testExecutionGrantAll();
    }
}
