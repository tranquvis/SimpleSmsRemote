package tranquvis.simplesmsremote.Services.Sms;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;

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

    public static MySmsSimpleMessage CreateResultReplyMessage(
            Context context, MySmsCommandMessage receivedMsg,
            List<ControlCommand.ExecutionResult> executionResults)
    {
        List<String> resultMessages = new ArrayList<>();

        for (ControlCommand.ExecutionResult execResult : executionResults)
        {
            if(execResult.getCustomResultMessage() != null)
            {
                resultMessages.add("[info] " + execResult.getCustomResultMessage());
            }
            else if(execResult.isSuccess())
            {
                resultMessages.add("[success] " + execResult.getCommand().toString());
            }
            else
            {
                resultMessages.add("[failed] " + execResult.getCommand().toString());
            }
        }

        String text = StringUtils.join(resultMessages, "\r\n");
        String message = "rc-result\r\n" + text;

        return new MySmsSimpleMessage(receivedMsg.getPhoneNumber(), message);
    }
}
