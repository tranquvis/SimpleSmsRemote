package tranquvis.simplesmsremote.ReceiverService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.SMSReceiver;

public class SMSReceiverService extends Service
{
    private static ReceiverStatus status = ReceiverStatus.NOT_STARTED;

    private SMSReceiver smsReceiver = new SMSReceiver(this);

    public SMSReceiverService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        switch (status)
        {
            case NOT_STARTED:
                Toast.makeText(this, R.string.receiver_started, Toast.LENGTH_SHORT).show();
                registerSMSReceiver();
                break;
            case RUNNING:
                break;
        }
        status = ReceiverStatus.RUNNING;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        unregisterSMSReceiver();
        Toast.makeText(this, R.string.receiver_stopped, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void registerSMSReceiver()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(2147483647);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    private void unregisterSMSReceiver()
    {
        unregisterReceiver(smsReceiver);
    }

    public static void start(Context context)
    {
        context.startService(new Intent(context, SMSReceiverService.class));
    }

    public static void stop(Context context)
    {
        context.stopService(new Intent(context, SMSReceiverService.class));
    }

    public static boolean isRunning()
    {
        return status == ReceiverStatus.RUNNING;
    }
}