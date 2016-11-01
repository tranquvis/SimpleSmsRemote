package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import org.intellij.lang.annotations.Language;

import tranquvis.simplesmsremote.CommandManagement.Commands.Command;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamNumber;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.DisplayUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandSetDisplayBrightness extends Command
{
    static final CommandParamNumber PARAM_BRIGHTNESS_VALUE =
            new CommandParamNumber("brightness_value");
    static final CommandParamBrightnessMode PARAM_BRIGHTNESS_MODE =
            new CommandParamBrightnessMode("brightness_mode");

    @Language("RegExp")
    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "set (?:(?:(?:(?:display|screen) )?brightness)" +
            "|(?:brightness of (?:display|screen))) to (.*)");
    @Language("RegExp")
    private static final String PATTERN_BRIGHTNESS_VALUE = "[0-9.,]+(%)?";
    @Language("RegExp")
    private static final String PATTERN_BRIGHTNESS_MODE = "(?i)auto";

    public CommandSetDisplayBrightness(@NonNull Module module)
    {
        super(module);

        this.titleRes = R.string.command_title_set_display_brightness;
        this.syntaxDescList = new String[]{
                "set brightness to [brightness]"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_CHILD_PATTERN_STRICT,
                new PatternTreeNode(PARAM_BRIGHTNESS_VALUE.getId(),
                        PATTERN_BRIGHTNESS_VALUE,
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_BRIGHTNESS_MODE.getId(),
                        PATTERN_BRIGHTNESS_MODE,
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception
    {
        DisplayUtils.BrightnessMode brightnessMode =
                commandInstance.getParam(PARAM_BRIGHTNESS_MODE);
        if(brightnessMode != null)
        {
            DisplayUtils.SetBrightnessMode(context, brightnessMode);
        }
        else
        {
            float brightnessValue = commandInstance.getParam(PARAM_BRIGHTNESS_VALUE).floatValue();
            DisplayUtils.SetBrightness(context, brightnessValue);
        }
    }

    private static class CommandParamBrightnessMode
            extends CommandParam<DisplayUtils.BrightnessMode>
    {

        private CommandParamBrightnessMode(String id)
        {
            super(id);
        }

        @Override
        public DisplayUtils.BrightnessMode getValueFromInput(String input) throws Exception
        {
            if(input.matches("auto"))
                return DisplayUtils.BrightnessMode.AUTO;
            throw new IllegalArgumentException("unexpected input");
        }
    }
}