package tranquvis.simplesmsremote.Data;

import android.os.Build;

import java.io.Serializable;

/**
 * Created by Andreas Kaltenleitner on 30.08.2016.
 */
public class UserSettings implements Serializable
{
    private boolean startReceiverOnSystemStart;
    private boolean notifyCommandsExecuted = true; //unused
    private boolean replyWithResult;
    private boolean receiverStartForeground = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

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


    public boolean isReplyWithResult()
    {
        return replyWithResult;
    }

    public void setReplyWithResult(boolean replyWithResult)
    {
        this.replyWithResult = replyWithResult;
    }

    public boolean isReceiverStartForeground()
    {
        return receiverStartForeground;
    }

    public void setReceiverStartForeground(boolean receiverStartForeground)
    {
        this.receiverStartForeground = receiverStartForeground;
    }
}
