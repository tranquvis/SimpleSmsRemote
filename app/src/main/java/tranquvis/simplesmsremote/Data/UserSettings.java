package tranquvis.simplesmsremote.Data;

import java.io.Serializable;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class UserSettings implements Serializable
{
    private boolean startReceiverOnSystemStart;
    private boolean notifyCommandsExecuted;

    public UserSettings()
    {
    }

    public boolean isStartReceiverOnSystemStart()
    {
        return startReceiverOnSystemStart;
    }

    public void setStartReceiverOnSystemStart(boolean startReceiverOnSystemStart)
    {
        this.startReceiverOnSystemStart = startReceiverOnSystemStart;
    }

    public boolean isNotifyCommandsExecuted()
    {
        return notifyCommandsExecuted;
    }

    public void setNotifyCommandsExecuted(boolean notifyCommandsExecuted)
    {
        this.notifyCommandsExecuted = notifyCommandsExecuted;
    }


}
