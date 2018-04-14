package tranquvis.simplesmsremote.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tranquvis.simplesmsremote.Activities.MainActivity;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Sms.MyCommandMessage;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MyNotificationManager {
    private static final int CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED = 1;
    private static final int CODE_NOTIFICATION_CLICK_RECEIVER_START_FAILED_AFTER_BOOT = 2;
    private static final int CODE_NOTIFICATION_CLICK_PERMANENT_STATUS = 3;

    private static final int NOTIFICATION_ID_START_RECEIVER_AFTER_BOOT_FAILED = 1;

    private static MyNotificationManager ourInstance;

    private Context context;
    private NotificationManager nm;

    public static class DefaultNotificationChannel
    {
        static final String ID = "Default";
        static final String NAME = "Default";

        @RequiresApi(api = Build.VERSION_CODES.O)
        static NotificationChannel Create() {
            return new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH);
        }

        static void Init(NotificationManager manager)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                return;
            manager.createNotificationChannel(Create());
        }
    }

    public static class ReceiverNotificationChannel
    {
        static final String ID = "Receiver";
        static final String NAME = "Sms Receiver";

        @RequiresApi(api = Build.VERSION_CODES.O)
        static NotificationChannel Create() {
            return new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_MIN);
        }

        static void Init(NotificationManager manager)
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                return;
            manager.createNotificationChannel(Create());
        }
    }

    private MyNotificationManager(Context context) {
        this.context = context;
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static MyNotificationManager getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new MyNotificationManager(context);
        return ourInstance;
    }

    public void initChannels()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;
        DefaultNotificationChannel.Init(nm);
        ReceiverNotificationChannel.Init(nm);
    }

    private int getNextNotificationId(){
        final int startId = 100;
        return new Random().nextInt(Integer.MAX_VALUE - startId) + startId;
    }

    public void notifySmsCommandsExecuted(MyCommandMessage commandMessage,
                                          List<CommandExecResult> executionResults) {
        final Resources res = context.getResources();

        final String tag = "SmsCommandsReceived";
        final String title = res.getString(R.string.notification_sms_command_received);


        List<String> resultMessages = new ArrayList<>();
        for (CommandExecResult execResult : executionResults) {
            if (execResult.getCustomResultMessage() != null) {
                resultMessages.add("[info] " + execResult.getCustomResultMessage());
            } else if (execResult.isSuccess()) {
                resultMessages.add("[success] " + execResult.getCommandInstance().getCommandText());
            } else {
                resultMessages.add("[failed] " + execResult.getCommandInstance().getCommandText());
            }
        }

        String text = StringUtils.join(resultMessages, "\r\n");

        DefaultNotificationChannel.Init(nm);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                DefaultNotificationChannel.ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(res.getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED,
                        new Intent(context, MainActivity.class), 0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text)
                        .setBigContentTitle(title));

        nm.notify(tag, getNextNotificationId(), builder.build());
    }

    public void notifyStartReceiverAfterBootFailed() {
        final Resources res = context.getResources();

        final String tag = "StartSmsReceiverAfterBootFailed";
        final String title = res.getString(R.string.notification_title_start_receiver_after_boot_failed);
        String text = res.getString(R.string.notification_content_start_receiver_after_boot_failed);

        DefaultNotificationChannel.Init(nm);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                DefaultNotificationChannel.ID)
                .setDefaults(0)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(res.getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_RECEIVER_START_FAILED_AFTER_BOOT,
                        new Intent(context, MainActivity.class), 0));

        nm.notify(tag, NOTIFICATION_ID_START_RECEIVER_AFTER_BOOT_FAILED, builder.build());
    }

    public Notification getPermanentStatusNotification() {
        final Resources res = context.getResources();

        final String title = res.getString(R.string.notification_title_permanent_status);
        String text = res.getString(R.string.notification_content_permanent_status);

        ReceiverNotificationChannel.Init(nm);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                ReceiverNotificationChannel.ID)
                .setDefaults(0)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(res.getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_PERMANENT_STATUS,
                        new Intent(context, MainActivity.class), 0));

        return builder.build();
    }
}
