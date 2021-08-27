package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.BatteryUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class CommandGetBatteryStatus extends Command {

    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "is (((battery )?charging)|(battery loading))\\s*(\\?)?" +
                    "|(((battery )?charging)|(battery loading))\\s*\\?" +
                    "|get battery status");

    public CommandGetBatteryStatus(@Nullable Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_battery_status;
        this.syntaxDescList = new String[]{
                "is battery charging"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }


    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result, DataManager dataManager) throws Exception {
        boolean isBatteryCharging = BatteryUtils.IsBatteryCharging(context);
        result.setCustomResultMessage(context.getString(
                isBatteryCharging ? R.string.result_msg_battery_is_charging_true
                        : R.string.result_msg_battery_is_charging_false));
        result.setForceSendingResultSmsMessage(true);
    }
}
