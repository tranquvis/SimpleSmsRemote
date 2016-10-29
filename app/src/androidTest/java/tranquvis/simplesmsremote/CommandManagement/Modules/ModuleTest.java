package tranquvis.simplesmsremote.CommandManagement.Modules;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
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
public abstract class ModuleTest extends AppContextTest
{
    protected Module module;
    protected List<Command> commands;

    protected boolean assertCompatibility = false;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        if(module == null) {
            Class<? extends Module> testedClass = UnitTestUtils.GetTestedClassFrom(getClass());
            module = testedClass.newInstance();
        }
        this.commands = module.getCommands();

        if(assertCompatibility)
            assertTrue(module.isCompatible());
    }

    @Test
    public void testCommands() throws Exception {
        for (Command command : commands) {
            execUnitTestOfCom(command);
        }
    }

    private void execUnitTestOfCom(Command command) throws Exception {
        Class<? extends CommandTest> unitTestClass =
                UnitTestUtils.GetUnitTestClassFrom(command.getClass());
        CommandTest commandTest = unitTestClass.newInstance();
        commandTest.setCommand(command);
        commandTest.setUp();
        commandTest.testPattern();
        commandTest.testExecution();
    }
}
