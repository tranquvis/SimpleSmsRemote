package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamNumber;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamUnit;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;
import tranquvis.simplesmsremote.Utils.UnitTools.Unit;
import tranquvis.simplesmsremote.Utils.UnitTools.UnitType;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandSetDisplayOffTimeout extends Command
{
    static final CommandParamNumber PARAM_TIMEOUT_VALUE =
            new CommandParamNumber("timeout_value");
    static final CommandParamUnit PARAM_TIMEOUT_UNIT = new CommandParamUnit("timeout_unit");

    @Language("RegExp")
    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "set (?:(?:display|screen) off timeout) to ([0-9.,]+)\\s*([a-z]+)");

    public CommandSetDisplayOffTimeout(@NonNull Module module)
    {
        super(module);

        this.titleRes = R.string.command_title_set_display_off_timeout;
        this.syntaxDescList = new String[]{
                "set display off timeout to [timeout]"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(PARAM_TIMEOUT_VALUE.getId(),
                        ".*",
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_TIMEOUT_UNIT.getId(),
                        Unit.GetFullPattern(UnitType.TIME),
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception
    {
        float timeoutValue = commandInstance.getParam(PARAM_TIMEOUT_VALUE).floatValue();
        Unit timeoutUnit = commandInstance.getParam(PARAM_TIMEOUT_UNIT);
        int timeoutMilliseconds = (int)(timeoutValue * timeoutUnit.getFactor() * 1000f);

        DisplayUtils.SetScreenOffTimeout(context, timeoutMilliseconds);
    }
}