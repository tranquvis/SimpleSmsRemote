package tranquvis.simplesmsremote.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import tranquvis.simplesmsremote.Data.AppDataManager;
import tranquvis.simplesmsremote.Data.LogEntry;
import tranquvis.simplesmsremote.Helper.MyNotificationManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Receiver.SMSReceiver;

public class SMSReceiverService extends Service {
    private static final String PREFERENCE_FILENAME = "sms_receiver_service_pref";
    private static final int ID = 986789;

    private static final String EXTRA_START_FOREGROUND = "start_foreground";

    private static boolean RUNNING;

    private final String TAG = getClass().getName();

    private SMSReceiver smsReceiver = new SMSReceiver(this);

    public SMSReceiverService() {
    }

    private static void setStartTime(Context context, Date time) {
        SharedPreferences preferencesWriter = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        preferencesWriter.edit().putString("time", String.valueOf(time.getTime())).commit();
    }

    /**
     * @param context    app context
     * @param foreground unused so far
     */
    public static void start(Context context, boolean foreground) {
        Intent intent = new Intent(context, SMSReceiverService.class);
        intent.putExtra(EXTRA_START_FOREGROUND, true); //always start foreground
        context.startService(intent);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, SMSReceiverService.class));
    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SMSReceiverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Date getStartTime(Context context) {
        SharedPreferences preferencesReader = context.getSharedPreferences(PREFERENCE_FILENAME,
                Context.MODE_MULTI_PROCESS);
        try {
            return new Date(Long.parseLong(preferencesReader.getString("time", "")));
        } catch (NumberFormatException e) {
            setStartTime(context, Calendar.getInstance().getTime());
            return getStartTime(context);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!RUNNING) {
            Toast.makeText(this, R.string.receiver_started, Toast.LENGTH_SHORT).show();

            if (intent != null && intent.hasExtra(EXTRA_START_FOREGROUND))
                startForeground(ID,
                        MyNotificationManager.getInstance(this).getPermanentStatusNotification());
            registerSMSReceiver();

            RUNNING = true;
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterSMSReceiver();
        } catch (Exception ignored) {
        }

        Log.i(TAG, getString(R.string.receiver_stopped));
        RUNNING = false;
        Toast.makeText(this, R.string.receiver_stopped, Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    private void registerSMSReceiver() {
        setStartTime(this, Calendar.getInstance().getTime());
        AppDataManager.getDefault().tryAddLogEntry(
                LogEntry.Predefined.SmsReceiverStarted(this), this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(2147483647);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    private void unregisterSMSReceiver() {
        unregisterReceiver(smsReceiver);
        AppDataManager.getDefault().tryAddLogEntry(
                LogEntry.Predefined.SmsReceiverStopped(this), this);
    }
}