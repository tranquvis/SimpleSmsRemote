package tranquvis.simplesmsremote.Services.SmsReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.MyNotificationManager;
import tranquvis.simplesmsremote.Services.Sms.MySms;
import tranquvis.simplesmsremote.Services.Sms.MySmsCommandMessage;
import tranquvis.simplesmsremote.Services.Sms.MySmsService;
import tranquvis.simplesmsremote.Services.Sms.MySmsSimpleMessage;
import tranquvis.simplesmsremote.Services.Sms.SmsServiceListener;

public class SMSReceiver extends BroadcastReceiver
{
    SMSReceiverService smsReceiverService;

    public SMSReceiver(SMSReceiverService smsReceiverService)
    {
        this.smsReceiverService = smsReceiverService;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle = intent.getExtras(); //get the SMS message passed in
            SmsMessage smsMessage;
            MySmsCommandMessage comMsg;
            if (bundle != null){
                //retrieve the SMS message received
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if(pdus == null || pdus.length == 0)
                        return;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                        smsMessage = SmsMessage.createFromPdu((byte[])pdus[0],
                                (String)bundle.get("format"));
                    else
                        smsMessage = SmsMessage.createFromPdu((byte[])pdus[0]);

                    comMsg = MySmsCommandMessage.CreateFromSmsMessage(smsMessage);
                    if(comMsg == null)
                        return;

                    DataManager.LoadUserData(context);

                    //execute commands
                    List<ControlCommand> failedCommands = new ArrayList<>();
                    for(ControlCommand command : comMsg.getControlCommands())
                    {
                        if(!command.execute(context, comMsg))
                            failedCommands.add(command);
                    }
                    comMsg.setFailedCommands(failedCommands);

                    if(DataManager.getUserData().getUserSettings().isNotifyCommandsExecuted())
                        MyNotificationManager.getInstance(context).notifySmsCommandsReceived(comMsg);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    DataManager.addLogEntry(LogEntry.Predefined.SmsProcessingFailed(context),
                            context);
                    return;
                }

                try
                {
                    if (DataManager.getUserData().getUserSettings().isReplyWithResult())
                    {
                        MySmsService smsService = new MySmsService(context, new SmsServiceListener()
                        {
                            @Override
                            public void OnSmsSent(MySms sms, int resultCode)
                            {
                                Log.i("ExecReplyMessage", "sms sent");
                            }

                            @Override
                            public void OnSmsDelivered(MySms sms, int resultCode)
                            {
                                Log.i("ExecReplyMessage", "sms delivered");
                            }
                        });
                        smsService.sendSMS(MySmsSimpleMessage.CreateResultReplyMessage(
                                context, comMsg));
                        DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultTrySending(
                                context, comMsg.getPhoneNumber()), context);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    DataManager.addLogEntry(LogEntry.Predefined.ReplyExecResultFailedUnexpected(
                            context), context);
                }
            }
        }
    }
}
