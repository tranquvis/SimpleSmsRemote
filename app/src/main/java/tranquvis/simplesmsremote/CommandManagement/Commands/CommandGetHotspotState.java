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
 * Created by Andreas Kaltenleitner on 27.10.2016.
 */

public class CommandGetHotspotState extends Command
{
    @Language("RegExp")
    private static final String
            PATTERN_ROOT = GetPatternFromTemplate(PATTERN_TEMPLATE_GET_STATE_ON_OFF,
                "((wifi|wlan)\\s+)?hotspot");

    public CommandGetHotspotState()
    {
        this.titleRes = R.string.command_title_get_hotspot_state;
        this.syntaxDescList =  new String[]{
                "is hotspot enabled"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }
    @Override
    protected void execute(Context context, CommandInstance commandInstance,
                           CommandExecResult result) throws Exception
    {
        boolean isHotspotEnabled = WifiUtils.IsHotspotEnabled(context);
        result.setCustomResultMessage(context.getString(
                isHotspotEnabled ? R.string.result_msg_hotspot_is_enabled_true
                        : R.string.result_msg_hotspot_is_enabled_false));
        result.setForceSendingResultSmsMessage(true);
    }
}
