package tranquvis.simplesmsremote.ReceiverService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tranquvis.simplesmsremote.R;

public class SMSReceiverService extends Service
{
    public static final String PREFERENCE_FILENAME = "sms_receiver_service_pref";

    private SMSReceiver smsReceiver = new SMSReceiver(this);

    public SMSReceiverService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        switch (getStatus(this))
        {
            case NOT_STARTED:
            case Stopped:
                Toast.makeText(this, R.string.receiver_started, Toast.LENGTH_SHORT).show();
                registerSMSReceiver();
                break;
            case RUNNING:
                break;
        }

        setStatus(this, ReceiverStatus.RUNNING);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        try {
            unregisterSMSReceiver();
        }
        catch (Exception e)
        {

        }
        Toast.makeText(this, R.string.receiver_stopped, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void registerSMSReceiver()
    {
        setStartTime(this, Calendar.getInstance().getTime());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(2147483647);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    private void unregisterSMSReceiver()
    {
        unregisterReceiver(smsReceiver);
    }

    private static void setStatus(Context context, ReceiverStatus status)
    {
        SharedPreferences preferencesWriter = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        preferencesWriter.edit().putString("status", status.name()).commit();
    }

    private static void setStartTime(Context context, Date time)
    {
        SharedPreferences preferencesWriter = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        preferencesWriter.edit().putString("time", String.valueOf(time.getTime())).commit();
    }

    public static void start(Context context)
    {
        context.startService(new Intent(context, SMSReceiverService.class));
    }

    public static void stop(Context context)
    {
        setStatus(context, ReceiverStatus.Stopped);
        context.stopService(new Intent(context, SMSReceiverService.class));
    }

    public static boolean isRunning(Context context)
    {
        return getStatus(context) == ReceiverStatus.RUNNING;
    }

    public static Date getStartTime(Context context)
    {
        SharedPreferences preferencesReader = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        try{
            return new Date(Long.parseLong(preferencesReader.getString("time", "")));
        }
        catch (NumberFormatException e)
        {
            setStartTime(context, Calendar.getInstance().getTime());
            return getStartTime(context);
        }
    }

    public static ReceiverStatus getStatus(Context context)
    {
        SharedPreferences preferencesReader = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        return ReceiverStatus.valueOf(preferencesReader.getString("status",
                ReceiverStatus.NOT_STARTED.name()));
    }
}