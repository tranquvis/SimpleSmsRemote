package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.Data.PhoneAllowlistModuleUserData;
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
     *
     * @param command     the underlying control command
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
     * get control command instance from command text
     *
     * @param commandText command text
     * @return control command instance
     */
    public static CommandInstance CreateFromCommand(String commandText) {
        for (Command com : Command.GetAllCommands(null)) {
            MatcherTreeNode matcherTree = com.getPatternTree().buildMatcherTree();
            if (matcherTree.testInput(commandText)) {
                return new CommandInstance(com, commandText, matcherTree);
            }
        }
        return null;
    }

    /**
     * Extract param from matcher tree.
     *
     * @param commandParam the param which should be retrieved
     * @param <T>          value type
     * @return param value or null if the param hasn't been found or isn't set
     */
    public <T> T getParam(CommandParam<T> commandParam) throws Exception {
        MatcherTreeNode paramNode = matcherTree.getNodeByPatternId(commandParam.getId());
        if (paramNode == null)
            return null;
        String paramInput = paramNode.getInput();
        if (paramInput == null)
            return null;
        return commandParam.getValueFromInput(paramInput);
    }

    /**
     * Check if param is assigned in matcher tree.
     *
     * @param commandParam the param, which should be checked
     * @return if param is assigned
     */
    public <T> boolean isParamAssigned(CommandParam<T> commandParam) {
        MatcherTreeNode paramNode = matcherTree.getNodeByPatternId(commandParam.getId());
        return paramNode != null && commandParam.isAssigned(paramNode.getInput());
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

    public boolean isExecutionGranted(Context context, String phone, DataManager dataManager) {
        Module module = getCommand().getModule();
        ModuleUserData userData = dataManager.getModuleUserData(module);

        if (!module.isCompatible(context)) {
            dataManager.tryAddLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    getCommand()), context);
            return false;
        }
        if (userData == null) {
            dataManager.tryAddLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context,
                    getCommand()), context);
            return false;
        }
        if (userData instanceof PhoneAllowlistModuleUserData) {
            if (!((PhoneAllowlistModuleUserData) userData).isPhoneGranted(phone)) {
                dataManager.tryAddLogEntry(LogEntry.Predefined.ComExecFailedPhoneNotGranted(
                        context, getCommand(), phone
                ), context);
                return false;
            }
        }
        if (!module.checkPermissions(context)) {
            dataManager.tryAddLogEntry(LogEntry.Predefined.ComExecFailedPermissionDenied(context,
                    getCommand()), context);
            return false;
        }

        return true;
    }

    /**
     * execute control command
     *
     * @param context    app context
     * @param controlSms command message
     * @return execution result
     */
    public CommandExecResult executeCommand(Context context, MyCommandMessage controlSms,
            DataManager dataManager) {
        CommandExecResult result = new CommandExecResult(this);
        Command command = getCommand();

        if (!isExecutionGranted(context, controlSms.getPhoneNumber(), dataManager)) {
            result.setSuccess(false);
            return result;
        }

        try {
            command.execute(context, this, controlSms.getPhoneNumber(), result,
                    dataManager);
        } catch (Exception e) {
            e.printStackTrace();
            dataManager.tryAddLogEntry(
                    LogEntry.Predefined.ComExecFailedUnexpected(context, command), context);
            result.setSuccess(false);
            return result;
        }

        dataManager.tryAddLogEntry(LogEntry.Predefined.ComExecSuccess(context, command), context);
        result.setSuccess(true);

        return result;
    }
}
