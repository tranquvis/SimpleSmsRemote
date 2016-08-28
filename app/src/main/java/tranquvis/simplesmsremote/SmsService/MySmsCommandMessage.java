package tranquvis.simplesmsremote.SmsService;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MySmsCommandMessage implements MySms
{
    private static final String KEY = "smsremote";
    private String phoneNumber;
    private List<ControlAction> controlActions = new ArrayList<>();

    public MySmsCommandMessage(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void addControlAction(ControlAction controlAction)
    {
        controlActions.add(controlAction);
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
        for (ControlAction action : controlActions)
        {
            message += " " + action.getId();
        }
        return message;
    }

    public List<ControlAction> getControlActions()
    {
        return controlActions;
    }

    /**
     * creates MySmsCommandMessage from common SmsMessage
     * @param smsMessage SmsMessage, containing the sms information
     * @return command message or null if the message doesn't use the required syntax
     */
    public static MySmsCommandMessage CreateFromSmsMessage(SmsMessage smsMessage)
    {
        String messageContent = smsMessage.getMessageBody();
        String messageOriginPhoneNumber = smsMessage.getDisplayOriginatingAddress();

        MySmsCommandMessage commandMessage = new MySmsCommandMessage(messageOriginPhoneNumber);

        //check key
        if(!messageContent.substring(0, KEY.length()).equals(KEY))
            return null;

        //retrieve control actions
        String controlActionsStr = messageContent.substring(KEY.length(),
                messageContent.length()).trim();
        String[] controlActionIds = controlActionsStr.split("[ ]{1,}");
        for (String actionId : controlActionIds)
        {
            if(actionId.length() != 0)
            {
                ControlAction controlAction = ControlAction.getFromId(actionId);
                if(controlAction != null)
                    commandMessage.addControlAction(controlAction);
            }
        }
        if(commandMessage.controlActions.size() == 0)
            return null;

        return commandMessage;
    }
}
