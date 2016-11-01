package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandGetDisplayOffTimeout extends Command
{
    @Language("RegExp")
    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(get|fetch|retrieve) (?:display|screen) off timeout");

    public CommandGetDisplayOffTimeout(@NonNull Module module)
    {
        super(module);

        this.titleRes = R.string.command_title_get_display_off_timeout;
        this.syntaxDescList = new String[]{
                "get display off timeout"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception
    {
        float screenOffTimeout = DisplayUtils.GetScreenOffTimeout(context);

        // retrieve most comfortable reading unit
        String timeoutStr;
        if(screenOffTimeout < 900)
            timeoutStr = String.format("%.0fms", screenOffTimeout);
        else if(screenOffTimeout < 60000)
            timeoutStr = String.format("%ds", Math.round(screenOffTimeout / 1000f));
        else
            timeoutStr = String.format("%.1fmin", screenOffTimeout / 60000f);

        result.setCustomResultMessage(context.getString(
                R.string.result_msg_display_off_timeout, timeoutStr));
        result.setForceSendingResultSmsMessage(true);
    }
}