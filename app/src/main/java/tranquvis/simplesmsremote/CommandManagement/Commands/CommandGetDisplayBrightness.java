package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

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
public class CommandGetDisplayBrightness extends Command {

    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(get|fetch|retrieve) ((((?:display|screen) )?brightness)" +
                    "|(brightness of (?:display|screen)))");

    public CommandGetDisplayBrightness(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_display_brightness;
        this.syntaxDescList = new String[]{
                "get brightness"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception {
        float brightnessPercentage = DisplayUtils.GetBrightness(context);
        DisplayUtils.BrightnessMode brightnessMode = DisplayUtils.GetBrightnessMode(context);
        String brightnessModeStr = brightnessMode == DisplayUtils.BrightnessMode.AUTO
                ? "auto" : "manual";

        result.setCustomResultMessage(context.getString(
                R.string.result_msg_display_brightness_percentage, brightnessPercentage,
                brightnessModeStr));
        result.setForceSendingResultSmsMessage(true);
    }
}