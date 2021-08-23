package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.CommandManagement.Commands.PhoneDependentCommand;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.Data.ModuleUserData;
import tranquvis.simplesmsremote.Data.PhoneWhitelistModuleUserData;
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
     * @throws Exception
     */
    public static CommandInstance CreateFromCommand(String commandText) throws Exception {
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
    public boolean isParamAssigned(CommandParam commandParam) {
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

    public boolean isExecutionGranted(Context context, String phone) {
        Module module = getCommand().getModule();
        ModuleUserData userData = module.getUserData();

        if (!module.isCompatible(context)) {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneIncompatible(context,
                    getCommand()), context);
            return false;
        }
        if (userData == null) {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedModuleDisabled(context,
                    getCommand()), context);
            return false;
        }
        if (userData instanceof PhoneWhitelistModuleUserData) {
            if (!((PhoneWhitelistModuleUserData) userData).isPhoneGranted(phone)) {
                DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPhoneNotGranted(
                        context, getCommand(), phone
                ), context);
                return false;
            }
        }
        if (!module.checkPermissions(context)) {
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedPermissionDenied(context,
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
    public CommandExecResult executeCommand(Context context, MyCommandMessage controlSms) {
        CommandExecResult result = new CommandExecResult(this);
        Command command = getCommand();

        if (!isExecutionGranted(context, controlSms.getPhoneNumber())) {
            result.setSuccess(false);
            return result;
        }

        try {
            if (command instanceof PhoneDependentCommand) {
                PhoneDependentCommand dependentCommand = (PhoneDependentCommand) command;
                dependentCommand.execute(context, this, controlSms.getPhoneNumber(), result);
            } else {
                command.execute(context, this, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DataManager.addLogEntry(LogEntry.Predefined.ComExecFailedUnexpected(context, command),
                    context);
            result.setSuccess(false);
        }

        DataManager.addLogEntry(LogEntry.Predefined.ComExecSuccess(context, command), context);
        result.setSuccess(true);

        return result;
    }
}
