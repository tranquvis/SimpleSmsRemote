package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.MobileDataUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandGetMobileDataState extends Command {
    @Language("RegExp")
    private static final String
            PATTERN_ROOT = GetPatternFromTemplate(PATTERN_TEMPLATE_GET_STATE_ON_OFF,
                "((mobile\\s+data)|(mobile\\s+internet))(\\s+connection)?");

    public CommandGetMobileDataState(@Nullable Module module)
    {
        super(module);

        this.titleRes = R.string.command_title_get_mobile_data_state;
        this.syntaxDescList =  new String[]{
                "is mobile data enabled"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.DO_NOT_MATCH
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        boolean isMobileDataEnabled = MobileDataUtils.IsMobileDataEnabled(context);
        result.setCustomResultMessage(context.getString(
                isMobileDataEnabled ? R.string.result_msg_mobile_data_is_enabled_true
                        : R.string.result_msg_mobile_data_is_enabled_false));
        result.setForceSendingResultSmsMessage(true);
    }
}
