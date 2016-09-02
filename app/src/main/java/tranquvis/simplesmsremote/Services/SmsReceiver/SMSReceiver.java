package tranquvis.simplesmsremote.Services.SmsReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.MyNotificationManager;
import tranquvis.simplesmsremote.Services.Sms.MySmsCommandMessage;

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

                    MySmsCommandMessage comMsg = MySmsCommandMessage.CreateFromSmsMessage(smsMessage);
                    if(comMsg == null)
                        return;

                    DataManager.LoadUserData(context);

                    List<ControlCommand> failedCommands = new ArrayList<>();
                    //execute commands
                    for(ControlCommand command : comMsg.getControlCommands())
                    {
                        if(!command.execute(context, comMsg))
                            failedCommands.add(command);
                    }

                    if(DataManager.getUserData().getUserSettings().isNotifyCommandsExecuted())
                        MyNotificationManager.getInstance(context).notifySmsCommandsReceived(comMsg,
                                failedCommands);
                }
                catch(Exception e)
                {
                    DataManager.addLogEntry(LogEntry.Predefined.SmsProcessingFailed(context),
                            context);
                }
            }
        }
    }
}
