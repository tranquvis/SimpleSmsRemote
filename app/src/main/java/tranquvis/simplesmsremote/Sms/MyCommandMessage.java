package tranquvis.simplesmsremote.Sms;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandInstance;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MyCommandMessage implements MyMessage {
    private static final String KEY = "rc ";
    private String phoneNumber;
    private List<CommandInstance> commandInstances = new ArrayList<>();

    public MyCommandMessage(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * creates MyCommandMessage from common SmsMessage
     *
     * @param smsMessage SmsMessage, containing the sms information
     * @return command message or null if the message doesn't use the required syntax
     */
    public static MyCommandMessage CreateFromSmsMessage(SmsMessage smsMessage) {
        String messageContent = smsMessage.getMessageBody().trim();
        String messageOriginPhoneNumber = smsMessage.getDisplayOriginatingAddress();

        MyCommandMessage commandMessage = new MyCommandMessage(messageOriginPhoneNumber);

        //check key
        if (messageContent.length() < KEY.length()
                || !messageContent.substring(0, KEY.length()).toLowerCase()
                .equals(KEY.toLowerCase()))
            return null;

        //retrieve control actions
        String controlCommandsStr = messageContent.substring(KEY.length(),
                messageContent.length()).trim();
        String[] commandStrings = controlCommandsStr.split(";");
        for (String commandStr : commandStrings) {
            if (commandStr.length() != 0) {
                try {
                    CommandInstance commandInstance = CommandInstance.CreateFromCommand(commandStr);
                    if (commandInstance != null)
                        commandMessage.addCommandInstance(commandInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (commandMessage.commandInstances.size() == 0)
            return null;

        return commandMessage;
    }

    public void addCommandInstance(CommandInstance commandInstance) {
        commandInstances.add(commandInstance);
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getMessage() {
        String message = KEY;
        for (CommandInstance commandInstance : commandInstances) {
            message += " " + commandInstance.getCommandText();
        }
        return message;
    }

    public List<CommandInstance> getCommandInstances() {
        return commandInstances;
    }
}
