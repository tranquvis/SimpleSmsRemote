package tranquvis.simplesmsremote.Sms;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;

/**
 * Created by Andreas Kaltenleitner on 02.09.2016.
 */
public class MySimpleMessage implements MyMessage {
    private String phoneNumber;
    private String message;

    public MySimpleMessage(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public static MySimpleMessage CreateResultReplyMessage(
            Context context, MyCommandMessage receivedMsg,
            List<CommandExecResult> executionResults, boolean replyWithDefaultResult) {
        List<String> resultMessages = new ArrayList<>();

        for (CommandExecResult execResult : executionResults) {
            if (execResult.getCustomResultMessage() != null) {
                resultMessages.add("(info) " + execResult.getCustomResultMessage());
            } else if (replyWithDefaultResult) {
                if (execResult.isSuccess()) {
                    resultMessages.add("(success) " + context.getString(
                            execResult.getCommandInstance().getCommand().getTitleRes()));
                } else {
                    resultMessages.add("(failed) " + context.getString(
                            execResult.getCommandInstance().getCommand().getTitleRes()));
                }
            }
        }

        String text = StringUtils.join(resultMessages, "\r\n");
        String message = "rc-result\r\n" + text;

        return new MySimpleMessage(receivedMsg.getPhoneNumber(), message);
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
