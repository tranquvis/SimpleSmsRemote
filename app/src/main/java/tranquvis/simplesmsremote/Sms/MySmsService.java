package tranquvis.simplesmsremote.Sms;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MySmsService {
    private static final String CODE_SENT = "SMS_SENT";
    private static final String CODE_DELIVERED = "SMS_DELIVERED";

    private Context context;
    private SmsServiceListener smsServiceListener;

    public MySmsService(Context context) {
        this.context = context;
    }

    public void setSmsServiceListener(SmsServiceListener smsServiceListener) {
        this.smsServiceListener = smsServiceListener;
    }

    public void sendSMS(final MyMessage sms) {
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(CODE_SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(CODE_DELIVERED), 0);

        //TODO set timeout
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (smsServiceListener != null)
                    smsServiceListener.OnSmsSent(sms, getResultCode());
            }
        }, new IntentFilter(CODE_SENT));

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if (smsServiceListener != null)
                    smsServiceListener.OnSmsDelivered(sms, getResultCode());
            }
        }, new IntentFilter(CODE_DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.getPhoneNumber(), null, sms.getMessage(), sentPI,
                deliveredPI);
    }
}
