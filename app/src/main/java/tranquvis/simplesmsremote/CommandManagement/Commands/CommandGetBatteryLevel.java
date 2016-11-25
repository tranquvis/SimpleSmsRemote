package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.BatteryUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class CommandGetBatteryLevel extends Command {

    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(get|fetch|retrieve) (((battery|charge) (level|value))|(battery charge))");

    public CommandGetBatteryLevel(@Nullable Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_battery_level;
        this.syntaxDescList = new String[]{
                "get battery level"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }


    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception {
        float batteryLevel = BatteryUtils.GetBatteryLevel(context);
        result.setCustomResultMessage(context.getResources().getString(
                R.string.result_msg_battery_level, batteryLevel * 100));
        result.setForceSendingResultSmsMessage(true);
    }
}
