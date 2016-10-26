package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.Command;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.WifiUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

class CommandGetWifiState extends Command {
    @Language("RegExp")
    private static final String
            PATTERN_ROOT = "^\\s*(((is\\s+)?(wifi|wlan)\\s+(enabled|disabled|on|off)(\\?)?)" +
                "|(get\\s+(wifi|wlan)\\s+state))\\s*$";

    CommandGetWifiState()
    {
        this.titleRes = R.string.command_title_get_wifi_state;
        this.syntaxDescList =  new String[]{
                "is wifi enabled"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        boolean isWifiEnabled = WifiUtils.IsWifiEnabled(context);

        // create result message
        result.setCustomResultMessage(context.getString(isWifiEnabled ? R.string.result_msg_wifi_is_enabled_true
                        : R.string.result_msg_wifi_is_enabled_false));
        result.setForceSendingResultSmsMessage(true);
    }
}
