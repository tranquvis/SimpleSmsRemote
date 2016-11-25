package tranquvis.simplesmsremote.CommandManagement.Modules;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.CommandManagement.Commands.CommandTest;
import tranquvis.simplesmsremote.Utils.UnitTestUtils;

import static org.junit.Assert.assertTrue;

/**
 * Created by Kaltenleitner Andreas on 29.10.2016.
 */

/**
 * Commands are tested automatically in all inherited classes.
 */
public abstract class ModuleTest extends AppContextTest {
    private static List<String> successfulModuleTests = new ArrayList<>();
    private static List<String> failedModuleTests = new ArrayList<>();
    protected Module module;
    protected List<Command> commands;
    protected boolean assertCompatibility = true;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (module == null) {
            Class<? extends Module> testedClass = UnitTestUtils.GetTestedClassFrom(getClass());
            module = testedClass.newInstance();
        }
        this.commands = module.getCommands();

        if (assertCompatibility)
            assertTrue("module incompatible", module.isCompatible());
    }

    @Test
    public void testCommands() throws Exception {
        failedModuleTests.add(this.getClass().getSimpleName());

        for (Command command : commands) {
            execUnitTestOfCom(command);
        }

        failedModuleTests.remove(this.getClass().getSimpleName());
        successfulModuleTests.add(this.getClass().getSimpleName());
        Log.i("Successful Module Tests", org.apache.commons.lang3.StringUtils.join(
                successfulModuleTests, ", "));
        Log.e("Failed Module Tests", org.apache.commons.lang3.StringUtils.join(
                failedModuleTests, ", "));
    }

    private void execUnitTestOfCom(Command command) throws Exception {
        CommandTest commandTest = getUnitTestFrom(command);
        commandTest.testPattern();
        commandTest.testExecution();
    }

    protected <T extends CommandTest> T getUnitTestFrom(Command command) throws Exception {
        Class<T> unitTestClass = UnitTestUtils.GetUnitTestClassFrom(command.getClass());
        T commandTest = unitTestClass.newInstance();
        commandTest.setCommand(command);
        commandTest.setUp();
        return commandTest;
    }

    protected <T extends Command> T getCommand(Class<T> commandType) {
        for (Command command : commands) {
            if (command.getClass() == commandType)
                return (T) command;
        }
        return null;
    }
}
