package tranquvis.simplesmsremote.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.Services.SMSReceiverService;
import tranquvis.simplesmsremote.Sms.MyCommandMessage;
import tranquvis.simplesmsremote.Sms.MyMessage;
import tranquvis.simplesmsremote.Sms.MySimpleMessage;
import tranquvis.simplesmsremote.Sms.MySmsService;
import tranquvis.simplesmsremote.Sms.SmsServiceListener;

public class SMSReceiver extends BroadcastReceiver {
    private SMSReceiverService smsReceiverService;

    public SMSReceiver(SMSReceiverService smsReceiverService) {
        this.smsReceiverService = smsReceiverService;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras(); //get the SMS message passed in
            SmsMessage smsMessage;
            final MyCommandMessage comMsg;
            List<CommandExecResult> executionResults;
            boolean hasExecResultWithForceSendingResult = false;
            if (bundle != null) {
                //retrieve the SMS message received
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus == null || pdus.length == 0)
                        return;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0],
                                (String) bundle.get("format"));
                    else
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);

                    comMsg = MyCommandMessage.CreateFromSmsMessage(smsMessage);
                    if (comMsg == null)
                        return;

                    DataManager.LoadUserData(context);

                    //execute commands
                    executionResults = new ArrayList<>();
                    for (CommandInstance command : comMsg.getCommandInstances()) {
                        CommandExecResult result = command.executeCommand(context, comMsg);
                        executionResults.add(result);
                        if (result.isForceSendingResultSmsMessage())
                            hasExecResultWithForceSendingResult = true;
                    }

                    if (DataManager.getUserData().getUserSettings().isNotifyCommandsExecuted())
                        MyNotificationManager.getInstance(context).notifySmsCommandsExecuted(
                                comMsg, executionResults);

                } catch (Exception e) {
                    e.printStackTrace();
                    DataManager.addLogEntry(LogEntry.Predefined.SmsProcessingFailed(context),
                            context);
                    return;
                }

                try {
                    boolean replyWithDefaultResult = DataManager.getUserData().getUserSettings().
                            isReplyWithResult();
                    MySmsService smsService;
                    if (replyWithDefaultResult || hasExecResultWithForceSendingResult) {
                        smsService = new MySmsService(context);
                        smsService.setSmsServiceListener(new SmsServiceListener() {
                            @Override
                            public void OnSmsSent(MyMessage sms, int resultCode) {
                                DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultSent(
                                        context, comMsg.getPhoneNumber()), context);
                            }

                            @Override
                            public void OnSmsDelivered(MyMessage sms, int resultCode) {
                                Log.i("ExecReplyMessage", "result sms delivered");
                            }
                        });

                        smsService.sendSMS(MySimpleMessage.CreateResultReplyMessage(
                                context, comMsg, executionResults, replyWithDefaultResult));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultFailedUnexpected(
                            context), context);
                }
            }
        }
    }
}
