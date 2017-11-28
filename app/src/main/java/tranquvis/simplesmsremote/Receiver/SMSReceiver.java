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
import tranquvis.simplesmsremote.Services.SMSCommandHandlerService;
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
        if(intent.getAction() == null || !intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            return;

        SMSCommandHandlerService.start(context, intent);
    }
}
