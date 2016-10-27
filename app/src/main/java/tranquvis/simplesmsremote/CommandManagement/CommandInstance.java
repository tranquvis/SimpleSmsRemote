package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Sms.MyCommandMessage;
import tranquvis.simplesmsremote.Utils.Regex.MatcherTreeNode;

/**
 * Created by Andi on 07.10.2016.
 */

public class CommandInstance {
    private Command command;
    private String commandText;
    private MatcherTreeNode matcherTree;

    /**
     * Create command instance and fill it with parameters
     * @param command the underlying control command
     * @param commandText the command text which represents this instance
     * @param matcherTree The regex matcher tree for {@code commandText},
     *                    which contains all parameters.
     */
    private CommandInstance(Command command, String commandText,
                            MatcherTreeNode matcherTree) {
        this.command = command;
        this.commandText = commandText;
        this.matcherTree = matcherTree;
    }

    /**
     * Extract param from matcher tree.
     * @param commandParam the param which schould be retrieved
     * @param <T> value type
     * @return the param value or null if the param hasn' been found
     */
    public <T> T getParam(CommandParam<T> commandParam)
    {
        MatcherTreeNode paramNode = matcherTree.getNodeByPatternId(commandParam.getId());
        if(paramNode == null)
            return null;
        String paramInput = paramNode.getInput();
        if(paramInput == null)
            return null;
        return commandParam.getValueFromInput(paramInput);
    }

    public Command getCommand() {
        return command;
    }

    public String getCommandText() {
        return commandText;
    }

    @Override
    public String toString() {
        return getCommandText();
    }

    /**
     * get control command instance from command text
     * @param commandText command text
     * @return control command instance
     * @throws Exception
     */
    public static CommandInstance CreateFromCommand(String commandText) throws Exception {
        for (Command com : Command.GetAllCommands(null)) {
            MatcherTreeNode matcherTree = com.patternTree.buildMatcherTree();
            if (matcherTree.testInput(commandText)) {
                return new CommandInstance(com, commandText, matcherTree);
            }
        }
        return null;
    }

    public boolean isExecutionGranted(Context context, String phone)
    {
        Module module = getCommand().getModule();
        ControlModuleUserData moduleUserData = module.getUserData();

        if(!module.isCompatible())
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    getCommand()), context);
            return false;
        }
        if(moduleUserData == null)
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context,
                    getCommand()), context);
            return false;
        }
        if(!moduleUserData.isPhoneGranted(phone))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneNotGranted(context,
                    getCommand(), phone), context);
            return false;
        }
        if(!module.checkPermissions(context))
        {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPermissionDenied(context,
                    getCommand()), context);
            return false;
        }

        return true;
    }

    /**
     * execute control command
     * @param context app context
     * @param controlSms command message
     * @return execution result
     */
    public CommandExecResult executeCommand(Context context, MyCommandMessage controlSms)
    {
        CommandExecResult result = new CommandExecResult(this);
        Command command = getCommand();

        if(!isExecutionGranted(context, controlSms.getPhoneNumber()))
            result.setSuccess(false);
        else
        {
            try
            {
                command.execute(context, this, result);
                DataManager.addLogEntry(LogEntry.Predefined.ComExecSuccess(context, command), context);
                result.setSuccess(true);
            } catch (Exception e)
            {
                e.printStackTrace();
                DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedUnexpected(context, command),
                        context);
                result.setSuccess(false);
            }
        }

        return result;
    }
}
