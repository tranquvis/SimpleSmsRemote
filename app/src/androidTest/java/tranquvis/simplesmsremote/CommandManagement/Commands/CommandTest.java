package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import tranquvis.simplesmsremote.AppContextTest;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.Utils.Regex.MatcherTreeNode;
import tranquvis.simplesmsremote.Utils.UnitTestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Kaltenleitner Andreas on 26.10.2016.
 */

public abstract class CommandTest extends AppContextTest
{
    protected Command command;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (command == null) {
            Class<? extends Command> testedClass = UnitTestUtils.GetTestedClassFrom(getClass());
            Constructor<? extends Command> constructor = testedClass.getConstructor(Module.class);
            command = constructor.newInstance((Module)null);
        }
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Format input string with default locale
     * @param input input string
     * @param params string parameters
     * @return formatted string
     */
    protected String format(String input, Object... params)
    {
        return String.format(Locale.getDefault(), input, params);
    }

    @Test
    public abstract void testPattern() throws Exception;

    @Test
    public abstract void testExecution() throws Exception;

    /**
     * Make assertions about a user input.
     * @param input the user input
     * @return the Test class, with which assertions can be made
     * @throws Exception
     */
    protected CommandTester assertThat(String input) throws Exception {
        return new CommandTester(input, command, appContext);
    }

    public static class CommandTester
    {
        private String input;
        private Command command;
        private Context context;
        private CommandInstance ci;

        private CommandTester(String input, Command command, Context context)
                throws Exception {
            this.input = input;
            ci = CommandInstance.CreateFromCommand(input);
            this.command = command;
            this.context = context;
        }

        /**
         * Assert that the input matches a specific command.
         * @param command the command
         * @throws Exception
         */
        public CommandTester matches(Command command) throws Exception {
            MatcherTreeNode matcherTree = command.getPatternTree().buildMatcherTree();

            //region build matcher tree once more in order to find fail details
            if (!matcherTree.testInput(input)) {
                String details = "";
                boolean firstFail = true;
                for (MatcherTreeNode matcherTreeNode : matcherTree.getFailedNodesOfLastMatch())
                {
                    if(!firstFail) details += "\r\n";
                    else firstFail = false;

                    details += "Match failed at pattern  '" + matcherTreeNode.getPattern().getRegex()
                            + "'  with input '" + matcherTreeNode.getInput() +
                            "': \r\n\t" + matcherTreeNode.getLastMatchResult().getFailDetail();
                }
                throw new AssertionFailedError("The given input ('" + input +
                        "') does not match the command. \r\n" + details);
            }
            //endregion

            assertTrue("unexpected command retrieved: '" +
                        context.getString(ci.getCommand().getTitleRes())  + "' except '" +
                            context.getString(command.getTitleRes()) + "'",
                    ci != null &&  ci.getCommand().equals(command));
            return this;
        }

        /**
         * Assert that the input matches the default command of the related unit test.
         * @throws Exception
         */
        public CommandTester matches() throws Exception {
            return matches(command);
        }

        /**
         * Assert that the input does not match a specific command.
         * @param command the command
         * @throws Exception
         */
        public CommandTester doesNotMatch(Command command) throws Exception {
            assertFalse(ci != null &&  ci.getCommand() == command);
            return this;
        }

        /**
         * Assert that the input does not match the default command of the related unit test.
         * @throws Exception
         */
        public CommandTester doesNotMatch() throws Exception {
            return doesNotMatch(command);
        }

        /**
         * Assert that the input has a specific parameter, which is defined in its matching command.
         * @param param the parameter to check
         */
        public CommandTester has(CommandParam param) throws Exception {
            assertTrue(ci.getParam(param) != null);
            return this;
        }

        /**
         * Assert that the input has a specific parameter, which is defined in its matching command.
         * Moreover the value of the parameter is expected to equal the given {@code value}.
         * @param param the parameter to check
         * @param value the value, which the parameter should have
         * @param <T> type of the parameter's value
         */
        public <T> CommandTester has(CommandParam<T> param, T value) throws Exception {
            Object paramValue = ci.getParam(param);
            assertTrue(paramValue != null && paramValue.equals(value));
            return this;
        }

        /**
         * Assert that a command executes successful with the given input.
         * Assert before that the input matches the command.
         * @param command the command
         * @return the result of the execution
         * @throws Exception
         */
        public CommandExecResult executes(Command command) throws Exception {
            CommandExecResult result = new CommandExecResult(ci);
            result.setSuccess(true);
            command.execute(context, ci, result);
            assertTrue(result.isSuccess());
            return result;
        }

        /**
         * Assert that the default command of the related unit test
         * executes successful with the given input.
         * Assert before that the input matches the default command.
         * @return the result of the execution
         * @throws Exception
         */
        public CommandExecResult executes() throws Exception {
            return executes(command);
        }

        /**
         * Assert that a command executes with error with the given input.
         * Assert before that the input matches the command.
         * @param command the command
         * @return the result of the execution
         * @throws Exception
         */
        public CommandExecResult executesWithError(Command command) throws Exception {
            CommandExecResult result = new CommandExecResult(ci);
            result.setSuccess(false);
            try {
                command.execute(context, ci, result);
            }
            catch (Exception ex) {
                return result;
            }
            assertFalse(result.isSuccess());
            return result;
        }

        /**
         * Assert that the default command of the related unit test
         * executes with error with the given input.
         * Assert before that the input matches the default command.
         * @return the result of the execution
         * @throws Exception
         */
        public CommandExecResult executesWithError() throws Exception {
            return executesWithError(command);
        }
    }
}
