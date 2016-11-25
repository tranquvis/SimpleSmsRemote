package tranquvis.simplesmsremote.CommandManagement.Commands;

import android.content.Context;
import android.support.annotation.NonNull;

import tranquvis.simplesmsremote.CommandManagement.CommandExecResult;
import tranquvis.simplesmsremote.CommandManagement.CommandInstance;
import tranquvis.simplesmsremote.CommandManagement.Modules.Module;
import tranquvis.simplesmsremote.CommandManagement.Params.CommandParamAudioType;
import tranquvis.simplesmsremote.Helper.AudioTypeHelper;
import tranquvis.simplesmsremote.R;
import tranquvis.simplesmsremote.Utils.Device.AudioUtils;
import tranquvis.simplesmsremote.Utils.Regex.MatchType;
import tranquvis.simplesmsremote.Utils.Regex.PatternTreeNode;

/**
 * Created by Andreas Kaltenleitner on 31.10.2016.
 */
public class CommandGetAudioVolume extends Command {
    static final CommandParamAudioType PARAM_AUDIO_TYPE = new CommandParamAudioType("audio_type");


    private static final String PATTERN_ROOT = AdaptSimplePattern(
            "(?:get|fetch|retrieve) (?:(?:(.*?) volume)|(?:volume (?:of|for) (.*?)))");

    public CommandGetAudioVolume(@NonNull Module module) {
        super(module);

        this.titleRes = R.string.command_title_get_audio_volume;
        this.syntaxDescList = new String[]{
                "get [audio type] volume"
        };
        this.patternTree = new PatternTreeNode("root",
                PATTERN_ROOT,
                MatchType.BY_INDEX_STRICT,
                new PatternTreeNode(PARAM_AUDIO_TYPE.getId(),
                        CommandParamAudioType.GetEntirePattern(),
                        MatchType.DO_NOT_MATCH
                )
        );
    }

    @Override
    public void execute(Context context, CommandInstance commandInstance,
                        CommandExecResult result) throws Exception {
        AudioUtils.AudioType audioType = commandInstance.getParam(PARAM_AUDIO_TYPE);
        String audioTypeStr = context.getString(AudioTypeHelper.GetNameResFromAudioType(audioType));

        int volumeIndex = AudioUtils.GetVolumeIndex(context, audioType);
        String volumeStr = String.valueOf(volumeIndex);

        if (volumeIndex == AudioUtils.VOLUME_INDEX_RING_VIBRATE)
            volumeStr = "vibrate";
        else if (volumeIndex == AudioUtils.VOLUME_INDEX_RING_SILENT)
            volumeStr = "silent";

        int maxIndex = AudioUtils.GetMaxVolumeIndex(context, audioType);
        String maxIndexStr = String.valueOf(maxIndex);

        result.setCustomResultMessage(context.getString(
                R.string.result_msg_audio_volume, audioTypeStr, volumeStr, maxIndexStr));
        result.setForceSendingResultSmsMessage(true);
    }
}