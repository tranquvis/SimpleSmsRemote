package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.Nullable;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParam;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamAudioType;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamNumber;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.AudioUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 26.10.2016.
 */

public class CommandSetAudioVolume extends Command {
    static final CommandParamAudioType PARAM_AUDIO_TYPE = new CommandParamAudioType("audio_type");
    static final CommandParamNumber PARAM_VOLUME_VALUE = new CommandParamNumber("volume_value");
    static final CommandParamUnit PARAM_VOLUME_UNIT = new CommandParamUnit("volume_unit");
    static final CommandParamRingerMode PARAM_RINGER_MODE =
            new CommandParamRingerMode("ringer_mode");


    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "set (?:(?:volume (?:(index|percentage) )?(?:(?:of|for) )?(.*?))" +
                    "|(?:(.*?) volume(?: (index|percentage))?)) to ([^%]+?)\\s*(%)?");

    private static final String PATTERN_UNIT = "(?i)%|index|percentage";

    private static final String PATTERN_RINGER_MODE = "(?i)vibrate|vibration|silent|mute";

    private static final String PATTERN_VOLUME_VALUE = "[0-9.,]+";

    public CommandSetAudioVolume(@Nullable Module module) {
        super(module);

        this.titleRes = R.string.command_title_set_audio_volume;
        this.syntaxDescList = new String[]{
                "set [audio type] volume to [volume]",
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_CHILD_PATTERN_STRICT,
                new PatternTreeNode(PARAM_AUDIO_TYPE.getId(),
                        CommandParamAudioType.GetEntirePattern(),
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_VOLUME_VALUE.getId(),
                        PATTERN_VOLUME_VALUE,
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_VOLUME_UNIT.getId(),
                        PATTERN_UNIT,
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_RINGER_MODE.getId(),
                        PATTERN_RINGER_MODE,
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance, CommandExecResult result)
            throws Exception {
        // get params
        AudioUtils.AudioType audioType = commandInstance.getParam(PARAM_AUDIO_TYPE);
        Unit unit = commandInstance.getParam(PARAM_VOLUME_UNIT);
        Double value = commandInstance.getParam(PARAM_VOLUME_VALUE);
        if (value == null) {
            if (audioType != AudioUtils.AudioType.RING)
                throw new Exception("Ringer modes like silent or vibrate can only be set for audio type ring.");
            value = commandInstance.getParam(PARAM_RINGER_MODE).doubleValue();
            unit = Unit.INDEX;
        }
        if (unit == null)
            unit = Unit.INDEX;

        // set volume
        if (unit == Unit.INDEX)
            AudioUtils.SetVolumeIndex(context, value.intValue(), audioType);
        else
            AudioUtils.SetVolumePercentage(context, value.floatValue(), audioType);
    }

    enum Unit {
        PERCENT, INDEX
    }

    private static class CommandParamRingerMode extends CommandParam<Integer> {
        private CommandParamRingerMode(String id) {
            super(id);
        }

        @Override
        public Integer getValueFromInput(String input) throws Exception {
            if (input.matches("(?i)vibrate|vibration"))
                return AudioUtils.VOLUME_INDEX_RING_VIBRATE;
            if (input.matches("(?i)silent|mute"))
                return AudioUtils.VOLUME_INDEX_RING_SILENT;
            throw new IllegalArgumentException("Invalid ringer mode given.");
        }
    }

    private static class CommandParamUnit extends CommandParam<Unit> {
        private CommandParamUnit(String id) {
            super(id);
        }

        @Override
        public Unit getValueFromInput(String input) throws Exception {
            if (input.matches("(?i)%|percentage"))
                return Unit.PERCENT;
            else
                return Unit.INDEX;
        }
    }
}
