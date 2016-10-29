package tranquvis.simplesmsremote.CommandManagement;

public class CommandExecResult
{
    private CommandInstance commandInstance;
    private boolean success = true;
    private String customResultMessage = null;
    private boolean forceSendingResultSmsMessage = false;

    public CommandExecResult(CommandInstance commandInstance)
    {
        this.commandInstance = commandInstance;
    }


    public CommandInstance getCommandInstance() {
        return commandInstance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCustomResultMessage() {
        return customResultMessage;
    }

    public void setCustomResultMessage(String customResultMessage) {
        this.customResultMessage = customResultMessage;
    }

    public boolean isForceSendingResultSmsMessage() {
        return forceSendingResultSmsMessage;
    }

    public void setForceSendingResultSmsMessage(boolean forceSendingResultSmsMessage) {
        this.forceSendingResultSmsMessage = forceSendingResultSmsMessage;
    }
}
