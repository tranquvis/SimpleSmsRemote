package tranquvis.simplesmsremote.Services.Sms;

import android.content.Context;
import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 02.09.2016.
 */
public class MySmsSimpleMessage implements MySms
{
    private String phoneNumber;
    private String message;

    public MySmsSimpleMessage(String phoneNumber, String message)
    {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public static MySmsSimpleMessage CreateResultReplyMessage(Context context,
                                                              MySmsCommandMessage receivedMsg)
    {
        Resources res = context.getResources();

        //1: success, 0: partly success, -1 failed
        int result = receivedMsg.getSuccessfulCommands().isEmpty() ? -1 :
                (receivedMsg.getFailedCommands().isEmpty() ? 1 : 0);

        int titleRes = result == 1 ? R.string.result_msg_commands_exec_success
                : (result == 0 ? R.string.result_msg_commands_exec_partly_success
                    : R.string.result_msg_commands_exec_failed
                );

        String message = "rc-result " + res.getString(titleRes);

        if(result == 0)
        {
            message += res.getString(R.string.successful_commands_head) + "\r\n - ";
            message += StringUtils.join(receivedMsg.getSuccessfulCommands(), "\r\n - ");
            message += "\r\n";
            message += res.getString(R.string.failed_commands_head) + "\r\n - ";
            message += StringUtils.join(receivedMsg.getFailedCommands(), "\r\n - ");
        }

        return new MySmsSimpleMessage(receivedMsg.getPhoneNumber(), message);
    }
}
