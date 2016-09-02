package tranquvis.simplesmsremote.Data;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import tranquvis.simplesmsremote.ControlCommand;
import tranquvis.simplesmsremote.R;

/**
 * Created by Andreas Kaltenleitner on 31.08.2016.
 */
public class LogEntry
{
    private String title;
    private String summary;
    private Date time;
    private Type type;

    public LogEntry(String title, String summary, Date time, Type type)
    {
        this.title = title;
        this.summary = summary;
        this.time = time;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSummary()
    {
        return summary;
    }

    public Date getTime()
    {
        return time;
    }

    public Type getType()
    {
        return type;
    }

    public String toTextLine()
    {
        return String.format("%1$s''%2$s''%3$d''%4$s", title, summary, time.getTime(), type.name());
    }

    public static LogEntry parseFromTextLine(String textLine)
    {
        try
        {
            String[] parts = textLine.split("''");
            String title = parts[0];
            String summary = parts[1];
            if(summary.length() == 0 || summary.equals("null"))
                summary = null;
            Date time = new Date(Long.parseLong(parts[2]));
            Type type = Type.valueOf(parts[3]);

            return new LogEntry(title, summary, time, type);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static class Predefined
    {
        public static LogEntry ComExecFailedPermissionDenied(Context context, ControlCommand command)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_failed),
                            command.toString()),
                    context.getString(R.string.log_summary_com_exec_failed_perm_denied),
                    Calendar.getInstance().getTime(), Type.Error
            );
        }

        public static LogEntry ComExecFailedPhoneNotGranted(Context context, ControlCommand command,
                                                            String phone)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_failed),
                            command.toString()),
                    String.format(context.getString(R.string.log_summary_com_exec_failed_phone_not_granted),
                            phone, context.getString(command.getModule().getTitleRes())),
                    Calendar.getInstance().getTime(), Type.Error
            );
        }

        public static LogEntry ComExecFailedPhoneIncompatible(Context context, ControlCommand command)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_failed),
                            command.toString()),
                    String.format(context.getString(
                            R.string.log_summary_com_exec_failed_phone_incompatible),
                            context.getString(command.getModule().getTitleRes())),
                    Calendar.getInstance().getTime(), Type.Error
            );
        }

        public static LogEntry ComExecFailedModuleDisabled(Context context, ControlCommand command)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_failed),
                            command.toString()),
                    String.format(context.getString(
                            R.string.log_summary_com_exec_failed_module_disabled),
                            context.getString(command.getModule().getTitleRes())),
                    Calendar.getInstance().getTime(), Type.Error
            );
        }

        public static LogEntry ComExecFailedUnexpected(Context context, ControlCommand command)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_failed),
                            command.toString()),
                    context.getString(R.string.log_summary_com_exec_failed_unexpected),
                    Calendar.getInstance().getTime(), Type.Error
            );
        }

        public static LogEntry ComExecSuccess(Context context, ControlCommand command)
        {
            return new LogEntry(
                    String.format(context.getString(R.string.log_title_com_exec_success),
                            command.toString()), null,
                    Calendar.getInstance().getTime(), Type.Success
            );
        }

        public static LogEntry SmsProcessingFailed(Context context)
        {
            return new LogEntry(context.getString(R.string.log_title_sms_processing_failed), null,
                    Calendar.getInstance().getTime(), Type.Error);
        }

        public static LogEntry SmsReceiverStarted(Context context)
        {
            return new LogEntry(context.getString(R.string.log_title_sms_receiver_started), null,
                    Calendar.getInstance().getTime(), Type.Info);
        }

        public static LogEntry SmsReceiverStopped(Context context)
        {
            return new LogEntry(context.getString(R.string.log_title_sms_receiver_stopped), null,
                    Calendar.getInstance().getTime(), Type.Info);
        }

        public static LogEntry AfterBootReceiverStartFailedUnexpected(Context context)
        {
            return new LogEntry(context.getString(R.string.log_title_after_boot_receiver_start_failed),
                    context.getString(R.string.log_summary_after_boot_receiver_start_failed_unexpected),
                    Calendar.getInstance().getTime(), Type.Error);
        }

        public static LogEntry ReplyExecResultFailedUnexpected(Context context)
        {
            return new LogEntry(context.getString(R.string.log_title_reply_exec_result_failed),
                    context.getString(R.string.log_summary_reply_exec_result_failed_unexpected),
                    Calendar.getInstance().getTime(), Type.Error);
        }

        public static LogEntry ReplyExecResultTrySending(Context context, String phone)
        {
            return new LogEntry(String.format(context.getString(
                    R.string.log_title_reply_exec_result_try_sending), phone),
                    null, Calendar.getInstance().getTime(), Type.Info);
        }
    }

    public enum Type
    {
        Error(R.color.colorError, R.drawable.ic_error_red_400_18dp),
        Success(R.color.colorSuccess, R.drawable.ic_check_circle_green_400_18dp),
        Info(R.color.colorInfo, R.drawable.ic_info_indigo_400_18dp);

        private int colorRes;
        private int iconRes;

        Type(int colorRes, int iconRes)
        {

            this.colorRes = colorRes;
            this.iconRes = iconRes;
        }

        public int getColorRes()
        {
            return colorRes;
        }

        public int getIconRes()
        {
            return iconRes;
        }
    }
}
