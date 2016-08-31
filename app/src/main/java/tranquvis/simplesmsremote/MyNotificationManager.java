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

import java.util.List;

import tranquvis.simplesmsremote.Activities.MainActivity;
import tranquvis.simplesmsremote.SmsService.MySmsCommandMessage;

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

    Context context;
    NotificationManager nm;

    public static final int CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED = 1;

    public void notifySmsCommandsReceived(MySmsCommandMessage commandMessage,
                                          List<ControlCommand> failedCommands)
    {
        final Resources res = context.getResources();

        final String tag = "SmsCommandsReceived";
        final String ticker = res.getString(R.string.notification_sms_command_received);
        final String title = res.getString(R.string.notification_sms_command_received);

        String text = "";

        List<ControlCommand> successfulCommands = commandMessage.getControlCommands();
        successfulCommands.removeAll(failedCommands);
        if(!successfulCommands.isEmpty())
        {
            text += res.getString(R.string.successful_commands_head) + "\r\n";
            text += StringUtils.join(successfulCommands, "\r\n");
        }

        if(!failedCommands.isEmpty())
        {
            text += res.getString(R.string.failed_commands_head) + "\r\n";
            text += StringUtils.join(failedCommands, "\r\n");
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                //TODO set custom icon
                .setSmallIcon(R.drawable.ic_textsms_grey_700_36dp)
                .setContentTitle(title)
                .setContentText(text)
                //optional
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setTicker(ticker)
                .setContentIntent(PendingIntent.getActivity(context,
                        CODE_NOTIFICATION_CLICK_SMS_COMMAND_RECEIVED,
                        new Intent(context, MainActivity.class), 0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text)
                        .setBigContentTitle(title));

        notify(builder.build(), tag);
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
