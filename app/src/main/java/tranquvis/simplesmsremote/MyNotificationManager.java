package tranquvis.simplesmsremote;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import tranquvis.simplesmsremote.Activities.MainActivity;
import tranquvis.simplesmsremote.Services.Sms.MySmsCommandMessage;

/**
 * Created by Andreas Kaltenleitner on 24.08.2016.
 */
public class MyNotificationManager
{
    private static MyNotificationManager ourInstance;

    public static MyNotificationManager getInstance(Context context)
    {
        if (ourInstance == null)
            ourInstance = new MyNotificationManager(context);
        return ourInstance;
    }

    private MyNotificationManager(Context context)
    {
        this.context = context;
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Context context;
    private NotificationManager nm;

    private static final int CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED = 1;
    private static final int CODE_NOTIFICATION_CLICK_RECEIVER_START_FAILED_AFTER_BOOT = 2;
    private static final int CODE_NOTIFICATION_CLICK_PERMANENT_STATUS = 3;

    public void notifySmsCommandsExecuted(MySmsCommandMessage commandMessage,
                                          List<ControlCommand.ExecutionResult> executionResults)
    {
        final Resources res = context.getResources();

        final String tag = "SmsCommandsReceived";
        final String title = res.getString(R.string.notification_sms_command_received);


        List<String> resultMessages = new ArrayList<>();
        for (ControlCommand.ExecutionResult execResult : executionResults)
        {
            if(execResult.getCustomResultMessage() != null)
            {
                resultMessages.add("[info] " + execResult.getCustomResultMessage());
            }
            else if(execResult.isSuccess())
            {
                resultMessages.add("[success] " + execResult.getCommand().toString());
            }
            else
            {
                resultMessages.add("[failed] " + execResult.getCommand().toString());
            }
        }

        String text = StringUtils.join(resultMessages, "\r\n");

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setColor(res.getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED,
                        new Intent(context, MainActivity.class), 0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text)
                        .setBigContentTitle(title));

        notify(builder.build(), tag);
    }

    public void notifyStartReceiverAfterBootFailed()
    {
        final Resources res = context.getResources();

        final String tag = "StartSmsReceiverAfterBootFailed";
        final String title = res.getString(R.string.notification_title_start_receiver_after_boot_failed);
        String text = res.getString(R.string.notification_content_start_receiver_after_boot_failed);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(0)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_RECEIVER_START_FAILED_AFTER_BOOT,
                        new Intent(context, MainActivity.class), 0));

        notify(builder.build(), tag);
    }

    public Notification PermanentStatusNotification()
    {
        final Resources res = context.getResources();

        final String title = res.getString(R.string.notification_title_permanent_status);
        String text = res.getString(R.string.notification_content_permanent_status);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(0)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_PERMANENT_STATUS,
                        new Intent(context, MainActivity.class), 0));

        return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notify(final Notification notification,
                        final String notificationTag)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
        {
            nm.notify(notificationTag, 0, notification);
        }
        else
        {
            nm.notify(notificationTag.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications with notificationTag
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void cancel(final String notificationTag)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
        {
            nm.cancel(notificationTag, 0);
        }
        else
        {
            nm.cancel(notificationTag.hashCode());
        }
    }
}
