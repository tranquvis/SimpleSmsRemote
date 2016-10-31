package tranquvis.simplesmsremote.CommandManagement;

import android.content.Context;

import tranquvis.simplesmsremote.Data.ControlModuleUserData;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Sms.MyMessage;

/**
 * Created by Andi on 08.10.2016.
 */

public class CommandExec
{
    private CommandInstance commandInstance;
    private Context context;

    public CommandExec(CommandInstance commandInstance, Context context) {
        this.commandInstance = commandInstance;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public CommandInstance getCommandInstance() {
        return commandInstance;
    }

    public CommandExecResult execute(MyMessage controlSms)
    {
        return null;
        /*
        else if(command == DISPLAY_SET_OFF_TIMEOUT)
        {
            String timeoutStr =
                    commandInstance.getParam(Command.PARAM_DISPLAY_OFF_TIMEOUT);

            int timeoutStrLength = timeoutStr.length();
            String timeoutValueStr;
            int timeoutMilliseconds;
            if(timeoutStr.endsWith("ms"))
            {
                timeoutValueStr = timeoutStr.substring(0, timeoutStrLength - 2);
                timeoutMilliseconds = (int)Float.parseFloat(timeoutValueStr);
            }
            else if(timeoutStr.endsWith("s"))
            {
                timeoutValueStr = timeoutStr.substring(0, timeoutStrLength - 1);
                timeoutMilliseconds = (int)(Float.parseFloat(timeoutValueStr) * 1000);
            }
            else if(timeoutStr.endsWith("min"))
            {
                timeoutValueStr = timeoutStr.substring(0, timeoutStrLength - 3);
                timeoutMilliseconds = (int)(Float.parseFloat(timeoutValueStr) * 60000);
            }
            else
            {
               throw new Exception("no unit detected");
            }

            DisplayUtils.SetScreenOffTimeout(context, timeoutMilliseconds);
        }
        else if (command == DISPLAY_GET_OFF_TIMEOUT)
        {
            float screenOffTimeout = DisplayUtils.GetScreenOffTimeout(context);

            String timeoutStr;
            if(screenOffTimeout < 900)
                timeoutStr = String.format("%.0fms", screenOffTimeout);
            else if(screenOffTimeout < 60000)
                timeoutStr = String.format("%ds", Math.round(screenOffTimeout / 1000f));
            else
                timeoutStr = String.format("%.1fmin", screenOffTimeout / 60000f);

            result.setCustomResultMessage(context.getString(
                    R.string.result_msg_display_off_timeout, timeoutStr));
            result.setForceSendingResultSmsMessage(true);
        }
        else if (command == DISPLAY_TURN_OFF)
        {
            DisplayUtils.TurnScreenOff(context);
            Thread.sleep(2000); // otherwise the notification forces the screen to turn on again
        }
        */
    }
}