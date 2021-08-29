package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamNumber;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamUnit;
import tranquvis.simplesmsremote.Data.DataManager;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Services.PlaySoundService;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;
import tranquvis.simplesmsremote.Utils.UnitTools.Unit;
import tranquvis.simplesmsremote.Utils.UnitTools.UnitType;

public class CommandPlaySound
        extends Command {
    public static final int DEFAULT_DURATION = 10000; // in ms

    static final CommandParamNumber PARAM_DURATION_VALUE =
            new CommandParamNumber("duration_value");
    static final CommandParamUnit PARAM_DURATION_UNIT = new CommandParamUnit("duration_unit");

    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "play (?:(?:sound)|(?:ringtone))(?: (?:for )?([0-9.,]+)\\s*([a-z]+))?");

    public CommandPlaySound(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_audio_volume;
        this.syntaxDescList = new String[]{
                "play sound",
                "play sound for [duration]"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_IF_NOT_EMPTY,
                new PatternTreeNode(PARAM_DURATION_VALUE.getId(),
                        ".*",
                        MatchType.DO_NOT_MATCH
                ),
                new PatternTreeNode(PARAM_DURATION_UNIT.getId(),
                        Unit.GetFullPattern(UnitType.TIME),
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result, DataManager dataManager) throws Exception {
        int duration = DEFAULT_DURATION; // in ms
        if (commandInstance.isParamAssigned(PARAM_DURATION_VALUE)) {
            float durationValue = commandInstance.getParam(PARAM_DURATION_VALUE).floatValue();
            Unit durationUnit = commandInstance.getParam(PARAM_DURATION_UNIT);
            duration = (int) (durationValue * durationUnit.getFactor() * 1000f);
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PlaySoundService.start(context, uri, duration);
    }
}