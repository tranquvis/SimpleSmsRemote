package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import org.junit.Test;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.Sms.MyCommandMessage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
 */

public abstract class CommandTest extends AppContextTest
{
    protected Command command;

    public CommandTest(Command command) {
        this.command = command;
    }

    @Test
    protected abstract void testPattern() throws Exception;

    @Test
    protected abstract void testExecution() throws Exception;

    protected void execute(String input)
    {

    }

    protected CommandTester assertThat(String input) throws Exception {
        return new CommandTester(input, command, appContext);
    }

    protected static class CommandTester
    {
        private Command defaultCommand;
        private Context context;
        private CommandInstance ci;

        private CommandTester(String input, Command defaultCommand, Context context)
                throws Exception {
            ci = CommandInstance.CreateFromCommand(input);
            this.defaultCommand = defaultCommand;
            this.context = context;
        }

        public CommandTester matches(Command command) throws Exception {
            assertTrue(ci != null &&  ci.getCommand() == command);
            return this;
        }

        public CommandTester matches() throws Exception {
            return matches(defaultCommand);
        }

        public CommandTester doesNotMatch(Command command) throws Exception {
            assertFalse(ci == null ||  ci.getCommand() == command);
            return this;
        }

        public CommandTester doesNotMatch() throws Exception {
            return doesNotMatch(defaultCommand);
        }

        public CommandTester has(CommandParam param)
        {
            assertTrue(ci.getParam(param) != null);
            return this;
        }

        public CommandTester has(CommandParam param, Object value)
        {
            Object paramValue = ci.getParam(param);
            assertTrue(paramValue != null && paramValue.equals(value));
            return this;
        }

        public CommandExecResult executes(Command command) throws Exception {
            CommandExecResult result = new CommandExecResult(ci);
            result.setSuccess(false);
            command.execute(context, ci, result);
            assertTrue(result.isSuccess());
            return result;
        }

        public CommandExecResult executes() throws Exception {
            return executes(defaultCommand);
        }
    }
}
