package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamOnOff;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.WifiUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandSetWifiState extends Command {
    static final CommandParamOnOff PARAM_WIFI_STATE = new CommandParamOnOff("wifi_state");


    private static final String PATTERN_ROOT =
            GetPatternFromTemplate(PATTERN_TEMPLATE_SET_STATE_ON_OFF, "wlan|wifi");

    public CommandSetWifiState(@Nullable Module module) {
        super(module);

        this.titleRes = R.string.command_title_set_wifi_state;
        this.syntaxDescList = new String[]{
                "enable wifi",
                "disable wifi"
        };
        this.patternTree = new PatternTreeNode(PARAM_WIFI_STATE.getId(),
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        WifiUtils.SetWifiState(context, commandInstance.getParam(PARAM_WIFI_STATE));
    }
}
