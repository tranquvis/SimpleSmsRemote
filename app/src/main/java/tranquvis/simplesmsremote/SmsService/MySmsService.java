package tranquvis.simplesmsremote.SmsService;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.*;
import android.widget.Toast;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MySmsService
{
    private static final String CODE_SENT = "SMS_SENT";
    private static final String CODE_DELIVERED = "SMS_DELIVERED";

    private Context context;
    private final SmsServiceListener smsServiceListener;

    public MySmsService(Context context, SmsServiceListener smsServiceListener)
    {
        this.context = context;
        this.smsServiceListener = smsServiceListener;
    }

    public void sendSMS(final MySms sms)
    {
        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sms.getPhoneNumber(), null, sms.getMessage(), null, null);
            Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

        /*
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(CODE_SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(CODE_DELIVERED), 0);

        context.registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                smsServiceListener.OnSmsSent(sms, getResultCode());
            }
        }, new IntentFilter(CODE_SENT));

        context.registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                smsServiceListener.OnSmsDelivered(sms, getResultCode());
            }
        }, new IntentFilter(CODE_DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sms.getPhoneNumber(), null, sms.getMessage(), sentPI,
                deliveredPI);
                */
    }
}
