package tranquvis.simplesmsremote.ReceiverService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.ControlCommand;
import tranquvis.simplesmsremote.MyNotificationManager;
import tranquvis.simplesmsremote.ReceiverService.SMSReceiverService;
import tranquvis.simplesmsremote.SmsService.MySmsCommandMessage;

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

                    List<ControlCommand> failedCommands = new ArrayList<>();
                    for(ControlCommand command : comMsg.getControlCommands())
                    {
                        if(!command.execute(context))
                            failedCommands.add(command);
                    }

                    MyNotificationManager.getInstance(context).notifySmsCommandsReceived(comMsg,
                            failedCommands);
                }
                catch(Exception e)
                {
                    Log.e("sms receive error", "Error occurred while receiving SMS");
                    //TODO show Notification
                }
            }
        }
    }
}
