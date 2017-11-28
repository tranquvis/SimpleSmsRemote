package tranquvis.simplesmsremote.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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
import tranquvis.simplesmsremote.Sms.MyCommandMessage;
import tranquvis.simplesmsremote.Sms.MyMessage;
import tranquvis.simplesmsremote.Sms.MySimpleMessage;
import tranquvis.simplesmsremote.Sms.MySmsService;
import tranquvis.simplesmsremote.Sms.SmsServiceListener;

public class SMSCommandHandlerService extends IntentService {
    public SMSCommandHandlerService() {
        super("SMSCommandHandlerService");
    }

    public static void start(Context context, Intent smsIntent) {
        Intent intent = new Intent(context, SMSCommandHandlerService.class);
        intent.putExtras(smsIntent);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

                DataManager.LoadUserData(this);

                //execute commands
                executionResults = new ArrayList<>();
                for (CommandInstance command : comMsg.getCommandInstances()) {
                    CommandExecResult result = command.executeCommand(this, comMsg);
                    executionResults.add(result);
                    if (result.isForceSendingResultSmsMessage())
                        hasExecResultWithForceSendingResult = true;
                }

                if (DataManager.getUserData().getUserSettings().isNotifyCommandsExecuted())
                    MyNotificationManager.getInstance(this).notifySmsCommandsExecuted(
                            comMsg, executionResults);

            } catch (Exception e) {
                e.printStackTrace();
                DataManager.addLogEntry(LogEntry.Predefined.SmsProcessingFailed(this),
                        this);
                return;
            }

            try {
                boolean replyWithDefaultResult = DataManager.getUserData().getUserSettings().
                        isReplyWithResult();
                MySmsService smsService;
                if (replyWithDefaultResult || hasExecResultWithForceSendingResult) {
                    smsService = new MySmsService(this);
                    smsService.setSmsServiceListener(new SmsServiceListener() {
                        @Override
                        public void OnSmsSent(MyMessage sms, int resultCode) {
                            DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultSent(
                                    SMSCommandHandlerService.this, comMsg.getPhoneNumber()), SMSCommandHandlerService.this);
                        }

                        @Override
                        public void OnSmsDelivered(MyMessage sms, int resultCode) {
                            Log.i("ExecReplyMessage", "result sms delivered");
                        }
                    });

                    smsService.sendSMS(MySimpleMessage.CreateResultReplyMessage(
                            this, comMsg, executionResults, replyWithDefaultResult));
                }
            } catch (Exception e) {
                e.printStackTrace();
                DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultFailedUnexpected(
                        this), this);
            }
        }
    }
}
