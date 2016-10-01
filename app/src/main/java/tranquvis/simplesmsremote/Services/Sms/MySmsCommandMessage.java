package tranquvis.simplesmsremote.Services.Sms;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MySmsCommandMessage implements MySms
{
    private static final String KEY = "rc ";
    private String phoneNumber;
    private List<ControlCommand> controlCommands = new ArrayList<>();

    public MySmsCommandMessage(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void addControlCommand(ControlCommand controlCommand)
    {
        controlCommands.add(controlCommand);
    }

    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    @Override
    public String getMessage()
    {
        String message = KEY;
        for (ControlCommand command : controlCommands)
        {
            message += " " + command.toString();
        }
        return message;
    }

    public List<ControlCommand> getControlCommands()
    {
        return controlCommands;
    }

    /**
     * creates MySmsCommandMessage from common SmsMessage
     * @param smsMessage SmsMessage, containing the sms information
     * @return command message or null if the message doesn't use the required syntax
     */
    public static MySmsCommandMessage CreateFromSmsMessage(SmsMessage smsMessage)
    {
        String messageContent = smsMessage.getMessageBody().trim();
        String messageOriginPhoneNumber = smsMessage.getDisplayOriginatingAddress();

        MySmsCommandMessage commandMessage = new MySmsCommandMessage(messageOriginPhoneNumber);

        //check key
        if(messageContent.length() < KEY.length()
                || !messageContent.substring(0, KEY.length()).toLowerCase()
                .equals(KEY.toLowerCase()))
            return null;

        //retrieve control actions
        String controlCommandsStr = messageContent.substring(KEY.length(),
                messageContent.length()).trim();
        String[] commandStrings = controlCommandsStr.split(",");
        for (String commandStr : commandStrings)
        {
            if(commandStr.length() != 0)
            {
                ControlCommand controlCommand = ControlCommand.getFromCommand(commandStr);
                if(controlCommand != null)
                    commandMessage.addControlCommand(controlCommand);
            }
        }
        if(commandMessage.controlCommands.size() == 0)
            return null;

        return commandMessage;
    }
}
