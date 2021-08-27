package tranquvis.simplesmsremote.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tranquvis.simplesmsremote.Services.SMSCommandHandlerService;
import tranquvis.simplesmsremote.Services.SMSReceiverService;

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
